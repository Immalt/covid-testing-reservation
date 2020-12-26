package org.acme.timeslot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@RegisterForReflection
public class Organization extends PanacheEntity {

    @Column(nullable = false, name = "organization_name")
    public String organizationName;

    @OneToMany(mappedBy = "organization", targetEntity = WorkingDay.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<WorkingDay> workingDays = new ArrayList<>();


    @OneToMany(mappedBy = "organization", targetEntity = Reservation.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Reservation> reservations = new ArrayList<>();
}
