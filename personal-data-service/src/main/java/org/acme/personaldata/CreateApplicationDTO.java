package org.acme.personaldata;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateApplicationDTO {
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @NotNull
    public String idCardNumber;
    @NotNull
    public String medicalInsurance;

    public List<CreateResultDTO> results = new ArrayList<>();

    public ApplicationEntity createEntity()
    {
        ApplicationEntity entity = new ApplicationEntity();
        entity.firstName = firstName;
        entity.lastName = lastName;
        entity.idCardNumber = idCardNumber;
        entity.medicalInsurance = medicalInsurance;
        entity.results = results.stream()
                .map(resultDTO -> resultDTO.createEntity(entity))
                .collect(Collectors.toList());

        return entity;
    }
}
