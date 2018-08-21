package de.haproxyhq.config.setup;

import de.haproxyhq.nosql.model.Agent;
import de.haproxyhq.nosql.model.HAProxyConfig;
import de.haproxyhq.nosql.model.Section;
import de.haproxyhq.nosql.repositories.AgentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.management.resources.agent;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class AgentConfiguration {

    @Value("${haproxy.agent.id}")
    private String haProxyAgentId;

    @Value("${haproxy.external-ip}")
    private String haProxyExternalIp;

    @Value("${haproxy.agent.token}")
    private String haProxyAgentToken;

    private AgentRepository agentRepository;

    public AgentConfiguration(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @PostConstruct
    public void setupAgent() {
        Agent existingAgent = agentRepository.findAgentByAgentId(haProxyAgentId).orElse(null);

        if (existingAgent == null) {
            Agent agent = new Agent();
            HAProxyConfig haProxyConfig = new HAProxyConfig();
            List<Section> sections = new ArrayList<>();
            haProxyConfig.setSections(sections);
            agent.setHaProxyConfig(haProxyConfig);
            agent.setDescription("Default HaProxy Configuration for Service Key Agent");
            agent.setIp(haProxyExternalIp);
            agent.setName("Default HaProxy Agent");
            agent.setAuthToken(haProxyAgentToken);
            agent.setVersion("1.0");
            agent.setAgentId(haProxyAgentId);

            agentRepository.save(agent);
        }

    }

}
