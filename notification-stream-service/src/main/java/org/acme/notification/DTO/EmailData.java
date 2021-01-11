package org.acme.notification.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public abstract class EmailData {
    @NotNull
    @Email
    public String email;
}
