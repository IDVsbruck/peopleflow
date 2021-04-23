package com.idvsbruck.pplflw.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.idvsbruck.pplflw.api.models.EmployeeModel.JsonKeys;
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
@JsonPropertyOrder(value = { JsonKeys.NAME, JsonKeys.AGE })
public class EmployeeModel {

    @NotBlank(message = "{name.blank}")
    @Size(max = 128, message = "{name.oversize}")
    @JsonProperty(value = JsonKeys.NAME, required = true)
    private String name;

    @JsonProperty(value = JsonKeys.AGE)
    private String age;

    public static class JsonKeys {

        public static final String NAME = "name";
        public static final String AGE = "age";
    }
}
