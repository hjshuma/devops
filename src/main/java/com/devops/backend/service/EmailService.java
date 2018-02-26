package com.devops.backend.service;

import com.devops.web.domain.frontend.FeedbackPojo;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    public void sendFeedbackEmail(FeedbackPojo feedbackPojo);

    public void sendGenericEmailMessage(SimpleMailMessage message);
}
