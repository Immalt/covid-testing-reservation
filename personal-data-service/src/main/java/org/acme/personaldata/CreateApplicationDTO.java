package org.acme.personaldata;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RegisterForReflection
public class CreateApplicationDTO {
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @NotNull
    public String idCardNumber;
    @NotNull
    public String medicalInsurance;

    @NotNull
    @Email
    public String email;

    public List<CreateResultDTO> results = new ArrayList<>();

    public ApplicationEntity createEntity()
    {
        ApplicationEntity entity = new ApplicationEntity();
        entity.firstName = firstName;
        entity.lastName = lastName;
        entity.idCardNumber = idCardNumber;
        entity.medicalInsurance = medicalInsurance;
        entity.email = email;
        entity.results = results.stream()
                .map(resultDTO -> resultDTO.createEntity(entity))
                .collect(Collectors.toList());

        return entity;
    }
}
