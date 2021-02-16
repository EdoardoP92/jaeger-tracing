package jaeger.tracing.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapAdapter;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

@RestController
@RequestMapping("/")
public class JaegerController {
	
	@Autowired
	private OtherComponent otherComponent;
	
	@Autowired
	private Tracer tracer;
	
	private SpanContext parentContext;
	
	@GetMapping
	public String helloJaegerPoste(@RequestHeader HttpHeaders headers) {
		
//		GlobalTracer.registerIfAbsent(this.tracer);
		String ret = null;
		
		TextMap httpHeadersCarrier = new TextMapAdapter(headers.toSingleValueMap());

		//get context if exist
		this.parentContext = this.tracer.extract(Format.Builtin.HTTP_HEADERS, httpHeadersCarrier);
//		this.parentContext = this.tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headers.toSingleValueMap()));
		//create parent span
		Span span = GlobalTracer.get().buildSpan("controller").asChildOf(parentContext).start();
		
		//set span tags
		Tags.SPAN_KIND.set(span, Tags.SPAN_KIND_SERVER);
		Tags.HTTP_METHOD.set(span, "GET");
		
		//set span log
		span.log("Controller class ...");
		
		//set baggage
		span.setBaggageItem("class", this.getClass().getName());
		
		if(headers.get("jaeger") != null) {
			Tags.HTTP_STATUS.set(span, HttpStatus.OK.value());
			ret = otherComponent.greet(span);
			
		}else {
			Tags.ERROR.set(span, true);
			Tags.HTTP_STATUS.set(span, HttpStatus.BAD_REQUEST.value());
			StringWriter sw = new StringWriter();
			new Exception("jaeger key is not present in headers")
			.printStackTrace(new PrintWriter(sw));
			span.setBaggageItem("Exception", sw.toString());
			ret = otherComponent.greet(span);
		}
		span.finish();
		
		return ret;
	}
}
