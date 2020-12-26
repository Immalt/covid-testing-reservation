package org.acme.timeslot.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.WorkingDay;
import org.acme.timeslot.enums.TimeUnit;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@RegisterForReflection
public class WorkingDayDTO {
    @NotNull
    public Integer testInterval;
    @NotNull
    public TimeUnit timeUnit;
    @NotNull
    public DayOfWeek day;
    @NotNull
    public LocalTime workingFrom;
    @NotNull
    public LocalTime workingTill;

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
        workingDay.timeUnit = timeUnit;
        workingDay.day = day;
        workingDay.workingFrom = workingFrom;
        workingDay.workingTill = workingTill;
    }
}
