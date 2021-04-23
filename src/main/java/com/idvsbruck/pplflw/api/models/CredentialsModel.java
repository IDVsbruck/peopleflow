package com.idvsbruck.pplflw.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.idvsbruck.pplflw.api.config.annotations.EmailPattern;
import com.idvsbruck.pplflw.api.models.CredentialsModel.JsonKeys;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = { JsonKeys.CUSTOMER_EMAIL, JsonKeys.CUSTOMER_PASSWORD })
public class CredentialsModel {

    @EmailPattern
    @JsonProperty(value = JsonKeys.CUSTOMER_EMAIL, required = true)
    private String email;

    @NotBlank(message = "{password.blank}")
    @Size(max = 64, message = "{password.oversize}")
    @JsonProperty(value = JsonKeys.CUSTOMER_PASSWORD, required = true)
    private String password;

    public static class JsonKeys {

        public static final String CUSTOMER_EMAIL = "email";
        public static final String CUSTOMER_PASSWORD = "password";
    }
}
