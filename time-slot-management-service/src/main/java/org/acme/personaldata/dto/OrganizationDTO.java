package org.acme.personaldata.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.personaldata.entity.Organization;

import javax.validation.constraints.NotNull;

@RegisterForReflection
public class OrganizationDTO {
    @NotNull
    public String organizationName;

    public Organization createEntity()
    {
        Organization organization = new Organization();
        updateEntity(organization);
        return organization;
    }

    public void updateEntity(Organization organization)
    {
        organization.organizationName = organizationName;
    }
}
