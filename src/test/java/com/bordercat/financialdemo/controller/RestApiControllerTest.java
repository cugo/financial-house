package com.bordercat.financialdemo.controller;

import com.bordercat.financialdemo.model.ReportDto;
import com.bordercat.financialdemo.model.TransactionDto;
import com.bordercat.financialdemo.model.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Created by Caglar Sekmen
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RestClientTest
public class RestApiControllerTest {

    @Autowired
    private RestTemplateBuilder builder;

    final String baseUrl = "https://sandbox-reporting.rpdpymnt.com/api/v3";

    //Token should be renewed for the tests
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtZXJjaGFudFVzZXJJZCI6NTMsInJvbGUiOi" +
            "J1c2VyIiwibWVyY2hhbnRJZCI6Mywic3ViTWVyY2hhbnRJZHMiOlszLDc0LDkzLDExOTEsMTI5NSwxMTEsMTM3LD" +
            "EzOCwxNDIsMTQ1LDE0NiwxNTMsMzM0LDE3NSwxODQsMjIwLDIyMSwyMjIsMjIzLDI5NCwzMjIsMzIzLDMyNywzMj" +
            "ksMzMwLDM0OSwzOTAsMzkxLDQ1NSw0NTYsNDc5LDQ4OCw1NjMsMTE0OSw1NzAsMTEzOCwxMTU2LDExNTcsMTE1OCw" +
            "xMTc5LDEyOTMsMTI5NCwxMzA2LDEzMDddLCJ0aW1lc3RhbXAiOjE1ODI2MzkxNTN9.H66dZo1Q4_nQflJm4Btbp0AmoPI0vS9t4dEVJJ_RquM";

    //private ClientExceptionHandler clientExceptionHandler; // todo for 500 errors.

    @Test
    @Order(1)
    void successfulLogin_withCorrectCredentials() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String loginUri = "/merchant/user/login";
        URI uri = new URI(baseUrl + loginUri);
        UserDto userDto = UserDto.builder()
                .email("demo@financialhouse.io")
                .password("cjaiU8CV")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<UserDto> entity = new HttpEntity<>(userDto,headers);
        ResponseEntity result = restTemplate.postForEntity(uri, entity, String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void loginFail_withEmptyCredentials() throws URISyntaxException {
        UserDto userDto = UserDto.builder()
                .email("")
                .password("")
                .build();
        Assertions.assertEquals("",userDto.getEmail());
        Assertions.assertEquals("",userDto.getPassword());
    }

    @Test
    void getReportWithHeaderSuccess() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String loginUri = "/transactions/report";
        URI uri = new URI(baseUrl + loginUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ReportDto reportDto = ReportDto.builder()
                .fromDate("2015-07-01")
                .toDate("2015-10-01")
                .merchant(1)
                .acquirer(1).build();
        HttpEntity<ReportDto> request = new HttpEntity<>(reportDto, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        //Verify request succeed
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void getReportWithHeader_FailWithZeroValues() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String loginUri = "/transactions/report";
        URI uri = new URI(baseUrl + loginUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ReportDto reportDto = ReportDto.builder()
                .fromDate("2015-07-01")
                .toDate("2015-10-01")
                .merchant(0)
                .acquirer(0).build();
        HttpEntity<ReportDto> request = new HttpEntity<>(reportDto, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        //Verify request failed
        Assertions.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    void getReportWithoutHeaderFail() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String loginUri = "/transactions/report";
        URI uri = new URI(baseUrl + loginUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", ""); //empty token

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ReportDto reportDto = ReportDto.builder()
                .fromDate("2015-07-01")
                .toDate("2015-10-01")
                .merchant(1)
                .acquirer(1).build();
        HttpEntity<ReportDto> request = new HttpEntity<>(reportDto, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        String body = result.getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String message = root.get("message").textValue();
        Assertions.assertEquals(200, result.getStatusCodeValue());
        Assertions.assertEquals("Token is required", message);

    }

    @Test
    void getReportWithWrongTokenFail() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setErrorHandler(clientExceptionHandler);
        //Need error handler for 500
        String loginUri = "/transactions/report";
        URI uri = new URI(baseUrl + loginUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "23423423434");

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ReportDto reportDto = ReportDto.builder()
                .fromDate("2015-07-01")
                .toDate("2015-10-01")
                .merchant(1)
                .acquirer(1).build();
        HttpEntity<ReportDto> request = new HttpEntity<>(reportDto, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        // Assertions.assertEquals(500,result.getStatusCodeValue());
    }

    @Test
    void getTransactionWithCorrectValue() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String loginUri = "/transaction";
        URI uri = new URI(baseUrl + loginUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionId("529-1438673740-2")
                .build();
        HttpEntity<TransactionDto> request = new HttpEntity<>(transactionDto, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        Assertions.assertEquals(200,result.getStatusCodeValue());
    }

    @Test
    void getTransactionWithEmptyValue() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String loginUri = "/transaction";
        URI uri = new URI(baseUrl + loginUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionId("")
                .build();
        HttpEntity<TransactionDto> request = new HttpEntity<>(transactionDto, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
        Assertions.assertEquals(500,result.getStatusCodeValue());
    }
}
