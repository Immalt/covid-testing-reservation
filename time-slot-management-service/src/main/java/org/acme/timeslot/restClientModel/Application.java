package org.acme.timeslot.restClientModel;

import org.hibernate.id.UUIDGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

public class Application {
    public String firstName;
    public String lastName;
    public String idCardNumber;
    public String medicalInsurance;
    public String email;
}
