package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Antrianemail;
import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.repository.AntrianemailRepository;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AntrianemailRepository antrianemailRepository;

    public void sendRawEmail(String email, String subject, String content) {
        // send email
        Antrianemail antrianemail = new Antrianemail();
        antrianemail.setEmail(email);
        antrianemail.setSubject(subject);
        antrianemail.setContent(content);
        antrianemailRepository.save(antrianemail);
    }

    public void sendEmail(String employeeCode, String subject, String content) {
        // get employee email
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) {
            return;
        }
        sendRawEmail(employee.getEmail(), subject, content);
    }

    public void sendEmail(Employee employee, String subject, String content) {
        sendRawEmail(employee.getEmail(), subject, content);
    }
}
