package org.acme.timeslot.endpoint;

import org.acme.timeslot.dto.WorkingDayDTO;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.WorkingDay;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/organization/{organizationId : \\d+}/working-day")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Traced
public class WorkingDayResource {
    @POST
    public WorkingDay createWorkingDay(@PathParam("organizationId") Long organizationId, WorkingDayDTO applicationDTO)
    {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) return null;

        WorkingDay reservation = applicationDTO.createEntity(organization);
        reservation.persistAndFlush();
        return reservation;
    }

    @GET
    public List<WorkingDay> getWorkingDays(
            @DefaultValue ("0") @QueryParam("page") Integer page,
            @DefaultValue ("25") @QueryParam("perPage") Integer perPage,
            @PathParam("organizationId") Long organizationId)
    {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) return null;

        return WorkingDay.find("organization_id", organization)
                .page(page, perPage)
                .list();
    }

    @PUT
    @Path("{workingDayId : \\d+}")
    public WorkingDay updateWorkingDay(
            @PathParam("organizationId") Long organizationId,
            @PathParam("workingDayId") Long workingDayId,
            @Valid WorkingDayDTO dto
    ) {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) throw new NotFoundException();

        WorkingDay reservation = WorkingDay.findById(workingDayId);
        if (null == reservation || !reservation.organization.equals(organization))
            return null;

        dto.updateEntity(reservation);
        reservation.persistAndFlush();
        return reservation;
    }

    @GET
    @Path("{workingDayId : \\d+}")
    public WorkingDay getApplication(
            @PathParam("organizationId") Long organizationId,
            @PathParam("workingDayId") Long workingDayId
    ) {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) throw new NotFoundException();

        WorkingDay reservation = WorkingDay.findById(workingDayId);
        if (null == reservation || !reservation.organization.equals(organization))
            return null;

        return reservation;
    }

    @DELETE
    @Path("{workingDayId : \\d+}")
    public Boolean removeApplication(
            @PathParam("organizationId") Long organizationId,
            @PathParam("workingDayId") Long workingDayId
    ) {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) throw new NotFoundException();

        WorkingDay reservation = WorkingDay.findById(workingDayId);
        if (null == reservation || !reservation.organization.equals(organization))
            return false;

        return WorkingDay.deleteById(workingDayId);
    }
}
