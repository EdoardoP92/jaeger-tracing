package jaeger.tracing.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapAdapter;

@Component
public class SpringRestTemplate {
	
	private RestTemplate restTemplate;
	
	private Tracer tracer;
	
	public SpringRestTemplate() {}

	@Autowired
	public SpringRestTemplate(RestTemplate restTemplate, Tracer tracer) {
		super();
		this.restTemplate = restTemplate;
		this.tracer = tracer;
	}

	public ResponseEntity<String> callDummyEndPoint() {
		
		String url = "http://localhost:8099/dummy";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		TextMap httpHeadersCarrier = new TextMapAdapter(headers.toSingleValueMap());
		
		HttpEntity<String> requestEntity = new HttpEntity<>("{\"Hi\":\"DummyEndPoint\"}", headers);
		tracer.inject(
				tracer.activeSpan().context(),
			    Format.Builtin.HTTP_HEADERS,
			    httpHeadersCarrier
			);

		return this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	}

}
