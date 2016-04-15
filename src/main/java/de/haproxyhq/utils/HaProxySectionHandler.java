/**
 * 
 */
package de.haproxyhq.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.haproxyhq.controller.schema.types.ConnectionDetails;
import de.haproxyhq.nosql.model.HaProxyConfig;
import de.haproxyhq.nosql.model.HaProxyConfig.Section;

/**
 * @author Johannes Hiemer.
 *
 */
@Service
public class HaProxySectionHandler {
	
	private static final String NAME_IDENTIFIER = "name";

	private static final Logger log = LoggerFactory
			.getLogger(HaProxySectionHandler.class);

	private static final String LISTEN_TYPE = "listen";

	private static final String BIND = "bind";

	private static final String BLANK = " ";
	
	private static final String COLON = ":";

	private static final String BIND_IP = "0.0.0.0";

	private static final String SERVER = "server service_endpoint";

	@Value("${haproxy.port_range.start}")
	private int portRangeStart;

	@Value("${haproxy.port_range.end}")
	private int portRangeEnd;
	
	@Value("${haproxy.external_ip}")
	private String haProxyExternalIp;

	private List<Integer> availablePorts = new ArrayList<Integer>();

	private List<Integer> usedPorts = new ArrayList<Integer>();

	public ConnectionDetails append(HaProxyConfig haProxyConfig, ConnectionDetails connectionDetails) {
		List<Section> sections = haProxyConfig.getSections();

		Section section = new Section();

		Map<String, String> sectionProperties = new HashMap<String, String>();
		sectionProperties.put("type", "listen");
		sectionProperties.put(NAME_IDENTIFIER, connectionDetails.getIdentifier());
		section.setSection(sectionProperties);
		
		Integer externalPort = this.resolveNextAvailablePort(haProxyConfig);
		List<String> values = new ArrayList<String>();
		values.add(BIND + BLANK + BIND_IP + COLON + this.resolveNextAvailablePort(haProxyConfig));
		values.add(SERVER + BLANK + connectionDetails.getIp() + COLON + connectionDetails.getPort());
		section.setValues(values);

		sections.add(section);
		
		return new ConnectionDetails(haProxyExternalIp, externalPort);
	}
	
	public boolean exists(HaProxyConfig haProxyConfig, ConnectionDetails connectionDetails) {
		if (haProxyConfig.getSections() != null)
			for (Section section : haProxyConfig.getSections()) {
				if (section.getSection().size() > 0 && section.getSection().get(NAME_IDENTIFIER) != null) {
					if (section.getSection().get(NAME_IDENTIFIER).equals(connectionDetails.getIdentifier())) 
						return true;
				}
			}
		
		return false;
	}

	public void remove(HaProxyConfig haProxyConfig, ConnectionDetails connectionDetails) {
		for (Section section : haProxyConfig.getSections()) {
			if (section.getSection().size() > 0 && section.getSection().get(NAME_IDENTIFIER) != null) {
				if (section.getSection().get(NAME_IDENTIFIER).equals(connectionDetails.getIdentifier())) {
					haProxyConfig.getSections().remove(section);
					break;
				}
			}
		}
	}

	private void listUsedPort(HaProxyConfig haProxyConfig) {
		for (Section section : haProxyConfig.getSections()) 
			if (section.getSection().get("type").equals(LISTEN_TYPE)) 
				for (String value : section.getValues()) 
					if (value.contains(BIND)) {
						try {
							usedPorts.add(Integer
									.parseInt(value
											.substring(value.indexOf(":") + 1, value.length())
											));
						} catch (Exception ex) {
							log.error("Exception during used port retrieval. Config corrupt?", ex);
						}
					}
	}

	private void intersect() {
		availablePorts.removeAll(usedPorts);
	}

	private Integer resolveNextAvailablePort(HaProxyConfig haProxyConfig) {
		this.initAvailablePorts();

		this.listUsedPort(haProxyConfig);
		this.intersect();

		return availablePorts.get(0);
	}

	private void initAvailablePorts() {
		availablePorts = new ArrayList<Integer>();
		for (int i = this.portRangeStart; i <= this.portRangeEnd; i++) {
			availablePorts.add(i);
		}
	}

}