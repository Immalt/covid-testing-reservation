package org.acme.personaldata.service;

import org.acme.personaldata.entity.Organization;
import org.acme.personaldata.entity.Reservation;
import org.acme.personaldata.entity.WorkingDay;
import org.acme.personaldata.exception.CannotCreateReservation;
import org.acme.personaldata.exception.CannotListReservation;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.transaction.TransactionScoped;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@TransactionScoped
public class ReservationService {
    private final EntityManager em;

    public ReservationService(EntityManager em)
    {
        this.em = em;
    }

    public void validateTerm(Reservation reservation) throws CannotCreateReservation {
        try {
            isValidTime(reservation);
            isFreeTerm(reservation);
        } catch (NoResultException e) {
            throw new CannotCreateReservation(e);
        }
    }

    public WorkingDay getWorkingDayByDay(DayOfWeek day, List<WorkingDay> workingDays) throws NoResultException {
        for (WorkingDay workingDay: workingDays) {
            if (workingDay.workingDay.equals(day)) return workingDay;
        }

        throw new NoResultException();
    }

    public void isFreeTerm(Reservation reservation) throws CannotCreateReservation {
        if (null != Reservation.find("term", reservation.term)) throw new CannotCreateReservation();
    }

    public void isValidTime(Reservation reservation) throws CannotCreateReservation {
        if (!generateListReservation(reservation.organization, reservation.term).contains(reservation))
            throw new CannotCreateReservation();
    }

    public List<Reservation> getFreeTermsForOrganization(Organization organization, LocalDate from, LocalDate till) throws CannotListReservation {
        List<Reservation> notAvailableTerms = getTermsForOrganization(organization, from, till);
        List<Reservation> allTerms = generateListReservation(organization, from, till);

        if (!allTerms.removeAll(notAvailableTerms)) {
            throw new CannotListReservation();
        }
        return allTerms;
    }

    private List<Reservation> generateListReservation(Organization organization, LocalDate from, LocalDate till)
    {
        LocalDateTime iterator = from.atStartOfDay();

        List<DayOfWeek> workingDays = organization.workingDays
                .stream()
                .map(day -> day.workingDay).collect(Collectors.toList());

        List<Reservation> list = new LinkedList<>();
        while (iterator.isBefore(till.atTime(23, 59, 59))) {
            DayOfWeek dayOfWeekIterator = iterator.getDayOfWeek();
            if (!workingDays.contains(dayOfWeekIterator)) {
                iterator = iterator.plusDays(1);
                continue;
            }

            list.addAll(generateListReservation(organization, iterator));
            iterator = iterator.plusDays(1);
        }

        return list;
    }

    private List<Reservation> generateListReservation(Organization organization, LocalDateTime day)
    {
        DayOfWeek dayOfWeek = day.getDayOfWeek();
        WorkingDay workingDay = getWorkingDayByDay(dayOfWeek, organization.workingDays);

        LocalDateTime iterator = day.withHour(workingDay.workingFrom.getHour())
                .withMinute(workingDay.workingFrom.getMinute())
                .withSecond(0)
                .withNano(0)
                ;

        LocalDateTime endOfWorkingDay = day.withHour(workingDay.workingTill.getHour())
                .withMinute(workingDay.workingTill.getMinute())
                .withSecond(0)
                .withNano(0)
                ;

        List<Reservation> list = new LinkedList<>();
        while (iterator.isBefore(endOfWorkingDay)) {
            Reservation reservation = new Reservation();
            reservation.organization = organization;
            reservation.term = iterator;
            list.add(reservation);
            iterator = iterator.plusSeconds(workingDay.testInterval * workingDay.timeUnit.toSeconds());
        }

        return list;
    }

    public List<Reservation> getTermsForOrganization(Organization organization, LocalDate from, LocalDate till)
    {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Reservation> query = builder.createQuery(Reservation.class);
        Root<Reservation> reservationQuery = query.from(Reservation.class);
        Root<Organization> organizationQuery = query.from(Organization.class);
        query.select(reservationQuery);


        ParameterExpression<Long> idParameter = builder.parameter(Long.class);
        ParameterExpression<LocalDateTime> fromParameter = builder.parameter(LocalDateTime.class);
        ParameterExpression<LocalDateTime> tillParameter = builder.parameter(LocalDateTime.class);

        query.where(builder.equal(organizationQuery.get("id"), idParameter));
        query.where(builder.greaterThanOrEqualTo(reservationQuery.get("from"), fromParameter));
        query.where(builder.lessThanOrEqualTo(reservationQuery.get("till"), tillParameter));

        return em.createQuery(query)
                .setParameter(idParameter, organization.id)
                .setParameter(fromParameter, from.atStartOfDay())
                .setParameter(tillParameter, till.atTime(23, 59, 59))
                .getResultList();
    }
}
