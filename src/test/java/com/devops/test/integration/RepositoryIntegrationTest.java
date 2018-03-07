package com.devops.test.integration;

import com.devops.DevopsApplication;
import com.devops.backend.persistence.domain.backend.Plan;
import com.devops.backend.persistence.domain.backend.Role;
import com.devops.backend.persistence.domain.backend.User;
import com.devops.backend.persistence.domain.backend.UserRole;
import com.devops.backend.persistence.repositories.PlanRepository;
import com.devops.backend.persistence.repositories.RoleRepository;
import com.devops.backend.persistence.repositories.UserRepository;
import com.devops.enums.PlansEnum;
import com.devops.enums.RolesEnum;
import com.devops.utils.UsersUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevopsApplication.class)
@WebAppConfiguration
public class RepositoryIntegrationTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception {
        Plan basicPlan = createPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Plan retrievedPlan = planRepository.findOne(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void testCreateNewRole() throws Exception {

        Role userRole = createRole(RolesEnum.BASIC);
        roleRepository.save(userRole);

        Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);

    }

    @Test
    public void createNewUser() throws Exception {

        Plan basicPlan = createPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UsersUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createRole(RolesEnum.BASIC);
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);

        for (UserRole ur : userRoles) {
            roleRepository.save(ur.getRole());
        }

        basicUser = userRepository.save(basicUser);
        User newlyCreateUser = userRepository.findOne(basicUser.getId());
        Assert.assertNotNull(newlyCreateUser);
        Assert.assertTrue(newlyCreateUser.getId() != 0);
        Assert.assertNotNull(newlyCreateUser.getPlan());
        Assert.assertNotNull(newlyCreateUser.getPlan().getId());
        Set<UserRole> newlyCreatedUserUserRole = newlyCreateUser.getUserRoles();
        for (UserRole ur : newlyCreatedUserUserRole) {
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }

    }

    private Plan createPlan(PlansEnum plansEnum) {
        return new Plan(plansEnum);
    }

    private Role createRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }

}
