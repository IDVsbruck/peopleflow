package com.idvsbruck.pplflw.api.persistence.repository;

import com.idvsbruck.pplflw.api.dto.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(@Param("email") final String email);

    @Modifying
    @Query(nativeQuery = true)
    int deleteByEmail(@Param(value = "email") String email);
}
