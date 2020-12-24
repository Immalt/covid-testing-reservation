package org.acme.personaldata;

import io.quarkus.runtime.annotations.RegisterForReflection;

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
