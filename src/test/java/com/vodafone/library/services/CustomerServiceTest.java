package com.vodafone.library.services;

import com.vodafone.library.model.Customer;
import com.vodafone.library.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCustomers() {
        // Mock data
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null));
        customers.add(new Customer(2L, "Omar Mohamed", "omarmohamed@example.com", "01198765432", "password2", "Cairo", null));
        when(customerRepository.findAll()).thenReturn(customers);

        // Call service method
        ResponseEntity<List<Customer>> responseEntity = customerService.getAllCustomers();

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customers, responseEntity.getBody());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testGetCustomerByIdExisting() {
        // Mock data
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Call service method
        ResponseEntity<Customer> responseEntity = customerService.getCustomerById(1L);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customer, responseEntity.getBody());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCustomerByIdNonExisting() {
        // Mock data
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Call service method
        ResponseEntity<Customer> responseEntity = customerService.getCustomerById(1L);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateCustomer() {
        // Mock data
        Customer customerToSave = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        when(customerRepository.save(any(Customer.class))).thenReturn(customerToSave);

        // Call service method
        ResponseEntity<Customer> responseEntity = customerService.createCustomer(customerToSave);

        // Verify
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(customerToSave, responseEntity.getBody());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testUpdateCustomerExisting() {
        // Mock data
        Customer existingCustomer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Customer updatedCustomerDetails = new Customer(1L, "Updated Name", "updated@example.com", "0123456789", "updatedPassword", "Updated Address", null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomerDetails);

        // Call service method
        ResponseEntity<Customer> responseEntity = customerService.updateCustomer(1L, updatedCustomerDetails);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCustomerDetails, responseEntity.getBody());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testUpdateCustomerNonExisting() {
        // Mock data
        Customer updatedCustomerDetails = new Customer(1L, "Updated Name", "updated@example.com", "0123456789", "updatedPassword", "Updated Address", null);
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Call service method
        ResponseEntity<Customer> responseEntity = customerService.updateCustomer(1L, updatedCustomerDetails);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testDeleteCustomerExisting() {
        // Mock data
        Customer existingCustomer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));

        // Call service method
        ResponseEntity<Void> responseEntity = customerService.deleteCustomer(1L);

        // Verify
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCustomerNonExisting() {
        // Mock data
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Call service method
        ResponseEntity<Void> responseEntity = customerService.deleteCustomer(1L);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).deleteById(any(Long.class));
    }
}
