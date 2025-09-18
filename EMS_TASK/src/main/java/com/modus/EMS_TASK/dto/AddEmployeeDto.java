package com.modus.EMS_TASK.dto;

import com.modus.EMS_TASK.entity.EmployeeProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String department;
    private String designation;
    private String phoneNumber;
    private String address;
    private String enabled;

    // Mapping constructor
    public AddEmployeeDto(EmployeeProfile profile) {
        this.id = profile.getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.department = profile.getDepartment();
        this.designation = profile.getDesignation();
        this.phoneNumber = profile.getPhoneNumber();
        this.address = profile.getAddress();
        this.enabled = profile.getEnabled() != null ? profile.getEnabled() : "Y";
    }

}
