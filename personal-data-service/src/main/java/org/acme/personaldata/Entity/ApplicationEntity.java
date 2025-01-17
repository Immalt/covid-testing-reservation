package org.acme.personaldata.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@RegisterForReflection
public class ApplicationEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator= UUIDGenerator.GENERATOR_NAME)
    @Column(updatable = false, nullable = false)
    public UUID id;

    @Column(nullable = false, name = "first_name")
    public String firstName;

    @Column(nullable = false, name = "last_name")
    public String lastName;

    @Column(nullable = false, name = "id_card_number")
    public String idCardNumber;

    @Column(nullable = false, name = "medial_insurance")
    public String medicalInsurance;

    @Column(nullable = false, name = "email")
    public String email;

    @OneToMany(mappedBy = "application", targetEntity = ResultEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ResultEntity> results = new ArrayList<>();

    public ApplicationEntity() { }

    public static List<ApplicationEntity> getByIDCardNumber(String IDCardNumber, Integer page, Integer perPage)
    {
        return find("IDCardNumber", IDCardNumber).page(page, perPage).list();
    }
}
