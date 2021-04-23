package com.idvsbruck.pplflw.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.idvsbruck.pplflw.api.dto.Employee.JsonKeys;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "employee")
@Entity(name = "Employee")
@JsonPropertyOrder(value = { JsonKeys.EMAIL, JsonKeys.NAME, JsonKeys.ACTIVE, JsonKeys.AGE })
public class Employee {

    @Id
    @JsonIgnore
    @Column(name = ColumnNames.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = ColumnNames.EMAIL)
    @JsonProperty(value = JsonKeys.EMAIL)
    private String email;

    @Column(name = ColumnNames.ACTIVE)
    @JsonProperty(value = JsonKeys.ACTIVE)
    private Boolean active = false;

    @Column(name = ColumnNames.NAME)
    @JsonProperty(value = JsonKeys.NAME)
    private String name;

    @Column(name = ColumnNames.AGE)
    @JsonProperty(value = JsonKeys.AGE)
    private String age;

    public Employee(String email) {
        this.email = email;
        this.active = false;
    }

    public static final class ColumnNames {

        public static final String ID = "id";
        public static final String EMAIL = "email";
        public static final String ACTIVE = "active";
        public static final String NAME = "name";
        public static final String AGE = "age";
    }

    public static final class JsonKeys {

        public static final String EMAIL = "email";
        public static final String ACTIVE = "active";
        public static final String NAME = "name";
        public static final String AGE = "age";
    }
}
