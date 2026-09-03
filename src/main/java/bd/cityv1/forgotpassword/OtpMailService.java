package bd.cityv1.forgotpassword;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpMailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("CityFix - Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp +
                "\nThis OTP is valid for 5 minutes." +
                "\nIf you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}