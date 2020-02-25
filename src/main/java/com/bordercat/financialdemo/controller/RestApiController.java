package com.bordercat.financialdemo.controller;

import com.bordercat.financialdemo.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * Created by Caglar Sekmen
 */

@Api(value="REST API to consume the endpoints")
@RestController
@RequestMapping("/v1/api")
@Validated
public class RestApiController {

    private final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    //Externalize Configuration
    @Value("${financial.api.apihost}")
    private String apiHost ;

    @Value("${financial.api.login}")
    private String loginUri;

    @Value("${financial.api.report}")
    private String reportUri;

    @Value("${financial.api.list}")
    private String transactionListUri;

    @Value("${financial.api.transaction}")
    private String transactionUri;

    @Value("${financial.api.client}")
    private String clientUri;

    @Autowired
    private RestTemplate restTemplate;

    public RestApiController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private String token;

    /**
     * Consumes /merchant/login
     * Extracts token from the response body
     * @param userDto for request body
     * @return ResponseEntity in Json
     */

    @ApiOperation(value = "User Login", response = ResponseEntity.class)
    @PostMapping(value="/user/login", produces = "application/json")
    public ResponseEntity login(@Valid @RequestBody UserDto userDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        //wrapping the UserDto object and the header for the entity.
        HttpEntity<UserDto> entity = new HttpEntity<UserDto>(userDto,headers);
        String uri = apiHost+loginUri;
        ResponseEntity responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        logger.info( "Response Entity: ", responseEntity);
        //extracting token from the response body
        String body = responseEntity.getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        token = root.get("token").textValue();

        return responseEntity;
    }

    /**
     * Setting the Authorization Header with the token
     * @param token
     * @return HttpHeaders with the Authorization header
     */
    public HttpHeaders setHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        logger.info("Header is set");
        return headers;
    }

    /**
     * Consumes /transactions/report
     * Token is merged from login()
     * @param reportDto for request body
     * @return ResponseEntity in Json
     */
    @PostMapping(value="/transactions/report", produces = "application/json")
    public ResponseEntity getReport(@Valid @RequestBody ReportDto reportDto){

        HttpHeaders headers = setHeader(token);
        String uri = apiHost+reportUri;
        HttpEntity<ReportDto> request = new HttpEntity<>(reportDto, headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(uri, request, String.class);
        return responseEntity;
    }

    /**
     * Consumes transaction/list
     * @param transactionListDto for request body
     * @return ResponseEntity in Json
     */
    @PostMapping(value="/transaction/list", produces = "application/json")
    public ResponseEntity getTransactionList(@Valid @RequestBody TransactionListDto transactionListDto){

        HttpHeaders headers = setHeader(token);
        String uri = apiHost+transactionListUri;
        HttpEntity<TransactionListDto> request = new HttpEntity<>(transactionListDto, headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(uri, request, String.class);
        return responseEntity;
    }


    /**
     * Consumes /transaction
     * @param transactionDto
     * @return ResponseEntity
     */
    @PostMapping(value="/transaction", produces = "application/json")
    public ResponseEntity getTransaction(@Valid @RequestBody TransactionDto transactionDto) {

        HttpHeaders headers = setHeader(token);
        String uri = apiHost+transactionUri;
        HttpEntity<TransactionDto> request = new HttpEntity<>(transactionDto, headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(uri, request, String.class);
        return responseEntity;
    }

    /**
     * Consumes /client
     * @param clientDto
     * @return ResponseEntity
     */
    @PostMapping(value="/client", produces = "application/json")
    public ResponseEntity getClient(@Valid @RequestBody ClientDto clientDto) {

        HttpHeaders headers = setHeader(token);
        String uri = apiHost+transactionUri;
        HttpEntity<ClientDto> request = new HttpEntity<>(clientDto, headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(uri, request, String.class);
        return responseEntity;
    }
}
