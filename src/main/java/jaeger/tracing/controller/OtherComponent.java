package jaeger.tracing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;

@Component
public class OtherComponent {
	
	private OtherComponent2 otherComponent2;
	
	public OtherComponent() {}

	@Autowired
	public OtherComponent(OtherComponent2 otherComponent2) {
		super();
		this.otherComponent2 = otherComponent2;
	}

	public String greet(Span parentSpan) {
		
		String ret = null;
		//create child span
		Span span = GlobalTracer.get().buildSpan("other-component").asChildOf(parentSpan).start();
		
		span.log("other compnent class ...");
		span.setBaggageItem("class", this.getClass().getName());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret = otherComponent2.greet(span);
		span.finish();
		return ret;
	}
}
