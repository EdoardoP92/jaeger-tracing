package jaeger.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;

@Component
public class HelloJaeger {

	@Autowired
	private Tracer tracer;
	
	public void executeAction(String operationName) {
		
		Span span = this.startSpan(operationName);
		span.setTag("HELLO", "HELLO_JAEGER");
		try(Scope scope = tracer.scopeManager().activate(span)){
			System.out.println("Hello Jaeger-Tracing");
			span.log(String.format("<---------- Hello Jaeger-Tracing ----------> %s", operationName+"AAAA"));
		}finally {
			this.endSpan(span);
		}
		
	}
	
	private Span startSpan(String operationName) {
		
		Span ret = tracer.buildSpan(operationName).start();
		return ret;
	}
	
	private void endSpan(Span spanToShutdown) {
		
		spanToShutdown.finish();
	}
}
