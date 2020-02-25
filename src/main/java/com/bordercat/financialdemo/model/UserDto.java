package com.bordercat.financialdemo.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Caglar Sekmen
 */

@ApiModel(description = "User credentials") //For Swagger
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {

    @NotEmpty(message = "email can not be empty")
    @Email
    private String email;

    @NotEmpty(message = "password can not be empty")
    private String password;
}
