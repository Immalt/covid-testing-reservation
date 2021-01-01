package org.acme.mailing.Factory;

import org.acme.mailing.Enum.LinkType;
import org.acme.mailing.Exception.UnknownLinkType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;

@Singleton
public class LinkFactory {
    @ConfigProperty(name = "cancellation.link")
    protected String cancellationLink;

    public String getLink(LinkType linkType) throws UnknownLinkType {
        switch (linkType) {
            case CANCELLATION_LINK:
                return cancellationLink;
            default:
                throw new UnknownLinkType();
        }
    }
}
