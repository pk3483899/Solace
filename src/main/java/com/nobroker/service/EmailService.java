package com.nobroker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import static com.nobroker.service.EmailVerificationService.emailOtpMapping;



@Service
public class EmailService {

    private JavaMailSender javaMailSender;
    private Userservice userservice;

//Constructor based dependency Injection
    public EmailService(JavaMailSender javaMailSender, Userservice userservice) {
        this.javaMailSender= javaMailSender;
        this.userservice = userservice;
    }


    //It will generate a random OTP
    public String generateOtp() {
//        It generates random 6-digit number by using Random()..
        return String.format("%06d", new java.util.Random().nextInt(1000000));
    }


//Map is used to store the data into key value pair
    //key: email id
    //Value: generated OTP
    public Map<String, String> sendOtpEmail(String email) {
        //it will call the method genereate otp and return 6 digit random number
        String otp = generateOtp();//generates an otp

        //After generating the opt we need to store the opt on teprarely basis on hashmap(key value pair)
          emailOtpMapping.put(email,otp);//storing the otp in hashmap

        sendEmail(email, "OTP for email varification", "Your OTP is : "+otp);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "OTP sent successfully");
        return response;
    }

    private void sendEmail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abc@gmail.com");//can be any thing also
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        //it sends the mail to the registered email id
        javaMailSender.send(message);
    }
    }