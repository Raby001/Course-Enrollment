package enroll_management.enroll_management.services.common;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.enums.RoleName;

import enroll_management.enroll_management.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {
    @Autowired
    private UserRepository userRepository;

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalStudents() {
        return userRepository.countByRoleName(RoleName.STUDENT);  
    }

    public long getTotalLecturers() {
        return userRepository.countByRoleName(RoleName.LECTURER);
    }

    public long getNewUsersThisWeek() {
        LocalDateTime mondayThisWeek = LocalDateTime.now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .toLocalDate()                  
            .atStartOfDay();                

        return userRepository.countNewUsersSince(mondayThisWeek);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); 
        return userRepository.findByUsername(username).orElse(null);
    }

}