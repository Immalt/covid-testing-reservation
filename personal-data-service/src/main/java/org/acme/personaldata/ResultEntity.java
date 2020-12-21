package org.acme.personaldata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Entity
public class ResultEntity extends PanacheEntity {

    @ManyToOne(targetEntity = ApplicationEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @JsonIgnore
    public ApplicationEntity application;

    @Column(nullable = false, name = "result")
    @Enumerated(EnumType.STRING)
    public ResultValuesEnum result;

    @Column(nullable = false, name = "test_type")
    @Enumerated(EnumType.STRING)
    public TestTypeEnum testType;

    public ResultEntity() { }
}
