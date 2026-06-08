package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.PasswordResetToken;
import com.harshkumar0614jain.worksphere.entity.User;
import com.harshkumar0614jain.worksphere.exception.BusinessException;
import com.harshkumar0614jain.worksphere.exception.ResourceNotFoundException;
import com.harshkumar0614jain.worksphere.model.ForgotPasswordRequest;
import com.harshkumar0614jain.worksphere.model.ResetPasswordRequest;
import com.harshkumar0614jain.worksphere.repository.EmployeeRepository;
import com.harshkumar0614jain.worksphere.repository.PasswordResetTokenRepository;
import com.harshkumar0614jain.worksphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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

//        find token by token string
        PasswordResetToken resetToken = passwordResetTokenRepo
                .findByToken(request.getToken())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "token","Invalid token "));

//        check token is not expired
        if(resetToken.getExpiryTime().isBefore(Instant.now()))
            throw new BusinessException("expiryToken","Expiry token is expired");

//        Check token is not already used
        if(resetToken.isUsed())
            throw new BusinessException("token",
                    "Token is already used");

//        find user by email from token
        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("email",
                        "User not found for this email :-"+resetToken.getEmail())
                );

//        Encode and update password
        String password = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(password);

//        Mark token as used
         resetToken.setUsed(true);

//         save both token and user
         passwordResetTokenRepo.save(resetToken);
         userRepository.save(user);


    }

}
