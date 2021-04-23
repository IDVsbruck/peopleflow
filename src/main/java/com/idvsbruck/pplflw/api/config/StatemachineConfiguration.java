package com.idvsbruck.pplflw.api.config;

import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import com.idvsbruck.pplflw.api.statemachine.EmployeeStatemachineListener;
import com.idvsbruck.pplflw.api.statemachine.actions.ActivateAction;
import com.idvsbruck.pplflw.api.statemachine.actions.ApproveAction;
import com.idvsbruck.pplflw.api.statemachine.actions.CheckAction;
import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory
public class StatemachineConfiguration extends EnumStateMachineConfigurerAdapter<EmployeeState, EmployeeEvent> {

    private final CheckAction checkAction;
    private final ApproveAction approveAction;
    private final ActivateAction activateAction;

    @Override
    public void configure(final StateMachineConfigurationConfigurer<EmployeeState, EmployeeEvent> config)
            throws Exception {
        config.withConfiguration().autoStartup(true).listener(new EmployeeStatemachineListener());
    }

    @Override
    public void configure(final StateMachineStateConfigurer<EmployeeState, EmployeeEvent> states) throws Exception {
        states.withStates().initial(EmployeeState.ADDED).end(EmployeeState.ACTIVE)
                .states(EnumSet.allOf(EmployeeState.class));
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<EmployeeState, EmployeeEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                    .source(EmployeeState.ADDED).target(EmployeeState.IN_CHECK)
                    .event(EmployeeEvent.CHECK).action(checkAction)
                .and().withExternal()
                    .source(EmployeeState.IN_CHECK).target(EmployeeState.APPROVED)
                    .event(EmployeeEvent.APPROVE).action(approveAction)
                .and().withExternal()
                    .source(EmployeeState.APPROVED).target(EmployeeState.ACTIVE)
                    .event(EmployeeEvent.ACTIVATE).action(activateAction);
        //TODO: can be added error handling - definition error action(s)
    }
}
