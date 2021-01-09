package org.acme.mailing.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public abstract class EmailData {
    @NotNull
    @Email
    public String email;
}
