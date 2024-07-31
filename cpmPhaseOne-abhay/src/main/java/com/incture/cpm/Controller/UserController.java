package com.incture.cpm.Controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Dto.UserDto;
import com.incture.cpm.Entity.User;
import com.incture.cpm.Service.CustomUserDetailsService;
import com.incture.cpm.Service.OtpService;
import com.incture.cpm.Service.UserService;
import com.incture.cpm.Util.JwtUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/security")
@Slf4j
public class UserController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public String home() {
        return "Welcome to the home page!";
    }

    @GetMapping("/user")
    public ResponseEntity<?> userAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication object: " + authentication);
        if (authentication != null) {
            System.out.println("Is authenticated: " + authentication.isAuthenticated());
            System.out.println("Principal: " + authentication.getPrincipal());
            System.out.println("Authorities: " + authentication.getAuthorities());
        }

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(getUserDetails(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Admin Content.";
    }

    @GetMapping("/allOtp")
    public Map<String, String> getOtpStore() {
        return otpService.getOtpStore();
    }

    @PostMapping("/generateOtp") // from /register
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        otpService.generateOtp(email);
        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String email, @RequestParam String password, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return userService.changePassword(email, password);
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @PostMapping("/register") // registration with otp verification
    public ResponseEntity<?> registerUser(@RequestParam String email,
            @RequestParam String password, @RequestParam String talentName, @RequestParam String inctureId,
            @RequestParam String otp) {
        System.out.println("Register endpoint hit with email: " + email);
        boolean isValid = otpService.verifyOtp(email, otp);
        
        if (isValid) {
            Set<String> roles = new HashSet<>();
            roles.add("USER");

            String message = userService.registerUser(email, password, roles, talentName, inctureId);
            if(message == "User")                   return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
            else if(message == "UnauthorizedUser")  return new ResponseEntity<>("User registered successfully", HttpStatus.I_AM_A_TEAPOT);
            else                                    return new ResponseEntity<>("User not registered", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            // Create headers and add JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);

            log.info("Login successful with jwt : {}", jwt);
            return new ResponseEntity<>(getUserDetails(userService.findByEmail(email)), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestParam String email,
            @RequestParam String password, @RequestParam String talentName, @RequestParam String inctureId) {
        System.out.println("Register endpoint hit with email: " + email);
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN".toUpperCase());
        roles.add("USER".toUpperCase());
        userService.registerUser(email, password, roles, talentName, inctureId);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/admin/addRole")
    public ResponseEntity<?> addRole(@RequestParam String email, @RequestParam String role) {
        try {
            userService.addRole(email, role);
            return ResponseEntity.ok("Role added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String password) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            userService.changePassword(email, password);
            return ResponseEntity.ok("Role added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/returnUser")
    public UserDto getUserDetails(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        userDto.setTalentId(user.getTalentId());
        userDto.setTalentName(user.getTalentName());
        userDto.setInctureId(user.getInctureId());
        userDto.setPhoto(user.getPhoto());
        // Set other necessary fields
        return userDto;
    }

    @DeleteMapping("/admin/deleteUser/{id}")// Ensure only admins can delete users
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or could not be deleted");
        }
    }

    @PutMapping("/photo/{id}")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        try {
            userService.saveUserPhoto(id, photo);
            return new ResponseEntity<>("Photo uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload photo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable Long id) {
        byte[] photo = userService.getUserPhoto(id);
        String contentType = userService.getUserPhotoContentType(id);
        if (photo != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));  // Use the stored content type
            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}