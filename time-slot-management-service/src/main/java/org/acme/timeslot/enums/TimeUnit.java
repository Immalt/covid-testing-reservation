package org.acme.timeslot.enums;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum TimeUnit {
    MINUTES {
        @Override
        public Integer toSeconds() {
            return 60;
        }
    },
    SECONDS {
        @Override
        public Integer toSeconds() {
            return 1;
        }
    },
    HOURS {
        @Override
        public Integer toSeconds() {
            return 60*60;
        }
    },
    ;
    public abstract Integer toSeconds();
}
