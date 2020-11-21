package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodEnumMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForFlux;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForFlux;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForMono;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.webflux.function.OpenApiGeneratorWebFluxRouterFunctionAutoConfiguration;
import de.qaware.openapigeneratorforspring.webflux.function.RouterFunctionHandlerMethodWithInfoBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Import(OpenApiGeneratorWebFluxRouterFunctionAutoConfiguration.class)
public class OpenApiGeneratorWebFluxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Conditional(OpenApiConfigurationProperties.EnabledCondition.class)
    public OpenApiResourceForWebFlux openApiResource(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier openApiObjectMapperSupplier) {
        return new OpenApiResourceForWebFlux(openApiGenerator, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodsProvider handlerMethodsProviderFromWebFlux(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder,
            SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper,
            Map<String, RouterFunction<?>> routerFunctions,
            RouterFunctionHandlerMethodWithInfoBuilder routerFunctionHandlerMethodWithInfoBuilder
    ) {
        return new HandlerMethodsProviderForWebFlux(
                requestMappingHandlerMapping, springWebHandlerMethodBuilder, springWebRequestMethodEnumMapper,
                routerFunctions, routerFunctionHandlerMethodWithInfoBuilder
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiRequestAwareProviderForWebFlux openApiRequestAwareProviderForWebFlux() {
        return new OpenApiRequestAwareProviderForWebFlux();
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForFlux defaultTypeResolverForFlux(InitialSchemaBuilderForFlux initialSchemaBuilder) {
        return new TypeResolverForFlux(initialSchemaBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForFlux defaultInitialSchemaFactoryForFlux() {
        return new InitialSchemaBuilderForFlux();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForMono defaultInitialSchemaFactoryForMono() {
        return new InitialSchemaBuilderForMono();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiBaseUriSupplierForWebFlux openApiBaseUriProviderForWebFlux(OpenApiConfigurationProperties openApiConfigurationProperties) {
        return new OpenApiBaseUriSupplierForWebFlux(openApiConfigurationProperties);
    }
}
