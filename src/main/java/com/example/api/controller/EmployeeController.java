package com.example.api.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping("/employees")
    public String createNewEmployee(@RequestBody Employee employee) {
        System.out.println("Received Employee: " + employee);
        employeeRepository.save(employee);
        return "Employee created in the database";
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> empList = new ArrayList<>();
        employeeRepository.findAll().forEach(empList::add);
        return new ResponseEntity<List<Employee>>(empList, HttpStatus.OK);
    }

    @GetMapping("/employees/{empid}")
    public ResponseEntity<Employee> getEmployeeId(@PathVariable long empid) {
        employeeRepository.findById(empid);
        Optional<Employee> emp = employeeRepository.findById(empid);
        if (emp.isPresent()) {
            return new ResponseEntity<Employee>(emp.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/employees/{empid}")
    public ResponseEntity<String> updateEmployeeById(@PathVariable long empid, @RequestBody Employee employee) {
        Optional<Employee> emp = employeeRepository.findById(empid);

        if (emp.isPresent()) {
            Employee existEmp = emp.get();
            existEmp.setEmp_age(employee.getEmp_age());
            existEmp.setEmp_city(employee.getEmp_city());
            existEmp.setEmp_name(employee.getEmp_name());
            existEmp.setEmp_salary(employee.getEmp_salary());
            employeeRepository.save(existEmp);
            return ResponseEntity.ok("Employee Details against Id " + empid + " updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Details do not exist for empid " + empid);
        }
    }


    @DeleteMapping("/employees/{empid}")
    public String deleteEmployeeByEmpId(@PathVariable Long empid) {
        employeeRepository.deleteById(empid);
        return "Employee Deleted Sucessfully";
    }

    @DeleteMapping("employees")
    public String deleteAllEmployee()
    {
        employeeRepository.deleteAll();
        return "Employee deleted Successfully...";
    }
}
