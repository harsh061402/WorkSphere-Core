package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.PasswordResetToken;
import com.harshkumar0614jain.worksphere.exception.ResourceNotFoundException;
import com.harshkumar0614jain.worksphere.model.ForgotPasswordRequest;
import com.harshkumar0614jain.worksphere.model.ResetPasswordRequest;
import com.harshkumar0614jain.worksphere.repository.EmployeeRepository;
import com.harshkumar0614jain.worksphere.repository.PasswordResetTokenRepository;
import com.harshkumar0614jain.worksphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepo;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public void forgotPassword(ForgotPasswordRequest request){

//      Check user exists by email
        if(!userRepository
                .existsByEmail(request.getEmail()))
            throw new ResourceNotFoundException("email","Invalid email");

//      Delete any existing token for this email
        if(passwordResetTokenRepo.existsByEmail(request.getEmail()))
            passwordResetTokenRepo.deleteByEmail(request.getEmail());

//      Generate new token using UUID
        String token = UUID.randomUUID().toString();

//      Set expiry 15 minutes for now
        Instant expiryTime = Instant.now().plus(15, ChronoUnit.MINUTES);

//      Save Token
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .email(request.getEmail())
                .expiryTime(expiryTime)
                .used(false)
                .build();

        passwordResetTokenRepo.save(passwordResetToken);

//      Reset link
        String resetLink = baseUrl + "/api/auth/reset-password?token=" + token;

//      Send Email with reset link
        emailService.sendEmail(request.getEmail(),
                "Password Reset Request",
                "Hello,\n" +
                        "\n" +
                        "We received a request to reset your password.\n" +
                        "\n" +
                        "Please click the link below to reset your password:\n" +
                        resetLink +
                        "\n" +
                        "If you did not request this, please ignore this email.\n" +
                        "\n" +
                        "Thanks,\n" +
                        "Support Team");
    }

    public void resetPassword(ResetPasswordRequest request){

    }

}
