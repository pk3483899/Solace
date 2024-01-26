package com.nobroker.service;

import com.nobroker.entity.User;
import com.nobroker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailVerificationService {

    //This is static in nature so, we need to do static import of this object/variable.
    static final Map<String, String> emailOtpMapping = new HashMap<>();

    @Autowired
    private Userservice userservice;

    @Autowired
    private EmailService emailService;

    public Map<String, String> verifyOtp(String email, String otp) {//otp ending to the url
//        String storedOtp = emailOtpMapping.get(email);//it takes email and sends otp after sending to url
        String storedOtp = emailOtpMapping.get(email);
        Map<String, String> response = new HashMap<>();


        if (storedOtp != null && storedOtp.equals(otp)) {
            User user = userservice.getUserByEmail(email);
            if (user != null) {
                userservice.verifyEmail(user);
                emailOtpMapping.remove(email);//it will remove the otp after varifyEamil
                response.put("status", "Success");
                response.put("message", "Email verified Sucessfully");
            } else {
                response.put("status", "error");
                response.put("message", "User not found");
            }
        } else {
            response.put("status", "error");
            response.put("message", "Invalid Otp");
        }
        return response;
    }
//send otp for login
    public Map<String, String> sendOtpForLogin(String email) {
        if (userservice.isEmailVerified(email)) {
            String otp = emailService.generateOtp();//it call the generateOtp method
            emailOtpMapping.put(email, otp);//store tem. in hashmap

            //send otp to the user's email
            emailService.sendOtpEmail(email);

            Map<String, String> response = new HashMap<>();
            response.put("Status", "Sucess");
            response.put("message", "OTP sent successfully");
            return response;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Email is not verified");
            return response;

        }
    }

    //verify otp fro login
    public Map<String, String> verifyOtpForEmail(String email, String otp) {
        String storedOtp = emailOtpMapping.get(email);

        Map<String, String> response=new HashMap<>();
        if(storedOtp!=null && storedOtp.equals(otp)){
            emailOtpMapping.remove(email);
            //otp is valid
            response.put("status","Success");
            response.put("message", "Otp verified Successfully");

        }else {
            //Invalid Otp
            response.put("Status","error");
            response.put("message","Invalid Otp");
        }
        return response;
    }
}
