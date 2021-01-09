package org.acme.timeslot;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

public class FakeKafkaResource implements QuarkusTestResourceLifecycleManager {
    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        Map<String, String> props = InMemoryConnector.switchOutgoingChannelsToInMemory("reservation");
        env.putAll(props);
        return env;
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }
}
