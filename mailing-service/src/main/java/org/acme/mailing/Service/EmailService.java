package org.acme.mailing.Service;

import io.quarkus.mailer.MailTemplate;
import org.acme.mailing.DTO.EmailData;
import org.acme.mailing.Enum.EmailType;
import org.acme.mailing.Exception.UnknownEmailType;
import org.acme.mailing.Factory.TemplateFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EmailService {
    @Inject
    TemplateFactory templateFactory;

    public void sendEmail(EmailType emailType, EmailData emailData) throws UnknownEmailType {
        MailTemplate template = templateFactory.createTemplate(emailType);

        template.data("data", emailData).
                to(emailData.email)
                .subject(emailType.subject())
                .send()
        ;
    }
}
