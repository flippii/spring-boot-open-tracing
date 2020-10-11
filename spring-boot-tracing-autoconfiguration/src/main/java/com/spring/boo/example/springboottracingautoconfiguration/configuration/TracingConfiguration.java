package com.spring.boo.example.springboottracingautoconfiguration.configuration;

import brave.CurrentSpanCustomizer;
import brave.SpanCustomizer;
import brave.Tracing;
import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig.SingleBaggageField;
import brave.baggage.CorrelationScopeConfig.SingleCorrelationField;
import brave.context.slf4j.MDCScopeDecorator;
import brave.http.HttpTracing;
import brave.okhttp3.TracingInterceptor;
import brave.propagation.B3Propagation;
import brave.propagation.CurrentTraceContext;
import brave.propagation.CurrentTraceContext.ScopeDecorator;
import brave.propagation.Propagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.servlet.TracingFilter;
import brave.spring.webmvc.SpanCustomizingAsyncHandlerInterceptor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zipkin2.reporter.Sender;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.servlet.Filter;

@Configuration
// Importing a class is effectively the same as declaring bean methods
@Import(SpanCustomizingAsyncHandlerInterceptor.class)
public class TracingConfiguration {

    static final BaggageField USER_NAME = BaggageField.create("userName");

    /** Allows log patterns to use {@code %{traceId}} {@code %{spanId}} and {@code %{userName}} */
    @Bean
    public ScopeDecorator correlationScopeDecorator() {
        return MDCScopeDecorator.newBuilder()
                .add(SingleCorrelationField.create(USER_NAME)).build();
    }

    /** Propagates trace context between threads. */
    @Bean
    public CurrentTraceContext currentTraceContext(ScopeDecorator correlationScopeDecorator) {
        return ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(correlationScopeDecorator)
                .build();
    }

    /** Configures propagation for {@link #USER_NAME}, using the remote header "user_name" */
    @Bean
    public Propagation.Factory propagationFactory() {
        return BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY)
                .add(SingleBaggageField.newBuilder(USER_NAME).addKeyName("user_name").build())
                .build();
    }

    /** Configuration for how to send spans to Zipkin */
    @Bean
    public Sender sender(@Value("${zipkin.endpoint}") String zipkinEndpoint) {
        return OkHttpSender.create(zipkinEndpoint);
    }

    /** Configuration for how to buffer spans into messages for Zipkin */
    @Bean
    public AsyncZipkinSpanHandler zipkinSpanHandler(Sender sender) {
        return AsyncZipkinSpanHandler.create(sender);
    }

    /** Controls aspects of tracing such as the service name that shows up in the UI */
    @Bean
    public Tracing tracing(
            @Value("${spring.application.name}") String serviceName,
            Propagation.Factory propagationFactory,
            CurrentTraceContext currentTraceContext,
            AsyncZipkinSpanHandler zipkinSpanHandler) {

        return Tracing.newBuilder()
                .localServiceName(serviceName)
                .propagationFactory(propagationFactory)
                .currentTraceContext(currentTraceContext)
                .addSpanHandler(zipkinSpanHandler).build();
    }

    /** Allows someone to add tags to a span if a trace is in progress. */
    @Bean
    public SpanCustomizer spanCustomizer(Tracing tracing) {
        return CurrentSpanCustomizer.create(tracing);
    }

    /** Decides how to name and tag spans. By default they are named the same as the http method. */
    @Bean
    public HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing);
    }

    /** Creates server spans for http requests */
    @Bean
    public Filter tracingFilter(HttpTracing httpTracing) {
        return TracingFilter.create(httpTracing);
    }

    /**
     * Trace {@link OkHttpClient} because {@link OkHttp3ClientHttpRequestFactory} doesn't take a
     * {@link Call.Factory}
     */
    @Bean
    public OkHttpClient tracedOkHttpClient(HttpTracing httpTracing) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(TracingInterceptor.create(httpTracing))
                .build();
    }

    @Bean
    public RestTemplateCustomizer useTracedOkHttpClient(final OkHttpClient okHttpClient) {
        return restTemplate -> restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient));
    }

    @Bean
    public WebMvcConfigurer tracingWebMvcConfigurer(final SpanCustomizingAsyncHandlerInterceptor webMvcTracingCustomizer) {
        return new WebMvcConfigurer() {

            /** Decorates server spans with application-defined web tags */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(webMvcTracingCustomizer);
            }

        };
    }

}
