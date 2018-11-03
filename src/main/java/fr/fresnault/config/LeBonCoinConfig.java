package fr.fresnault.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class LeBonCoinConfig {

	private Map<String, String> headers = new HashMap<>();

	public Map<String, String> getHeaders() {
		if (headers.isEmpty()) {
			headers.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			headers.put("Accept-Encoding", "gzip, deflate, br");
			headers.put("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.put("Connection", "keep-alive");
			headers.put("Host", "www.leboncoin.fr");
			headers.put("Upgrade-Insecure-Requests", "1");
			headers.put("User-Agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");

		}
		return headers;
	}

}