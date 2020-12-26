package org.acme.timeslot.entity;

import com.fasterxml.jackson.annotation.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@RegisterForReflection
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"organization_id", "term"})
        }
)
public class Reservation extends PanacheEntity {
    @ManyToOne(targetEntity = Organization.class, optional = false)
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    public Organization organization;

    @Column(name = "application_id", nullable = false)
    public UUID applicationId;

    @Column(name = "term", nullable = false)
    public @NotNull LocalDateTime term;

    @Column(name = "created_on", nullable = false)
    @JsonIgnore
    public LocalDateTime createdOn;


    @Column(name = "last_update", nullable = true)
    @JsonIgnore
    public LocalDateTime lastUpdate;

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return organization.equals(that.organization) &&
                term.equals(that.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organization, term);
    }
}
