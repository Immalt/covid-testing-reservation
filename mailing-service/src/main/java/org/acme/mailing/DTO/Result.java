package org.acme.mailing.DTO;

import io.smallrye.common.constraint.NotNull;
import org.acme.mailing.Enum.TestResult;

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
