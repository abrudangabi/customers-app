package com.customer.data.unittest;

import com.customer.data.controller.CustomerController;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
import com.customer.data.request.CustomerUpdateRequest;
import com.customer.data.response.AddressResponse;
import com.customer.data.response.CustomerResponse;
import com.customer.data.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void getAllCustomerControllerTest() throws Exception {

        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest alex2 = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);;
        CustomerResponse alex = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        List<CustomerResponse> allEmployees = Arrays.asList(alex);

        given(customerService.getAll()).willReturn(allEmployees);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        List<CustomerResponse> expectedResponseBody = Arrays.asList(alex);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));

    }

    @Test
    void addCustomerTest() throws Exception {

        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest alex2 = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse alex = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        List<CustomerResponse> allEmployees = Arrays.asList(alex);

        given(customerService.addCustomer(any())).willReturn(alex);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(alex2)))
                .andExpect(status().isCreated())
                .andReturn();

        CustomerResponse expectedResponseBody = alex;
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));

    }

    @Test
    void updateCustomerTest() throws Exception {

        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerUpdateRequest alex2 = new CustomerUpdateRequest("gabi@yahoo.com", addressRequest);
        CustomerResponse alex = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        List<CustomerResponse> allEmployees = Arrays.asList(alex);

        given(customerService.updateCustomer(any(), any(Long.class))).willReturn(alex);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/customers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(alex2)))
                .andExpect(status().isOk())
                .andReturn();

        CustomerResponse expectedResponseBody = alex;
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));

    }

    @Test
    void getByCustomerIdTest() throws Exception {

        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest alex2 = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse alex = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        List<CustomerResponse> allEmployees = Arrays.asList(alex);

        given(customerService.getCustomerById(any(Long.class))).willReturn(alex);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(alex2)))
                .andExpect(status().isOk())
                .andReturn();

        CustomerResponse expectedResponseBody = alex;
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));

    }

    @Test
    void getByCustomerFirstNameOrLastNameTest() throws Exception {

        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest alex2 = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse alex = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);
        String name = "gabi";

        List<CustomerResponse> allEmployees = Arrays.asList(alex);

        given(customerService.getCustomerByName(any())).willReturn(allEmployees);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/name/" + name)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(alex2)))
                .andExpect(status().isOk())
                .andReturn();

        List<CustomerResponse> expectedResponseBody = Arrays.asList(alex);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));

    }

    @Test
    void addCustomerWithErrorTest() throws Exception {

        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest alex2 = new CustomerRequest("", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse alex = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);
        String name = "gabi";

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(alex2)))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("firstName", "First name cannot be empty or null");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

}
