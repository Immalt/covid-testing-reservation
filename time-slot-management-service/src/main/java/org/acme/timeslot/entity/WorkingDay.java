package org.acme.timeslot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.timeslot.enums.TimeUnit;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"organization_id", "working_day"})
        }
)
@RegisterForReflection
public class WorkingDay extends PanacheEntity {
    @ManyToOne(targetEntity = Organization.class)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @JsonIgnore
    public Organization organization;

    @Column(nullable = false, name = "test_interval")
    public Integer testInterval;

    @Column(nullable = false, name = "unit")
    @Enumerated(EnumType.STRING)
    public TimeUnit timeUnit;

    @Column(nullable = false, name = "working_day")
    @Enumerated(EnumType.STRING)
    public DayOfWeek day;

    @Column(nullable = false, name = "working_from")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    public LocalTime workingFrom;

    @Column(nullable = false, name = "working_till")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    public LocalTime workingTill;
}
