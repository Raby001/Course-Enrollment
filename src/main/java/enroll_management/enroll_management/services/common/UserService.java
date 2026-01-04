package enroll_management.enroll_management.services.common;
import enroll_management.enroll_management.enums.RoleName;

import enroll_management.enroll_management.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalStudents() {
        return userRepository.countByRoleName(RoleName.STUDENT);  // Adjust based on your Role enum or field
    }

    public long getTotalLecturers() {
        return userRepository.countByRoleName(RoleName.LECTURER);
    }

    public long getNewUsersThisWeek() {
        LocalDateTime mondayThisWeek = LocalDateTime.now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .toLocalDate()                    // Convert to date only
            .atStartOfDay();                  // 00:00:00 time

        return userRepository.countNewUsersSince(mondayThisWeek);
    }
}
