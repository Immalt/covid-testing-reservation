package org.acme.timeslot.endpoint;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.acme.timeslot.dto.OrganizationDTO;
import org.acme.timeslot.entity.Organization;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/organization")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Traced
public class OrganizationResource {
    @GET
    public List<Organization> getOrganizations(
            @DefaultValue ("0") @QueryParam("page") Integer page,
            @DefaultValue ("25") @QueryParam("perPage") Integer perPage
    ) {
        return Organization.findAll().page(page, perPage).list();
    }

    @POST
    public Organization createOrganization(OrganizationDTO applicationDTO)
    {
        Organization organization = applicationDTO.createEntity();
        organization.persistAndFlush();
        return organization;
    }

    @PUT
    @Path("{organizationId : \\d+}")
    public Organization updateOrganization(@PathParam("organizationId") Long organizationId, @Valid OrganizationDTO dto)
    {
        Organization organization = Organization.findById(organizationId);
        if (null == organization)
            return null;

        dto.updateEntity(organization);
        organization.persistAndFlush();
        return organization;
    }


    @GET
    @Path("{organizationId : \\d+}")
    public Organization getOrganization(@PathParam("organizationId") Long organizationId)
    {
        return Organization.findById(organizationId);
    }

    @DELETE
    @Path("{organizationId : \\d+}")
    public Boolean removeOrganization(@PathParam("organizationId") Long organizationId)
    {

        Organization organization = Organization.findById(organizationId);
        boolean deleted = Organization.deleteById(organizationId);

        organization.flush();
        return deleted;
    }
}
