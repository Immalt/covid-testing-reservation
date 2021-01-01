package org.acme.mailing.Factory;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.api.ResourcePath;
import org.acme.mailing.Enum.EmailType;
import org.acme.mailing.Exception.UnknownEmailType;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TemplateFactory {
    @ResourcePath("Reservation/NewReservation")
    MailTemplate created;

    @ResourcePath("Reservation/CancelReservation")
    MailTemplate canceled;


    @ResourcePath("Result/Result")
    MailTemplate results;

    public MailTemplate  createTemplate(EmailType emailType) throws UnknownEmailType {
        switch (emailType)
        {
            case CREATED_RESERVATION:
                return created;
            case CANCELLED_RESERVATION:
                return canceled;
            case RESULTS:
                return results;
            default:
                throw new UnknownEmailType();
        }
    }
}
