package com.idvsbruck.pplflw.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.idvsbruck.pplflw.api.config.annotations.EmailPattern;
import com.idvsbruck.pplflw.api.config.annotations.VerificationCodePattern;
import com.idvsbruck.pplflw.api.models.ActivationModel.JsonKeys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = { JsonKeys.EMAIL, JsonKeys.CODE })
public class ActivationModel {

    @EmailPattern
    @JsonProperty(value = JsonKeys.EMAIL, required = true)
    private String email;

    @VerificationCodePattern
    @JsonProperty(value = JsonKeys.CODE, required = true)
    private String code;

    public static class JsonKeys {

        public static final String EMAIL = "email";
        public static final String CODE = "code";
    }
}
