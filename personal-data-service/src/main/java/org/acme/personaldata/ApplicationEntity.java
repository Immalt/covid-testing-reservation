package org.acme.personaldata;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
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

    @OneToMany(mappedBy = "application", targetEntity = ResultEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ResultEntity> results = new ArrayList<>();

    public ApplicationEntity() { }

    public static List<ApplicationEntity> getByIDCardNumber(String IDCardNumber, Integer page, Integer perPage)
    {
        return find("IDCardNumber", IDCardNumber).page(page, perPage).list();
    }
}
