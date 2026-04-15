package com.abhi1kush.registrationstore.security;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Defensive layer in addition to prepared statements.
 * Rejects payloads containing common SQL-injection markers/keywords.
 */
@Component
public class SqlInjectionGuard {

	private static final Pattern SUSPICIOUS_SQL_PATTERN = Pattern.compile(
			"(?i)(--|/\\*|\\*/|;|\\b(select|insert|update|delete|drop|truncate|alter|union|exec)\\b)");

	public boolean hasSuspiciousContent(Object request) {
		if (request == null) {
			return false;
		}
		return scanObject(request);
	}

	private boolean scanObject(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof String text) {
			return SUSPICIOUS_SQL_PATTERN.matcher(text).find();
		}
		if (value instanceof Collection<?> items) {
			for (Object item : items) {
				if (scanObject(item)) {
					return true;
				}
			}
			return false;
		}
		Class<?> type = value.getClass();
		if (type.getName().startsWith("java.")) {
			return false;
		}
		Field[] fields = type.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object nested = field.get(value);
				if (scanObject(nested)) {
					return true;
				}
			} catch (IllegalAccessException ignored) {
				// If inaccessible unexpectedly, skip field and continue scan.
			}
		}
		return false;
	}
}
