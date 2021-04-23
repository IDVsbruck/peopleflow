package com.idvsbruck.pplflw.api.config.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Documented
@SuppressWarnings("unused")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Email(message = "{email.invalid}")
@NotBlank(message = "{email.blank}")
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Size(max = 128, message = "{email.oversize}")
public @interface EmailPattern {

    String message() default "Invalid, empty or long email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
