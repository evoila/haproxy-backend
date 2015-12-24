package de.haproxyhq.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.haproxyhq.nosql.model.HaProxyConfig;

public class TestUtils {

	public static HaProxyConfig createExampleHaProxyConfig(){
		HashMap<String, List<String>> global = new HashMap<>();
		global.put("deamon", new ArrayList<String>());
		global.put("maxconn", Arrays.asList("256"));
		
		HashMap<String, List<String>> defaults = new HashMap<>();
		defaults.put("mode", Arrays.asList("http"));
		defaults.put("timeout", Arrays.asList("connect", "5000ms"));
		
		HashMap<String, List<String>> listen = new HashMap<>();
		listen.put("_header", Arrays.asList("http-in"));
		listen.put("server", Arrays.asList("server1", "127.0.0.1:8000", "maxconn", "32"));
		
		HashMap<String, List<String>> frontend = new HashMap<>();
		frontend.put("_header", Arrays.asList("http-in"));
		frontend.put("bind", Arrays.asList("*:80"));
		
		HashMap<String, List<String>> backend = new HashMap<>();
		backend.put("_header", Arrays.asList("servers"));
		backend.put("server", Arrays.asList("server1", "127.0.0.1:8000", "maxconn", "32"));

		return new HaProxyConfig(global, defaults, listen, frontend, backend);
	}
}
