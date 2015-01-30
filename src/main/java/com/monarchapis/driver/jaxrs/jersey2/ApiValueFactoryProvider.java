package com.monarchapis.driver.jaxrs.jersey2;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;

import com.monarchapis.driver.annotation.ApiInject;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.service.v1.EventsApi;
import com.monarchapis.driver.service.v1.ServiceApi;
import com.monarchapis.driver.service.v1.ServiceContainer;

/**
 * Class to take {@link ApiContext} object from the thread and make sure it can
 * be automatically injected in resources.
 */
@Singleton
final class ApiValueFactoryProvider extends AbstractValueFactoryProvider {

	/**
	 * Injection resolver for {@link ApiInject} annotation. Will create a
	 * Factory Provider for the actual resolving of the {@link ApiContext}
	 * object.
	 */
	@Singleton
	static final class InjectionResolver extends ParamInjectionResolver<ApiInject> {

		/**
		 * Create new {@link ApiInject} annotation injection resolver.
		 */
		public InjectionResolver() {
			super(ApiValueFactoryProvider.class);
		}
	}

	/**
	 * Factory implementation for resolving request-based attributes and other
	 * information.
	 */
	private static final class ApiContextValueFactory extends AbstractContainerRequestValueFactory<ApiContext> {
		/**
		 * Fetch the ApiContext object from the request.
		 * 
		 * @return {@link ApiContext} stored on the request, or NULL if no
		 *         object was found.
		 */
		public ApiContext provide() {
			return ApiContext.getCurrent();
		}
	}

	private static final class ServiceApiValueFactory extends AbstractContainerRequestValueFactory<ServiceApi> {
		public ServiceApi provide() {
			return ServiceContainer.getInstance().getServiceApi();
		}
	}

	private static final class EventsApiValueFactory extends AbstractContainerRequestValueFactory<EventsApi> {
		public EventsApi provide() {
			return ServiceContainer.getInstance().getEventsApi();
		}
	}

	/**
	 * {@link ApiInject} annotation value factory provider injection
	 * constructor.
	 * 
	 * @param mpep
	 *            multivalued parameter extractor provider.
	 * @param injector
	 *            injector instance.
	 */
	@Inject
	public ApiValueFactoryProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator injector) {
		super(mpep, injector, Parameter.Source.UNKNOWN);
	}

	/**
	 * Return a factory for the provided parameter. We only expect
	 * {@link ApiContext} objects being annotated with {@link ApiInject}
	 * annotation
	 * 
	 * @param parameter
	 *            Parameter that was annotated for being injected
	 * @return {@link ApiContextValueFactory} if parameter matched
	 *         {@link ApiContext} type
	 */
	@Override
	public AbstractContainerRequestValueFactory<?> createValueFactory(Parameter parameter) {
		Class<?> classType = parameter.getRawType();

		if (classType != null) {
			if (classType.equals(ApiContext.class)) {
				return new ApiContextValueFactory();
			} else if (classType.equals(ServiceApi.class)) {
				return new ServiceApiValueFactory();
			} else if (classType.equals(EventsApi.class)) {
				return new EventsApiValueFactory();
			}
		}

		return null;
	}
}