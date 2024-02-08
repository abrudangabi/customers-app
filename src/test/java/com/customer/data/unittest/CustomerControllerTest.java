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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void getAllCustomerControllerTest() throws Exception {

        AddressResponse addressResponse = new AddressResponse();
        CustomerResponse response = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        List<CustomerResponse> allEmployees = List.of(response);

        given(customerService.getAll()).willReturn(allEmployees);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        List<CustomerResponse> expectedResponseBody = List.of(response);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));

    }

    @Test
    void addCustomerTest() throws Exception {

        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest request = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse response = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        given(customerService.addCustomer(any())).willReturn(response);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(response));

    }

    @Test
    void updateCustomerTest() throws Exception {

        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerUpdateRequest request = new CustomerUpdateRequest("gabi@yahoo.com", addressRequest);
        CustomerResponse response = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        given(customerService.updateCustomer(any(), any(Long.class))).willReturn(response);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/customers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(response));

    }

    @Test
    void getByCustomerIdTest() throws Exception {

        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest request = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse response = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);

        given(customerService.getCustomerById(any(Long.class))).willReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(response));

    }

    @Test
    void getByCustomerFirstNameOrLastNameTest() throws Exception {

        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        AddressResponse addressResponse = new AddressResponse();
        CustomerRequest request = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse response = new CustomerResponse(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressResponse);
        String name = "gabi";

        List<CustomerResponse> allEmployees = List.of(response);

        given(customerService.getCustomerByName(any())).willReturn(allEmployees);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/name/" + name)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        List<CustomerResponse> expectedResponseBody = List.of(response);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));

    }

    @Test
    void addCustomerWithErrorTest() throws Exception {

        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CustomerRequest request = new CustomerRequest("", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("firstName", "First name cannot be empty or null");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

}
