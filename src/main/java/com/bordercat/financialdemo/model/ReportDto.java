package com.bordercat.financialdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * Created by Caglar Sekmen
 */

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {

    @NotEmpty(message = "fromDate can not be empty")
    private String fromDate;

    @NotEmpty(message = "toDate can not be empty")
    private String toDate;

    @Positive(message = "merchant can not be zero")
    private Integer merchant;

    @Positive(message = "acquirer can not be zero")
    private Integer acquirer;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getMerchant() {
        return merchant;
    }

    public void setMerchant(Integer merchant) {
        this.merchant = merchant;
    }

    public Integer getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(Integer acquirer) {
        this.acquirer = acquirer;
    }
}
