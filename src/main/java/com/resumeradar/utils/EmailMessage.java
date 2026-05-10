package com.resumeradar.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailMessage {

	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    private static String loginUrl = "www.resumeradar.com/login";
    
    private static String supportEmail = "support@resumeradar.com";

    public void sendRegistrationEmail(String toEmail, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", toEmail);
        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportEmail", supportEmail);

        String htmlContent = templateEngine.process("registration-email", context);

        sendMail(toEmail , "Welcome to ResumeRadar – Registration Successful!" , htmlContent , fromEmail);
    }
    
    public void sendPasswordResetEmail(String toEmail, String name, Integer otp) throws MessagingException {
        // Thymeleaf context setup
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("otp", otp);
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportEmail", supportEmail);

        // Process the HTML template
        String htmlContent = templateEngine.process("forget-password-email", context);

        sendMail(toEmail , "Password Reset - ResumeRadar" , htmlContent , fromEmail);
    }
    
    public void sendPasswordUpdateConfirmation(String toEmail, String name) throws MessagingException {
        // Prepare Thymeleaf context
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportUrl", supportEmail);

        // Generate HTML from template
        String htmlContent = templateEngine.process("password-updated-email", context);

        sendMail(toEmail , "Your Password Has Been Updated" , htmlContent , fromEmail);
    }
    
    
    public void sendJobApplicationConfirmation(
            String toEmail,
            String name,
            String jobTitle,
            String companyName
    ) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("jobTitle", jobTitle);
        context.setVariable("companyName", companyName);
        context.setVariable("submittedDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        context.setVariable("supportEmail", supportEmail);

        String htmlContent = templateEngine.process("job-application-success", context);

        sendMail(toEmail , "Application Submitted – " + jobTitle + " at " + companyName , htmlContent , fromEmail);
    }
    
    public void sendJobInProcessNotification(
            String toEmail,
            String name,
            String jobTitle,
            String companyName
    ) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("jobTitle", jobTitle);
        context.setVariable("companyName", companyName);
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("supportEmail", supportEmail);

        String htmlContent = templateEngine.process("job-in-process-email", context);

        sendMail(toEmail , "Your Application is Under Review – " + jobTitle , htmlContent , fromEmail);
        
    }
    
    private void sendMail(String toEmail , String subject , String htmlContent , String fromEmail) throws MessagingException {
    	MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(fromEmail);

        mailSender.send(message);
    }


}
