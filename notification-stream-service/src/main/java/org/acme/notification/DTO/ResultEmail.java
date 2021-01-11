package org.acme.notification.DTO;

import org.acme.notification.Enum.EmailType;

public class ResultEmail {
    public EmailType emailType = EmailType.RESULTS;
    public Result emailData;
}
