package com.example.employee_service.service;


import com.example.employee_service.dto.CreateEmployeeInternalRequest;
import com.example.employee_service.dto.EmployeeProfileUpdateRequest;
import com.example.employee_service.dto.EmployeeRequest;
import com.example.employee_service.dto.EmployeeResponse;
import com.example.employee_service.enums.EmployeeStatus;
import com.example.employee_service.exception.*;
import com.example.employee_service.models.Employee;
import com.example.employee_service.repository.EmployeeRepository;
import com.example.employee_service.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final static Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        log.info("Creating employee.");

        if (employeeRepository.existsByEmail(request.getEmail())) {

            log.warn("Employee creation failed. Email already exists.");
            throw new ResourceAlreadyExistsException("Email already exists.");

        }

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {

            log.warn("Employee creation failed. Employee code already exists.");
            throw new ResourceAlreadyExistsException("Employee code already exists.");

        }

        if (employeeRepository.existsByPhone(request.getPhone())) {

            log.warn("Employee creation failed. Phone number already exists.");
            throw new ResourceAlreadyExistsException("Phone number already exists.");
        }

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setQualification(request.getQualification());
        employee.setExperience(request.getExperience());
        employee.setHireDate(LocalDate.now());
        employee.setStatus(request.getStatus());

        Employee savedEmployee = employeeRepository.save(employee);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(savedEmployee.getId());
        response.setEmployeeCode(savedEmployee.getEmployeeCode());
        response.setFirstName(savedEmployee.getFirstName());
        response.setLastName(savedEmployee.getLastName());
        response.setEmail(savedEmployee.getEmail());
        response.setPhone(savedEmployee.getPhone());
        response.setDepartment(savedEmployee.getDepartment());
        response.setDesignation(savedEmployee.getDesignation());
        response.setQualification(savedEmployee.getQualification());
        response.setExperience(savedEmployee.getExperience());
        response.setHireDate(savedEmployee.getHireDate());
        response.setStatus(savedEmployee.getStatus());

        log.info(
                "Employee created successfully. employeeId={}, employeeCode={}",
                savedEmployee.getId(),
                savedEmployee.getEmployeeCode()
        );

        return response;

    }


    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        log.debug("Fetching employee. employeeId={}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Employee not found. employeeId={}",
                            id
                    );

                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        Long authUserId = AuthenticatedUser.getUserId();

        if (!AuthenticatedUser.isAdmin() && !employee.getAuthUserId().equals(authUserId)) {

            log.warn(
                    "Access denied while fetching employee. " +
                            "employeeId={}, authUserId={}",
                    id,
                    authUserId
            );


            throw new AccessDeniedException("You are not allowed to access this resource.");
        }

        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setDepartment(employee.getDepartment());
        response.setDesignation(employee.getDesignation());
        response.setQualification(employee.getQualification());
        response.setExperience(employee.getExperience());
        response.setHireDate(employee.getHireDate());
        response.setStatus(employee.getStatus());

        return response;


    }




    @Override
    public EmployeeResponse getInternalEmployeeById(Long id) {

        log.debug("Fetching employee through internal endpoint. employeeId={}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Employee not found through internal endpoint. employeeId={}",
                            id
                    );

                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });


        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setDepartment(employee.getDepartment());
        response.setDesignation(employee.getDesignation());
        response.setQualification(employee.getQualification());
        response.setExperience(employee.getExperience());
        response.setHireDate(employee.getHireDate());
        response.setStatus(employee.getStatus());

        return response;


    }


    @Override
    public List<EmployeeResponse> getAllEmployees() {

        List<Employee> employees = employeeRepository.findAll();

        List<EmployeeResponse> responses = new ArrayList<>();

        for (Employee employee: employees) {

            EmployeeResponse response = new EmployeeResponse();

            response.setId(employee.getId());
            response.setEmployeeCode(employee.getEmployeeCode());
            response.setFirstName(employee.getFirstName());
            response.setLastName(employee.getLastName());
            response.setEmail(employee.getEmail());
            response.setPhone(employee.getPhone());
            response.setDesignation(employee.getDesignation());
            response.setDepartment(employee.getDepartment());
            response.setExperience(employee.getExperience());
            response.setQualification(employee.getQualification());
            response.setHireDate(employee.getHireDate());
            response.setStatus(employee.getStatus());

            responses.add(response);

        }

        return  responses;

    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " +id
                        ));

        Long authUserId = AuthenticatedUser.getUserId();

        if (!AuthenticatedUser.isAdmin() && !employee.getAuthUserId().equals(authUserId)) {
            throw new AccessDeniedException("You are not allowed to access this resource.");
        }


        employeeRepository.findByEmail(request.getEmail())
                .ifPresent(existingEmployee -> {
                    if (!existingEmployee.getId().equals(id)) {
                        throw new ResourceAlreadyExistsException("Email already exists.");

                    }
                });

        employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                .ifPresent(existingEmployee -> {
                    if (!existingEmployee.getId().equals(id)) {
                        throw new ResourceAlreadyExistsException(
                                "Employee code already exist."
                        );
                    }
                });

        employeeRepository.findByPhone(request.getPhone())
                .ifPresent(existingEmployee -> {
                    if (!existingEmployee.getId().equals(id)) {
                        throw new ResourceAlreadyExistsException(
                                "Phone number already exist."
                        );
                    }
                });

        if (AuthenticatedUser.isAdmin()) {
            employee.setEmployeeCode(request.getEmployeeCode());
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setEmail(request.getEmail());
            employee.setPhone(request.getPhone());
            employee.setDesignation(request.getDesignation());
            employee.setDepartment(request.getDepartment());
            employee.setExperience(request.getExperience());
            employee.setQualification(request.getQualification());
            employee.setStatus(request.getStatus());


        }

        Employee updatedEmployee = employeeRepository.save(employee);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(updatedEmployee.getId());
        response.setEmployeeCode(updatedEmployee.getEmployeeCode());
        response.setFirstName(updatedEmployee.getFirstName());
        response.setLastName(updatedEmployee.getLastName());
        response.setEmail(updatedEmployee.getEmail());
        response.setPhone(updatedEmployee.getPhone());
        response.setDepartment(updatedEmployee.getDepartment());
        response.setDesignation(updatedEmployee.getDesignation());
        response.setExperience(updatedEmployee.getExperience());
        response.setQualification(updatedEmployee.getQualification());
        response.setHireDate(updatedEmployee.getHireDate());
        response.setStatus(updatedEmployee.getStatus());

        return response;


    }

    public EmployeeResponse updateEmployeeProfile(
            Long id,
            EmployeeProfileUpdateRequest request) {

        log.info("Updating employee profile. employeeId={}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Employee not found for profile update. employeeId={}",
                            id
                    );

                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        Long authUserId = AuthenticatedUser.getUserId();

        if (!AuthenticatedUser.isAdmin() && !employee.getAuthUserId().equals(authUserId)) {

            log.warn(
                    "Access denied while updating employee profile. " +
                            "employeeId={}, authUserId={}",
                    id,
                    authUserId
            );

            throw new AccessDeniedException("You are not allowed to access this resource.");
        }


        employeeRepository.findByEmail(request.getEmail())
                .ifPresent(existingEmployee -> {

                    if (!existingEmployee.getId().equals(id)) {

                        log.warn(
                                "Employee profile update failed. " +
                                        "Email already exists. employeeId={}",
                                id
                        );

                        throw new ResourceAlreadyExistsException("Email already exists.");
                    }
                });




        employeeRepository.findByPhone(request.getPhone())
                .ifPresent(existingEmployee -> {

                    if (!existingEmployee.getId().equals(id)) {

                        log.warn(
                                "Employee profile update failed. " +
                                        "Phone number already exists. employeeId={}",
                                id
                        );

                        throw new ResourceAlreadyExistsException("Phone number already exist.");
                    }
                });

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setQualification(request.getQualification());
        employee.setExperience(request.getExperience());

        Employee updatedEmployee = employeeRepository.save(employee);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(updatedEmployee.getId());
        response.setEmployeeCode(updatedEmployee.getEmployeeCode());
        response.setFirstName(updatedEmployee.getFirstName());
        response.setLastName(updatedEmployee.getLastName());
        response.setEmail(updatedEmployee.getEmail());
        response.setPhone(updatedEmployee.getPhone());
        response.setDepartment(updatedEmployee.getDepartment());
        response.setDesignation(updatedEmployee.getDesignation());
        response.setExperience(updatedEmployee.getExperience());
        response.setQualification(updatedEmployee.getQualification());
        response.setHireDate(updatedEmployee.getHireDate());
        response.setStatus(updatedEmployee.getStatus());

        log.info("Employee profile updated successfully. employeeId={}", updatedEmployee.getId());

        return  response;
    }


    @Override
    public EmployeeResponse patchEmployee(Long id, EmployeeRequest request) {

        log.info("Patching employee. employeeId={}", id);

        // check if employee exist
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Employee not found for patch. employeeId={}",
                            id
                    );

                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        Long authUserId = AuthenticatedUser.getUserId();

        if (!AuthenticatedUser.isAdmin() && !employee.getAuthUserId().equals(authUserId)) {

            log.warn(
                    "Access denied while patching employee. " +
                            "employeeId={}, authUserId={}",
                    id,
                    authUserId
            );

            throw new AccessDeniedException("You are not allowed to access this resource.");
        }

        if (AuthenticatedUser.isAdmin()) {

            // apply every supplied field
            // check if employee code  is already taken by another user
            if (request.getEmployeeCode() != null ){
                employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(id)) {

                                log.warn(
                                        "Employee patch failed. Employee code already exists. " +
                                                "employeeId={}",
                                        id
                                );

                                throw new ResourceAlreadyExistsException("Employee code already exist.");
                            }
                        });

                employee.setEmployeeCode(request.getEmployeeCode());

            }

            // check if first name field is empty in dto
            if (request.getFirstName() != null) {
                employee.setFirstName(request.getFirstName());
            }

            // check if last name field is empty in dto
            if (request.getLastName() != null) {
                employee.setLastName(request.getLastName());
            }

            // check if email field is empty in dto
            if (request.getEmail() != null) {

                // check if email is already taken by another employee
                employeeRepository.findByEmail(request.getEmail())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(id)) {

                                log.warn(
                                        "Employee patch failed. Email already exists. " +
                                                "employeeId={}",
                                        id
                                );

                                throw new ResourceAlreadyExistsException("Email already exist.");
                            }
                        });

                employee.setEmail(request.getEmail());

            }

            if (request.getPhone() != null) {
                employeeRepository.findByPhone(request.getPhone())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(id)) {

                                log.warn(
                                        "Employee patch failed. Phone number already exists. " +
                                                "employeeId={}",
                                        id
                                );

                                throw new ResourceAlreadyExistsException("Phone number already exist.");
                            }
                        });

                employee.setPhone(request.getPhone());
            }

            if (request.getDepartment() != null) {
                employee.setDepartment(request.getDepartment());
            }

            if (request.getDesignation() != null) {
                employee.setDesignation(request.getDesignation());
            }

            if (request.getExperience() != null) {
                employee.setExperience(request.getExperience());
            }

            if (request.getQualification() != null) {
                employee.setQualification(request.getQualification());
            }

            if (request.getStatus() != null) {
                employee.setStatus(request.getStatus());
            }

        } else {

            // check if first name field is empty in dto
            if (request.getFirstName() != null) {
                employee.setFirstName(request.getFirstName());
            }

            // check if last name field is empty in dto
            if (request.getLastName() != null) {
                employee.setLastName(request.getLastName());
            }

            // check if email field is empty in dto
            if (request.getEmail() != null) {

                // check if email is already taken by another employee
                employeeRepository.findByEmail(request.getEmail())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(id)) {

                                log.warn(
                                        "Employee patch failed. Email already exists. " +
                                                "employeeId={}",
                                        id
                                );

                                throw new ResourceAlreadyExistsException(
                                        "Email already exist."
                                );
                            }
                        });
                employee.setEmail(request.getEmail());
            }

            if (request.getPhone() != null) {
                employeeRepository.findByPhone(request.getPhone())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(id)) {

                                log.warn(
                                        "Employee patch failed. Phone number already exists. " +
                                                "employeeId={}",
                                        id
                                );

                                throw new ResourceAlreadyExistsException("Phone number already exist.");
                            }
                        });


                employee.setPhone(request.getPhone());
            }



            if (request.getExperience() != null) {
                employee.setExperience(request.getExperience());
            }

            if (request.getQualification() != null) {
                employee.setQualification(request.getQualification());
            }

        }




        // save updated employee to database
        Employee updatedEmployee = employeeRepository.save(employee);

        // convert Entity to dto
        EmployeeResponse response = new EmployeeResponse();

        response.setId(updatedEmployee.getId());
        response.setEmployeeCode(updatedEmployee.getEmployeeCode());
        response.setFirstName(updatedEmployee.getFirstName());
        response.setLastName(updatedEmployee.getLastName());
        response.setEmail(updatedEmployee.getEmail());
        response.setPhone(updatedEmployee.getPhone());
        response.setDepartment(updatedEmployee.getDepartment());
        response.setDesignation(updatedEmployee.getDesignation());
        response.setExperience(updatedEmployee.getExperience());
        response.setQualification(updatedEmployee.getQualification());
        response.setHireDate(updatedEmployee.getHireDate());
        response.setStatus(updatedEmployee.getStatus());

        return response;

    }




    @Override
    public void deleteEmployee(Long id) {


        log.info("Deleting employee. employeeId={}", id);

        // check if employee exist
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Employee not found for deletion. employeeId={}",
                            id
                    );

                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });



        // delete employee
        employee.setStatus(EmployeeStatus.DELETED);
        employeeRepository.save(employee);

        log.info("Employee marked as deleted successfully. employeeId={}", id);

    }

    @Override
    public List<EmployeeResponse> getEmployeeByIds(List<Long> ids) {

        List<Employee> employees = employeeRepository.findAllById(ids);

        List<EmployeeResponse> responses = new ArrayList<>();

        for (Employee employee: employees) {

            EmployeeResponse response = new EmployeeResponse();

            response.setId(employee.getId());
            response.setEmployeeCode(employee.getEmployeeCode());
            response.setFirstName(employee.getFirstName());
            response.setLastName(employee.getLastName());
            response.setEmail(employee.getEmail());
            response.setPhone(employee.getPhone());
            response.setDesignation(employee.getDesignation());
            response.setDepartment(employee.getDepartment());
            response.setExperience(employee.getExperience());
            response.setQualification(employee.getQualification());
            response.setHireDate(employee.getHireDate());
            response.setStatus(employee.getStatus());

            responses.add(response);

        }

        return  responses;

    }

    public EmployeeResponse createInternalEmployee(CreateEmployeeInternalRequest request){

        log.info("Creating employee through internal service request. authUserId={}", request.getAuthUserId());


        if (employeeRepository.existsByEmail(request.getEmail())) {


            log.warn("Internal employee creation failed. Email already exists.");
            throw new ResourceAlreadyExistsException("Email already exists.");

        }

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {

            log.warn("Internal employee creation failed. Employee code already exists.");
            throw new ResourceAlreadyExistsException("Employee code already exists.");

        }

        if (employeeRepository.existsByAuthUserId(request.getAuthUserId())) {

            log.warn(
                    "Internal employee creation failed. " +
                            "Auth user ID already exists. authUserId={}",
                    request.getAuthUserId()
            );
            throw new ResourceAlreadyExistsException("Employee AuthUserId already exists.");
        }



        Employee employee = new Employee();

        employee.setAuthUserId(request.getAuthUserId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setDesignation(request.getDesignation());
        employee.setDepartment(request.getEmployeeDepartment());

        Employee savedEmployee =  employeeRepository.save(employee);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(savedEmployee.getId());
        response.setEmployeeCode(savedEmployee.getEmployeeCode());
        response.setFirstName(savedEmployee.getFirstName());
        response.setLastName(savedEmployee.getLastName());
        response.setEmail(savedEmployee.getEmail());
        response.setPhone(savedEmployee.getPhone());
        response.setDepartment(savedEmployee.getDepartment());
        response.setExperience(savedEmployee.getExperience());
        response.setQualification(savedEmployee.getQualification());
        response.setHireDate(savedEmployee.getHireDate());
        response.setStatus(savedEmployee.getStatus());

        log.info(
                "Employee created successfully through internal service request. " +
                        "employeeId={}, employeeCode={}, authUserId={}",
                savedEmployee.getId(),
                savedEmployee.getEmployeeCode(),
                savedEmployee.getAuthUserId()
        );

        return response;
    }

}
