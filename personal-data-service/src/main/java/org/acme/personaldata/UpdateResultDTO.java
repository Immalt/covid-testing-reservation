package org.acme.personaldata;

import javax.validation.constraints.NotNull;

public class UpdateResultDTO {
    @NotNull
    public ResultValuesEnum result;
    @NotNull
    public TestTypeEnum testType;

    public void updateEntity(ResultEntity entity)
    {
        entity.testType = testType;
        entity.result = result;
    }
}
