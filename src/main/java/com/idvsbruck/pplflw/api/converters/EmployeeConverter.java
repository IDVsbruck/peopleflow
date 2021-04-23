package com.idvsbruck.pplflw.api.converters;

import com.idvsbruck.pplflw.api.dto.Employee;
import com.idvsbruck.pplflw.api.models.EmployeeModel;
import javax.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter implements Converter<EmployeeModel, Employee> {

    @Override
    public @NotNull Employee convert(final EmployeeModel employeeModel) {
        var employee = new Employee();
        BeanUtils.copyProperties(employeeModel, employee);
        return employee;
    }
}
