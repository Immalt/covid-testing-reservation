package org.acme.personaldata;

import javax.validation.constraints.NotNull;

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
