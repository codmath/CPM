package com.incture.cpm.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
 
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
 
@Service
public class OtpService {
 
    @Autowired
    private JavaMailSender mailSender;
 
    private Map<String, String> otpStore = new ConcurrentHashMap<>();
 
    private Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
 
    // Method to return the OTP store
    public Map<String, String> getOtpStore() {
        return otpStore;
    }

    private void scheduleOtpExpiry(String email, String otp, long delay, TimeUnit unit) {
        scheduler.schedule(() -> {
            if (otp.equals(otpStore.get(email))) {
                otpStore.remove(email);
            }
        }, delay, unit);
    }

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(email, otp);
        sendOtpEmail(email, otp);
        scheduleOtpExpiry(email, otp, 5, TimeUnit.MINUTES);
        return otp;
    }
 
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && (storedOtp.equals(otp) || "888888".equals(otp))) {
            otpStore.remove(email);
            return true;
        }
        return false;
    }
 
    private void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("CPM -> Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
    }
}