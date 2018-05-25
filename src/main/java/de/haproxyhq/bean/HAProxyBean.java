package de.haproxyhq.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix="haproxy")
public class HAProxyBean {

	private String externalIp;
	private PortRange portRange;
	
	public static class PortRange {
		private int start;
		private int end;
		
		public int getStart() {
			return start;
		}
		
		public int getEnd() {
			return end;
		}
		
		public void setStart(int start) {
			this.start = start;
		}
	
		public void setEnd(int end) {
			this.end = end;
		}
	}
	
	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	public PortRange getPortRange() {
		return portRange;
	}

	public void setPortRange(PortRange portRange) {
		this.portRange = portRange;
	}

	public int getPortRangeStart() {
			return portRange.getStart();
	}

	public int getPortRangeEnd() {
		return portRange.getEnd();
	}
}
