package org.acme.mailing.DTO;

import io.smallrye.common.constraint.NotNull;

import javax.validation.constraints.Email;

public abstract class EmailData {
    @NotNull
    @Email
    public String email;
}
