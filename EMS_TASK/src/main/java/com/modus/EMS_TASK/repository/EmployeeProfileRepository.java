package com.modus.EMS_TASK.repository;

import com.modus.EMS_TASK.entity.EmployeeProfile;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    Optional<Object> findByPhoneNumber(String phoneNumber);
}
