package org.acme.personaldata.endpoint;

import org.acme.personaldata.dto.ReservationDTO;
import org.acme.personaldata.entity.Organization;
import org.acme.personaldata.entity.Reservation;
import org.acme.personaldata.exception.CannotListReservation;
import org.acme.personaldata.service.ReservationService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.List;


@Path("/organization/{organizationId : \\d+}/reservation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class ReservationResource {
    @Inject
    ReservationService reservationService;

    @POST
    public Reservation createReservation(@PathParam("organizationId") Long organizationId, ReservationDTO applicationDTO)
    {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) return null;

        Reservation reservation = applicationDTO.createEntity(organization);
        reservation.persistAndFlush();
        return reservation;
    }

    @GET
    @Path("/list-free")
    public List<Reservation> listFreeSlots(
            @PathParam("organizationId") Long organizationId,
            @QueryParam("fromTimestamp") Long from,
            @QueryParam("tillTimestamp") Long till
    ) throws CannotListReservation {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) return null;

        if (from != null && till != null) {
            return reservationService.getFreeTermsForOrganization(organization, new Timestamp(from).toLocalDateTime().toLocalDate(), new Timestamp(till).toLocalDateTime().toLocalDate());
        }

        return organization.reservations;
    }

    @GET
    public List<Reservation> getReservations(
            @PathParam("organizationId") Long organizationId,
            @DefaultValue ("0") @QueryParam("page") Integer page,
            @DefaultValue ("25") @QueryParam("perPage") Integer perPage
    ) {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) return null;

        return Reservation.find("organization_id", organization)
                .page(page, perPage)
                .list();
    }

    @PUT
    @Path("{reservationId : \\d+}")
    public Reservation updateReservation(
            @PathParam("organizationId") Long organizationId,
            @PathParam("reservationId") Long reservationId,
            @Valid ReservationDTO dto
    ) {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) throw new NotFoundException();

        Reservation reservation = Reservation.findById(reservationId);
        if (null == reservation || !reservation.organization.equals(organization))
            return null;

        dto.updateEntity(reservation);
        reservation.persistAndFlush();
        return reservation;
    }

    @GET
    @Path("{reservationId : \\d+}")
    public Reservation getReservation(
            @PathParam("organizationId") Long organizationId,
            @PathParam("reservationId") Long reservationId
    ) {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) throw new NotFoundException();

        Reservation reservation = Reservation.findById(reservationId);
        if (null == reservation || !reservation.organization.equals(organization))
            return null;

        return reservation;
    }

    @DELETE
    @Path("{reservationId : \\d+}")
    public Boolean removeReservation(
            @PathParam("organizationId") Long organizationId,
            @PathParam("reservationId") Long reservationId
    ) {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) throw new NotFoundException();

        Reservation reservation = Reservation.findById(reservationId);
        if (null == reservation || !reservation.organization.equals(organization))
            return false;

        return Reservation.deleteById(reservationId);
    }
}
