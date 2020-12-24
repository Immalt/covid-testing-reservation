package org.acme.personaldata;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.NotNull;

@RegisterForReflection
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
