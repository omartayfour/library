package com.vodafone.library.services;

import com.vodafone.library.config.SecurityConfig;
import com.vodafone.library.model.Customer;
import com.vodafone.library.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SecurityConfig securityConfig;


    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    public ResponseEntity<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Customer> createCustomer(Customer customer) {
        String encryptedPassword = SecurityConfig.encodePassword(customer.getPassword());
        customer.setPassword(encryptedPassword);
        Customer savedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    public ResponseEntity<Customer> updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(customerDetails.getName());
                    customer.setEmail(customerDetails.getEmail());
                    customer.setAddress(customerDetails.getAddress());
                    customer.setPhoneNumber(customerDetails.getPhoneNumber());
                    customer.setPassword(customerDetails.getPassword());
                    Customer updatedCustomer = customerRepository.save(customer);
                    return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Void> deleteCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
