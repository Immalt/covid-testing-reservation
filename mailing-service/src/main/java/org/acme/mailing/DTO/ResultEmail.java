package org.acme.mailing.DTO;

import org.acme.mailing.Enum.EmailType;

public class ResultEmail {
    public EmailType emailType = EmailType.RESULTS;
    public Result emailData;
}
