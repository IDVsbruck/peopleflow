package com.idvsbruck.pplflw.api.config;

import com.idvsbruck.pplflw.api.converters.EmployeeConverter;
import com.idvsbruck.pplflw.api.services.PplflwConversionService;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@RequiredArgsConstructor
public class PplflwConfiguration {

    public static final String VARIABLE_EMAIL = "EMAIL";

    private final EmployeeConverter employeeConverter;

    @Primary
    @Bean(name = "validatorBean")
    public LocalValidatorFactoryBean validatorBean() {
        var bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setCommonMessages(yamlProperties());
        return messageSource;
    }

    @Bean
    public Properties yamlProperties() {
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(new ClassPathResource("messages.yaml"));
        return bean.getObject();
    }

    @Bean
    public PplflwConversionService customConversionService() {
        var conversionService = new PplflwConversionService();
        ConversionServiceFactory.registerConverters(Set.of(employeeConverter), conversionService);
        return conversionService;
    }
}
