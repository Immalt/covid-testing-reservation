package org.acme.personaldata.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.personaldata.entity.Organization;
import org.acme.personaldata.entity.WorkingDay;
import org.acme.personaldata.enums.Unit;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@RegisterForReflection
public class WorkingDayDTO {
    @NotNull
    public Integer testInterval;
    @NotNull
    public Unit unit;
    @NotNull
    public DayOfWeek day;
    @NotNull
    public LocalTime from;
    @NotNull
    public LocalTime till;

    public WorkingDay createEntity(Organization organization)
    {
        WorkingDay workingDay = new WorkingDay();
        workingDay.organization = organization;
        updateEntity(workingDay);
        return workingDay;
    }

    public void updateEntity(WorkingDay workingDay)
    {
        workingDay.testInterval = testInterval;
        workingDay.timeUnit = unit;
        workingDay.workingDay = day;
        workingDay.workingFrom = from;
        workingDay.workingTill = till;
    }
}
