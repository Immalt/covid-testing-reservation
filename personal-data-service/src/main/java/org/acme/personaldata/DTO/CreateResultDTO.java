package org.acme.personaldata.DTO;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.personaldata.Entity.ApplicationEntity;
import org.acme.personaldata.Entity.ResultEntity;
import org.acme.personaldata.Enum.ResultValuesEnum;
import org.acme.personaldata.Enum.TestTypeEnum;

import javax.validation.constraints.NotNull;

@RegisterForReflection
public class CreateResultDTO {
    @NotNull
    public ResultValuesEnum result;
    @NotNull
    public TestTypeEnum testType;

    public ResultEntity createEntity(ApplicationEntity applicationEntity)
    {
        ResultEntity entity = new ResultEntity();
        entity.application = applicationEntity;
        entity.testType = testType;
        entity.result = result;

        applicationEntity.results.add(entity);
        return entity;
    }
}
