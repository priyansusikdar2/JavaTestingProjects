package com.testing.demo.jmeter1;

import com.testing.demo.model.User;
import com.testing.demo.repository.UserRepository;
import com.testing.demo.service.EmailService;
import com.testing.demo.service.UserService;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.UUID;

public class UserServiceSampler extends AbstractJavaSamplerClient {
    private UserService userService;
    private UserRepository userRepository;
    private EmailService emailService;

    @Override
    public void setupTest(JavaSamplerContext context) {
        // Initialize services once before all tests
        userRepository = new UserRepository();
        emailService = new EmailService();
        userService = new UserService(userRepository, emailService);
        System.out.println("JMeter: UserService initialized");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            // Generate unique user
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);
            String username = "jmeter_user_" + uniqueId;
            String email = username + "@test.com";
            int age = 20 + (int)(Math.random() * 50);

            // Execute the service method
            User user = userService.createUser(username, email, age);

            // Mark as successful
            result.setSuccessful(true);
            result.setResponseMessage("User created successfully with ID: " + user.getId());
            result.setResponseCode("200");
            result.setResponseData("User: " + username, "UTF-8");

        } catch (Exception e) {
            // Mark as failed
            result.setSuccessful(false);
            result.setResponseMessage("Failed to create user: " + e.getMessage());
            result.setResponseCode("500");
            result.setResponseData("Error: " + e.getMessage(), "UTF-8");
        }

        result.sampleEnd();
        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        // Clean up if needed
        if (userRepository != null) {
            userRepository.clear();
            System.out.println("JMeter: Cleaned up user repository");
        }
    }
}
