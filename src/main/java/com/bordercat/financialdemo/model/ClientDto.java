package com.bordercat.financialdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Created by Caglar Sekmen
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    @NotEmpty(message = "transactionId can not be empty")
    private String transactionId;
}
