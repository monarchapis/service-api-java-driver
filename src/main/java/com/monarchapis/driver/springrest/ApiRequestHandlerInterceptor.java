package com.monarchapis.driver.springrest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.ApiVersion;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.annotation.RequestWeight;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;

@Component
public class ApiRequestHandlerInterceptor extends HandlerInterceptorAdapter {
	@Inject
	private Authenticator authenticator;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			return preHandleMethod(request, response, (HandlerMethod) handler);
		} else {
			return true;
		}
	}

	private boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		Method method = handler.getMethod();

		ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
		String operation = apiOperation != null ? apiOperation.value() : method.getName();
		OperationNameHolder.setCurrent(operation);

		ApiVersion apiVersion = getAnnotation(method, ApiVersion.class);

		if (apiVersion != null) {
			VersionHolder.setCurrent(apiVersion.value());
		}

		BypassAnalytics bypassAnalytics = getAnnotation(method, BypassAnalytics.class);

		if (bypassAnalytics != null) {
			BypassAnalyticsHolder.setCurrent(true);
		}

		Authorize authorize = getAnnotation(method, Authorize.class);

		if (authorize != null) {
			RequestWeight requestWeight = method.getAnnotation(RequestWeight.class);
			BigDecimal weight = requestWeight != null ? new BigDecimal(requestWeight.value())
					: Authenticator.NORMAL_WEIGHT;
			String[] client = authorize.client();
			String[] delegated = authorize.delegated();
			boolean user = authorize.user();
			Claim[] claims = authorize.claims();

			authenticator.performAccessChecks(weight, client, delegated, user, claims);
		}

		return true;
	}

	private static <T extends Annotation> T getAnnotation(Method method, Class<T> clazz) {
		T annotation = method.getAnnotation(clazz);

		if (annotation == null) {
			Class<?> declaringClass = method.getDeclaringClass();
			annotation = declaringClass.getAnnotation(clazz);

			while (annotation == null && declaringClass.getSuperclass() != null) {
				declaringClass = declaringClass.getSuperclass();
				annotation = declaringClass.getAnnotation(clazz);
			}
		}

		return annotation;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}
}