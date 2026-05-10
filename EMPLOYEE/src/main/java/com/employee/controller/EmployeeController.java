package com.employee.controller;

import com.employee.exception.MissingParameterException;
import com.employee.model.dto.EmployeeDto;
import com.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/save")
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto response = employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable Long id) {
        EmployeeDto response = employeeService.updateEmployee(id, employeeDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getSingleEmployee(@PathVariable Long id) {
        EmployeeDto employee = employeeService.getSingleEmployee(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/getEmpCodeCompanyName")
    public ResponseEntity<EmployeeDto> getEmployeeByEmpCodeAndCompanyName(@RequestParam(required = false) String empCode, @RequestParam(required = false) String companyName) {
        List<String> missingParameters = new ArrayList<>();

        if(empCode == null || empCode.trim().isEmpty())
        {
            missingParameters.add("empCode");
        }

        if(companyName == null || companyName.trim().isEmpty())
        {
            missingParameters.add("companyName");
        }

        if(!missingParameters.isEmpty()) {
            String finalMessage = String.join(", ", missingParameters);
            throw new MissingParameterException("Please provide " + finalMessage);
        }
        EmployeeDto employeeDto = employeeService.getEmployeeByEmpCodeAndCompanyName(empCode, companyName);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }
}
