package com.vodafone.library.controller;

import com.vodafone.library.model.Customer;
import com.vodafone.library.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Customer customer1 = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Customer customer2 = new Customer(2L, "Omar Mohamed", "omarmohamed@example.com", "01198765432", "password2", "Cairo", null);
        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(new ResponseEntity<>(customers, HttpStatus.OK));

        ResponseEntity<List<Customer>> result = customerService.getAllCustomers();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(customers, result.getBody());
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);

        when(customerService.getCustomerById(anyLong())).thenReturn(new ResponseEntity<>(customer, HttpStatus.OK));

        ResponseEntity<Customer> result = customerService.getCustomerById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(customer, result.getBody());
    }

    @Test
    void testCreateCustomer() throws Exception {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);

        when(customerService.createCustomer(any(Customer.class))).thenReturn(new ResponseEntity<>(customer, HttpStatus.CREATED));


        ResponseEntity<Customer> response = customerController.createCustomer(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);

        when(customerService.updateCustomer(anyLong(), any(Customer.class))).thenReturn(new ResponseEntity<>(customer, HttpStatus.OK));

        ResponseEntity<Customer> response = customerController.updateCustomer(1L, customer);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        when(customerService.deleteCustomer(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Void> response = customerController.deleteCustomer(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
