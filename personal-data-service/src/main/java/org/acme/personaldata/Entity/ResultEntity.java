package org.acme.personaldata.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.personaldata.Enum.ResultValuesEnum;
import org.acme.personaldata.Enum.TestTypeEnum;

import javax.persistence.*;

@Entity
@RegisterForReflection
public class ResultEntity extends PanacheEntity {

    @ManyToOne(targetEntity = ApplicationEntity.class, fetch = FetchType.LAZY, optional = false)
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
