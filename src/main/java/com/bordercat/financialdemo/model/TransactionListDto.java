package com.bordercat.financialdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Caglar Sekmen
 */

@Data //for getters/setters
@Builder //for Unit testing
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionListDto {

    private String fromDate;
    private String toDate;
    private StatusType status;
    private String operation;
    private Integer merchantId;
    private Integer acquireId;
    private String paymentMethod;
    private String errorCode;
    private String filterField;
    private String filterValue;
    private Integer page;

}
