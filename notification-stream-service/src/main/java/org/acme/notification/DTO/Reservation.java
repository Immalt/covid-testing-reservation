package org.acme.notification.DTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation extends EmailData {
    @NotNull public String firstName;
    @NotNull public String lastName;
    @NotNull public String IDCardNumber;
    @NotNull public UUID applicationId;
    @NotNull public LocalDateTime term;
}
