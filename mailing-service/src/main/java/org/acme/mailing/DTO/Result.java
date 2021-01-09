package org.acme.mailing.DTO;

import org.acme.mailing.Enum.TestResult;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Result extends EmailData {
    @NotNull
    public String firstName;

    @NotNull
    public String lastName;

    @NotNull
    public String testType;

    @NotNull
    public TestResult testResult;


    @NotNull
    public LocalDateTime term;
}
