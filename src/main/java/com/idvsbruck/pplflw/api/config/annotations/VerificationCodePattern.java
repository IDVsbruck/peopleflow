package com.idvsbruck.pplflw.api.config.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Documented
@SuppressWarnings("unused")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "{code.blank}")
@Size(max = 36, message = "{code.oversize}")
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface VerificationCodePattern {

    String message() default "Empty or long verification code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
