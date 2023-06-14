package nl.uwv.otod.otod_portal.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j2;
import net.bytebuddy.utility.RandomString;
import nl.uwv.otod.otod_portal.exception.UserNotFoundException;
import nl.uwv.otod.otod_portal.mail.MailSender;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.service.UserService;
import nl.uwv.otod.otod_portal.util.Utility;

@Controller
@Log4j2
public class ForgotPasswordController {
     
    @Autowired
    private UserService userService;
     
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }
    
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
    	String email = request.getParameter("email");
        String token = RandomString.make(30);
         
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
             
        } catch (UserNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        }
             
        return "forgot_password_form";
    }
     
    public void sendEmail(String recipientEmail, String link) {
    	try {
			new MailSender().sendMail(recipientEmail, link);
		} catch (IOException e) {
			log.error(e.toString());
			e.printStackTrace();
		}
    }
     
     
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
    	
    	log.info("Get /reset_password");
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
         
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
         
        return "reset_password_form";
    }     
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
    	log.info("Post /reset_password");
        String token = request.getParameter("token");
        String password = request.getParameter("password");
         
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");
         
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            log.info("Invalid token, redirecting to message");
            return "message";
        } else {           
        	log.info("Successfully reeset password");
            userService.updatePassword(user, password);
             
            model.addAttribute("message", "Wachtwoord succesvol gewijzigd. ");
        }
         log.info("Returning to success");
        return "message";
    }
    
    /*
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        
        mailSender.setUsername("roderikd@gmail.com");
        mailSender.setPassword("UN#k-/J^2@Bq");
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        
        return mailSender;
    }*/
}
