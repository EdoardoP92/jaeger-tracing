package jaeger.tracing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.opentracing.Tracer;

@SpringBootApplication
public class JaegerTracingApplication {

	@Value("${opentracing.jaeger.service-name}")
	private String opentracingJaegerServiceName;
	
	@Value("${opentracing.jaeger.log-spans}")
	private boolean opentracingJaegerLogSpans;
	
	@Value("${opentracing.jaeger.udp-sender.host}")
	private String opentracingJaegerUdpSenderHost;
	
	@Value("${opentracing.jaeger.udp-sender.port}")
	private int opentracingJaegerUdpSenderPort;
	
	@Value("${opentracing.jaeger.probabilistic-sampler.sampling-rate}")
	private double opentracingJaegerProbabilisticSamplerSamplingRate;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	/**
	 * applicative configuration
	 */
//	@Bean
//	public Tracer tracing() {
//		
//		SamplerConfiguration samplerConfig = new SamplerConfiguration()
//				.fromEnv()
//                .withType(ProbabilisticSampler.TYPE);
//                .withParam(this.opentracingJaegerProbabilisticSamplerSamplingRate);
//		
//		SenderConfiguration senderConfig = new SenderConfiguration()
////				.fromEnv()
//				.withAgentHost(this.opentracingJaegerUdpSenderHost)
//				.withAgentPort(this.opentracingJaegerUdpSenderPort);
//       
//		ReporterConfiguration reporterConfig = new ReporterConfiguration()
////				.fromEnv()
//                .withLogSpans(this.opentracingJaegerLogSpans)
//                .withSender(senderConfig);
//        
//		Configuration config = new Configuration(this.opentracingJaegerServiceName)
//				.withSampler(samplerConfig);
//                .withReporter(reporterConfig);
//		
//		return config.getTracer();
//	}

	public static void main(String[] args) {
		SpringApplication.run(JaegerTracingApplication.class, args);
	}
}
