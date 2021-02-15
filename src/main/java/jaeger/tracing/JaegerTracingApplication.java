package jaeger.tracing;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.opentracing.Tracer;

@SpringBootApplication
public class JaegerTracingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaegerTracingApplication.class, args);
	}
	
	@Autowired
	private HelloJaeger helloJaeger;
	
	@Bean
	public Tracer tracing() {
		SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv()
                .withType(ProbabilisticSampler.TYPE)
                .withParam(1);
       
		ReporterConfiguration reporterConfig = ReporterConfiguration
				.fromEnv()
                .withLogSpans(true);
        
		Configuration config = new Configuration("service-name")
				.withSampler(samplerConfig)
                .withReporter(reporterConfig);
		return config.getTracer();
	}
	
	@PostConstruct
	public void helloJaeger() {
		
		this.helloJaeger.executeAction("hello-jaeger");
	}

}
