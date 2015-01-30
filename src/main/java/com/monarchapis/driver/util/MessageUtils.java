package com.monarchapis.driver.util;

import org.slf4j.helpers.MessageFormatter;

public abstract class MessageUtils {
	private MessageUtils() {
	}

	public static String format(String format, Object... args) {
		return MessageFormatter.arrayFormat(format, args).getMessage();
	}
}
