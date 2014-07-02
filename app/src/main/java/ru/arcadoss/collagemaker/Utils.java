package ru.arcadoss.collagemaker;

import java.util.Iterator;

/**
 * Created by arcturus at 02.07.14
 */
public class Utils {
	private static final String EMPTY_STRING = "";

	public static String joinStrings(final Iterator<?> iterator, final String separator) {
		if (iterator == null || !iterator.hasNext()) {
			return EMPTY_STRING;
		}
		final Object first = iterator.next();
		if (!iterator.hasNext()) {
			final String result = first.toString();
			return result;
		}

		// two or more elements
		final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			final Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}

	public static String joinStrings(final Iterable<?> iterable, final String separator) {
		if (iterable == null) {
			return EMPTY_STRING;
		}

		return joinStrings(iterable.iterator(), separator);
	}

}
