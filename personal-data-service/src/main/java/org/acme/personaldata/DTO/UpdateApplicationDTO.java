package org.acme.personaldata.DTO;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.personaldata.Entity.ApplicationEntity;

import javax.validation.constraints.NotNull;

@RegisterForReflection
public class UpdateApplicationDTO {
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @NotNull
    public String idCardNumber;
    @NotNull
    public String medicalInsurance;

    public void updateEntity(ApplicationEntity entity)
    {
        entity.firstName = firstName;
        entity.lastName = lastName;
        entity.idCardNumber = idCardNumber;
        entity.medicalInsurance = medicalInsurance;
    }
}
