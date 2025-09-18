package com.modus.EMS_TASK.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String department;
    private String designation;
    private String phoneNumber;
    private String address;
}

