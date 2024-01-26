package com.nobroker.controller;

import com.nobroker.entity.User;
import com.nobroker.service.EmailService;
import com.nobroker.service.EmailVerificationService;
import com.nobroker.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    //It register the user ,
    // send an otp to the registered email id,
    // and verify the email and update the status to true in db
    private Userservice userservice;
    private EmailService emailService;
    private EmailVerificationService emailVerificationService;

    //Constructor Based Dependency Injection
    public RegistrationController(Userservice userservice, EmailService emailService, EmailVerificationService emailVerificationService) {
        this.userservice = userservice;
        this.emailService = emailService;
        this.emailVerificationService=emailVerificationService;
    }

    //http://localhost:8080/api/register
    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody User user){
        //it will save the user details
        User saveUser = userservice.registerUser(user);

        //once you save the user details it will a hashmap
        //It will send an OTP to the register emailId
        return emailService.sendOtpEmail(user.getEmail());
    }

//    http://localhost:8080/api/verify-otp?email=&otp=
    @PostMapping("/verify-otp")
    public Map<String , String > verifyOTP(@RequestParam String email, @RequestParam String otp){
        return emailVerificationService.verifyOtp(email, otp);
    }


}
