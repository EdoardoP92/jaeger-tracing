package jaeger.tracing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import jaeger.tracing.rest.client.SpringRestTemplate;

@Component
public class OtherComponent2 {

	private SpringRestTemplate springRestTemplate;
	
	public OtherComponent2() {}

	@Autowired
	public OtherComponent2(SpringRestTemplate springRestTemplate) {
		super();
		this.springRestTemplate = springRestTemplate;
	}

	public String greet(Span parentSpan) {

		//create child span
		Span span = GlobalTracer.get().buildSpan("other-component-2")
				.asChildOf(parentSpan).start();

		span.log("other compnent-2 class ...");
		span.setBaggageItem("class", this.getClass().getName());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		span.finish();
		springRestTemplate.callDummyEndPoint();
		
		return "returning component";
	}
}
