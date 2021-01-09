package org.acme.personaldata.DTO;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.personaldata.Entity.ResultEntity;
import org.acme.personaldata.Enum.ResultValuesEnum;
import org.acme.personaldata.Enum.TestTypeEnum;

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
