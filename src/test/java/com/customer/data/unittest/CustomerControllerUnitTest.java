package com.customer.data.unittest;

import com.customer.data.controller.CustomerController;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CreateCustomerRequest;
import com.customer.data.request.UpdateCustomerRequest;
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
public class CustomerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void getAllCustomerControllerTest() throws Exception {

        AddressResponse addressResponse = new AddressResponse();
        CustomerResponse response = CustomerResponse.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com")
                .age(27).currentLivingAddress(addressResponse).build();

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
        CreateCustomerRequest request = new CreateCustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse response = CustomerResponse.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com")
                .age(27).currentLivingAddress(addressResponse).build();

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
        UpdateCustomerRequest request = new UpdateCustomerRequest("gabi@yahoo.com", addressRequest);
        CustomerResponse response = CustomerResponse.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com")
                .age(27).currentLivingAddress(addressResponse).build();

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
        CreateCustomerRequest request = new CreateCustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse response = CustomerResponse.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com")
                .age(27).currentLivingAddress(addressResponse).build();

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
        CreateCustomerRequest request = new CreateCustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
        CustomerResponse response = CustomerResponse.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com")
                .age(27).currentLivingAddress(addressResponse).build();
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
    void addCustomerWithEmptyFirstNameTest() throws Exception {

        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CreateCustomerRequest request = new CreateCustomerRequest("", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

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

    @Test
    void addCustomerWithEmptyEmailAndAddressTest() throws Exception {

        String requestDate = "1997-01-02";
        CreateCustomerRequest request = new CreateCustomerRequest("Gabi", "Abrudan", "", requestDate, null);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("errorMessage", "Customer email and living address are empty!");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void addCustomerWithWrongDateFormatTest() throws Exception {

        String requestDate = "1997/01/02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CreateCustomerRequest request = new CreateCustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("errorMessage", "Birth date must be in yyyy-MM-dd format");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void updateCustomerWithEmptyEmailAndAddressTest() throws Exception {

        UpdateCustomerRequest request = new UpdateCustomerRequest("", null);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/customers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("errorMessage", "Customer email and living address are empty!");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void updateCustomerWithWrongFieldsAddressTest() throws Exception {

        AddressRequest addressRequest = new AddressRequest("Rom", "", "Musatini", "5", "440077");
        UpdateCustomerRequest request = new UpdateCustomerRequest("", addressRequest);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/customers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("currentLivingAddress.city", "City cannot be empty or null");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void getCustomerWithIdBelow1Test() throws Exception {

        AddressRequest addressRequest = new AddressRequest("Rom", "", "Musatini", "5", "440077");
        UpdateCustomerRequest request = new UpdateCustomerRequest("", addressRequest);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/-1")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("errorMessage", "400 BAD_REQUEST \"Validation failure\"");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void searchCustomerWithSpaceInNameTest() throws Exception {

        AddressRequest addressRequest = new AddressRequest("Rom", "", "Musatini", "5", "440077");
        UpdateCustomerRequest request = new UpdateCustomerRequest("", addressRequest);
        String name = " ";

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/name/" + name)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andReturn();

        Map<String, String> exceptionMessage = new HashMap<>();
        exceptionMessage.put("errorMessage", "400 BAD_REQUEST \"Validation failure\"");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(exceptionMessage);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

}
