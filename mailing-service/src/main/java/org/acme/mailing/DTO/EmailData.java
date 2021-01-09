package org.acme.mailing.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;

public abstract class EmailData {
    @NotNull
    @Email
    public String email;
}
