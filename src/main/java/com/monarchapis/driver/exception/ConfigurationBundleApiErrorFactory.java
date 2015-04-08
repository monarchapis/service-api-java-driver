package com.monarchapis.driver.exception;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.configuration.ConfigurationBundle;
import com.monarchapis.driver.servlet.ApiRequest;

public class ConfigurationBundleApiErrorFactory implements ApiErrorFactory {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationBundleApiErrorFactory.class);

	@Inject
	private ConfigurationBundle configurationBundle;

	private String moreInfoFormat = null;

	public ConfigurationBundle getConfigurationBundle() {
		return configurationBundle;
	}

	public void setConfigurationBundle(ConfigurationBundle bundle) {
		this.configurationBundle = bundle;
	}

	public String getMoreInfoFormat() {
		return moreInfoFormat;
	}

	public void setMoreInfoFormat(String moreInfoFormat) {
		this.moreInfoFormat = moreInfoFormat;
	}

	@Override
	public ApiErrorException exception(String errorReason) {
		return new ApiErrorException(error(errorReason, null));
	}

	@Override
	public ApiErrorException exception(String errorReason, String template, Object... args) {
		return new ApiErrorException(error(errorReason, template, args));
	}

	@Override
	public ApiError error(String errorReason) {
		return error(errorReason, null);
	}

	@Override
	public ApiError error(String errorReason, String template, Object... args) {
		try {
			Validate.notBlank(errorReason, "errorReason is a required parameter");
			ApiRequest request = ApiRequest.getCurrent();
			String[] variants = getLocaleArray(request);

			int status = getVariantInteger(errorReason, "status", template, variants, args);
			String message = getVariantString(errorReason, "message", template, variants, args);
			String developerMessage = getVariantString(errorReason, "developerMessage", template, variants, args);
			String errorCode = getVariantString(errorReason, "errorCode", template, variants, args);
			String moreInfo = null;

			if (moreInfoFormat != null && errorCode != null) {
				moreInfo = MessageFormat.format(moreInfoFormat, errorReason, errorCode);
			}

			ApiError error = new ApiError(status, errorReason, message, developerMessage, errorCode);
			error.setMoreInfo(moreInfo);

			return error;
		} catch (Exception e) {
			logger.error("Could not create API error", e);
			String reason = "systemError";
			String moreInfo = null;

			if (moreInfoFormat != null) {
				moreInfo = MessageFormat.format(moreInfoFormat, reason, "SYSTEM-001");
			}

			return new ApiError( //
					500, //
					errorReason, //
					"A general error has occurred.", //
					"A general error has occurred.", //
					"SYSTEM-001", //
					moreInfo);
		}
	}

	private String getVariantString(String errorReason, String property, String template, String[] variants,
			Object... args) {
		String message = null;

		if (StringUtils.isNotBlank(template)) {
			message = configurationBundle.getString(errorReason + '.' + template + '.' + property, variants, args)
					.orNull();
		}

		if (message == null && args.length > 0) {
			message = configurationBundle.getString(errorReason + '.' + args.length + '.' + property, variants, args)
					.orNull();
		}

		if (message == null) {
			message = configurationBundle.getString(errorReason + ".default." + property, variants, args).orNull();
		}

		if (message == null) {
			throw new IllegalArgumentException(MessageFormat.format("Could not find string {0}:{1}", errorReason,
					property));
		}

		return message;
	}

	private Integer getVariantInteger(String errorReason, String property, String template, String[] variants,
			Object... args) {
		Integer value = null;

		if (StringUtils.isNotBlank(template)) {
			value = configurationBundle.getInteger(errorReason + '.' + template + '.' + property, variants).orNull();
		}

		if (value == null && args.length > 0) {
			value = configurationBundle.getInteger(errorReason + '.' + args.length + '.' + property, variants).orNull();
		}

		if (value == null) {
			value = configurationBundle.getInteger(errorReason + ".default." + property, variants).orNull();
		}

		if (value == null) {
			throw new IllegalArgumentException(MessageFormat.format("Could not find integer {0}:{1}", errorReason,
					property));
		}

		return value;
	}

	private static String[] getLocaleArray(ApiRequest request) {
		Enumeration<Locale> locales = request.getLocales();
		ArrayList<String> localeList = new ArrayList<String>(5);

		while (locales.hasMoreElements()) {
			Locale locale = locales.nextElement();
			locale = new Locale(locale.getLanguage(), locale.getCountry());
			String lang = locale.getLanguage();

			if (StringUtils.isNotBlank(lang) && !"null".equals(lang)) {
				if (StringUtils.isNotBlank(locale.getCountry())) {
					localeList.add(locale.getLanguage() + '-' + locale.getCountry());
				}

				localeList.add(locale.getLanguage());
			}
		}

		String[] localArray = localeList.toArray(new String[localeList.size()]);

		return localArray;
	}
}
