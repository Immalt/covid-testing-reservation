package org.acme.timeslot.endpoint;

import io.smallrye.common.constraint.NotNull;
import org.acme.timeslot.dto.ReservationDTO;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.Reservation;
import org.acme.timeslot.exception.CannotCreateReservation;
import org.acme.timeslot.exception.CannotListReservation;
import org.acme.timeslot.service.ReservationEvent;
import org.acme.timeslot.service.ReservationService;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;


@Path("/organization/{organizationId : \\d+}/reservation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Traced
public class OrganizationReservationResource {
    @Inject
    ReservationService reservationService;

    @Inject
    ReservationEvent reservationEvent;

    @POST
    public Reservation createReservation(@PathParam("organizationId") Long organizationId, ReservationDTO applicationDTO) throws CannotCreateReservation {
        Organization organization = Organization.findById(organizationId);
        if (organization == null) return null;

        Reservation reservation = applicationDTO.createEntity(organization);
        reservationService.validateTerm(reservation);
        reservation.persistAndFlush();

        reservationEvent.creationMessage(reservation);
        return reservation;
    }

    @GET
    @Path("/list-free")
    public List<Reservation> listFreeSlots(
            @NotNull @PathParam("organizationId") Long organizationId,
            @QueryParam("fromTimestamp") Long from,
            @QueryParam("tillTimestamp") Long till
    ) throws CannotListReservation {

        Organization organization = Organization.findById(organizationId);
        if (organization == null) return null;

        if (from != null && till != null) {
            return reservationService.getFreeTermsForOrganization(organization, new Timestamp(from).toLocalDateTime().toLocalDate(), new Timestamp(till).toLocalDateTime().toLocalDate());
        } else if (from == null && till == null) {
            return reservationService.getFreeTermsForOrganization(organization, LocalDate.now(), LocalDate.now());
        }

        return null;
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


        boolean isDeleted = Reservation.deleteById(reservationId);
        reservationEvent.deletionMessage(reservation);
        return isDeleted;
    }
}
