package org.acme.personaldata.Message;

import org.acme.personaldata.Enum.ResultValuesEnum;
import org.acme.personaldata.Enum.TestTypeEnum;

import java.time.LocalDateTime;

public class ResultMessage {
    public String firstName;
    public String lastName;
    public String email;
    public TestTypeEnum testType;
    public ResultValuesEnum testResult;
    public LocalDateTime term;
}
