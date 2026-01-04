package enroll_management.enroll_management.services.student;


import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.common.ImageUploadService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@Transactional
public class StudentProfileService {

    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    public StudentProfileService(UserRepository userRepository, ImageUploadService imageUploadService) {
        this.userRepository = userRepository;
        this.imageUploadService = imageUploadService;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void updateProfile(String firstName, String lastName, String email, LocalDate dob) {
        User user = getCurrentUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDob(dob);
        userRepository.save(user);
    }

    public void uploadProfileImage(MultipartFile image) throws IOException {
        User user = getCurrentUser();
        String imageUrl = imageUploadService.uploadImage(image);
        user.setUserImage(imageUrl);
        userRepository.save(user);
    }
}