package org.acme.notification.DTO;

import org.acme.notification.Enum.TestResult;
import org.acme.notification.Enum.TestType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Result extends EmailData {
    @NotNull
    public String firstName;

    @NotNull
    public String lastName;

    @NotNull
    public TestType testType;

    @NotNull
    public TestResult testResult;


    @NotNull
    public LocalDateTime term;
}
