package org.acme.personaldata;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;

import java.util.HashMap;
import java.util.Map;

public class FakeKafkaResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        Map<String, String> props = InMemoryConnector.switchOutgoingChannelsToInMemory("result");
        env.putAll(props);
        return env;
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }
}
