package com.employee.service.impl;

import com.employee.client.AddressClient;
import com.employee.exception.BadRequestException;
import com.employee.exception.ResourceNotFoundException;
import com.employee.model.dto.AddressDto;
import com.employee.model.dto.EmployeeDto;
import com.employee.model.entity.Employee;
import com.employee.repository.EmployeeRepository;
import com.employee.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final AddressClient addressClient;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, AddressClient addressClient) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.addressClient = addressClient;
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        if(employeeDto.getId() != null)
        {
            throw new RuntimeException("Employee already exists");
        }

        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee savedEntity = employeeRepository.save(entity);
        return modelMapper.map(savedEntity, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        if(employeeDto.getId() == null || id == null)
        {
            throw new BadRequestException("Employee id is required");
        }

        if(!Objects.equals(id, employeeDto.getId()))
        {
            throw new BadRequestException("Employee id mismatch");
        }

        employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee updatedEmployee = employeeRepository.save(entity);
        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDto getSingleEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        List<AddressDto> addressDtos = new ArrayList<>();
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        try {
            addressDtos = addressClient.getAddressByEmpId(id);
            employeeDto.setAddressDto(addressDtos);
        }
        catch (Exception e){
            log.error("Address not found with Employee id: {}", id);
        }

        return employeeDto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty())
        {
            throw new ResourceNotFoundException("No employees found");
        }
        List<EmployeeDto> employeeDtoList = employees.stream().map(employee -> modelMapper.map(employee, EmployeeDto.class)).toList();
        List<EmployeeDto> response = new ArrayList<>();
        for(EmployeeDto employeeDto : employeeDtoList)
        {
            List<AddressDto> addressDtos = new ArrayList<>();
            try {
                addressDtos = addressClient.getAddressByEmpId(employeeDto.getId());
                employeeDto.setAddressDto(addressDtos);
            }
            catch (Exception e){
                log.error("Address not found with Employee id: {}", employeeDto.getId());
            }

            response.add(employeeDto);
        }
        return response;
    }

    @Override
    public EmployeeDto getEmployeeByEmpCodeAndCompanyName(String empCode, String companyName) {
        Employee employee = employeeRepository.findByEmpCodeAndCompanyName(empCode, companyName).orElseThrow(() -> new ResourceNotFoundException("Employee not found with empCode: " + empCode + " and companyName: " + companyName));
        return modelMapper.map(employee, EmployeeDto.class);
    }

}
