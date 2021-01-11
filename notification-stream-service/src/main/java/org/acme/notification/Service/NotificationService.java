package org.acme.notification.Service;

import org.acme.notification.DTO.EmailData;
import org.acme.notification.Enum.EmailType;
import org.acme.notification.Exception.UnknownEmailType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationService {

    public void sendNotification(EmailType emailType, EmailData emailData) throws UnknownEmailType {

    }
}
