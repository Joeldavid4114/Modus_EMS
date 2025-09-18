package com.modus.EMS_TASK.controller;

import com.modus.EMS_TASK.dto.AddEmployeeDto;
import com.modus.EMS_TASK.dto.UserRegistrationDto;
import com.modus.EMS_TASK.entity.EmployeeProfile;
import com.modus.EMS_TASK.entity.User;
import com.modus.EMS_TASK.repository.EmployeeProfileRepository;
import com.modus.EMS_TASK.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminController {

    private final EmployeeProfileRepository profileRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    //  pagination and sorting
    @GetMapping("/dashboard")
    public ResponseEntity<Page<AddEmployeeDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<EmployeeProfile> profiles = profileRepo.findAll(pageable);
        Page<AddEmployeeDto> dtoPage = profiles.map(AddEmployeeDto::new);
        return ResponseEntity.ok(dtoPage);
    }




    @PostMapping("/add-employee")
    public ResponseEntity<String> addEmployee(@RequestBody AddEmployeeDto request) {
        if (profileRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Employee with this phone number already exists");
        }

        EmployeeProfile profile = new EmployeeProfile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDepartment(request.getDepartment());
        profile.setDesignation(request.getDesignation());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());


        profile.setEnabled(request.getEnabled() != null ? request.getEnabled() : "Y");

        profileRepo.save(profile);

        return ResponseEntity.ok("Employee profile added successfully");
    }



    @PatchMapping("/profiles/{id}/enable")
    public ResponseEntity<String> enableProfile(@PathVariable Long id) {
        Optional<EmployeeProfile> optionalProfile = profileRepo.findById(id);
        if (optionalProfile.isEmpty()) {
            return ResponseEntity.badRequest().body("Employee profile not found");
        }

        EmployeeProfile profile = optionalProfile.get();
        profile.setEnabled("Y");
        profileRepo.save(profile);

        return ResponseEntity.ok("Employee profile enabled");
    }

    @PatchMapping("/profiles/{id}/disable")
    public ResponseEntity<String> disableProfile(@PathVariable Long id) {
        Optional<EmployeeProfile> optionalProfile = profileRepo.findById(id);
        if (optionalProfile.isEmpty()) {
            return ResponseEntity.badRequest().body("Employee profile not found");
        }

        EmployeeProfile profile = optionalProfile.get();
        profile.setEnabled("N");
        profileRepo.save(profile);

        return ResponseEntity.ok("Employee profile disabled");
    }


    @PutMapping("/users/{id}/profile")
    public ResponseEntity<String> updateEmployeeProfile(@PathVariable Long id, @RequestBody UserRegistrationDto request) {
        Optional<EmployeeProfile> optionalProfile = profileRepo.findById(id);
        if (optionalProfile.isEmpty()) {
            return ResponseEntity.badRequest().body("Employee profile not found");
        }

        EmployeeProfile profile = optionalProfile.get();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDepartment(request.getDepartment());
        profile.setDesignation(request.getDesignation());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());

        profileRepo.save(profile);
        return ResponseEntity.ok("Employee profile updated successfully");
    }


//    @GetMapping("/users/{id}/profile")
//    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
//        Optional<User> optionalUser = userRepo.findById(id);
//        if (optionalUser.isEmpty()) {
//            return ResponseEntity.badRequest().body("User not found");
//        }
//
//        User user = optionalUser.get();
//        EmployeeProfile profile = user.getProfile();
//        return ResponseEntity.ok(profile);
//    }
}
