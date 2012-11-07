/**
 * Copyright (C) 2012 Christian Sterzl <christian.sterzl@gmail.com>
 *
 * This file is part of ValidationConstraints.
 *
 * ValidationConstraints is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ValidationConstraints is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ValidationConstraints.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vcollaborate.validation.daterange;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * @author Christian Sterzl
 */
public class DateRangeValidator implements
		ConstraintValidator<DateRange, Object> {

	private static double MILLILSPERDAY = 86400000.0;

	public boolean isValid(final Object instance, final ConstraintValidatorContext ctx) {
		List<Field> startDateFields = new ArrayList<Field>();
		List<Field> endDateFields = new ArrayList<Field>();

		annotatedFields(instance, startDateFields, endDateFields);

		if (startDateFields.isEmpty() || endDateFields.isEmpty()) {
			return true;
		}

		HashMap<Integer, Interval> intervals = new HashMap<Integer, Interval>();

		try {
			for (Field field : startDateFields) {
				int idAnnotated = idFromStartDate(field);
				intervals.put(idAnnotated, new Interval(
						calendarInstanceFromField(instance, field)));
			}

			for (Field field : endDateFields) {
				int idAnnotated = idFromEndDate(field);
				long expectedInterval = field.getAnnotation(EndDate.class)
						.minimumDaysRange();
				long[] allowedRanges = field.getAnnotation(EndDate.class)
						.allowedDayRanges();
				Interval intervalWithStartDate = intervals.get(idAnnotated);

				intervalWithStartDate.intervalLimitInformation(
						calendarInstanceFromField(instance, field),
						expectedInterval, allowedRanges);
			}
		} catch (IllegalAccessException e) {
		        throw new RuntimeException("This should never happen. If so, please report a bug!", e);
		}

		for (Interval interval : intervals.values()) {
			if (!interval.isValid()) {
				return false;
			}
		}

		return true;
	}

	private void annotatedFields(final Object instance, final List<Field> startDateFields,
			final List<Field> endDateFields) {
		Field[] allModelFields = instance.getClass().getDeclaredFields();
		for (Field field : allModelFields) {
			if (containsAnnotation(field, StartDate.class)) {
				startDateFields.add(field);
			}
			if (containsAnnotation(field, EndDate.class)) {
				endDateFields.add(field);
			}
		}
	}

	private int idFromStartDate(final Field field) {
		return field.getAnnotation(StartDate.class).id();
	}

	private int idFromEndDate(final Field field) {
		return field.getAnnotation(EndDate.class).id();
	}

	private Object calendarInstanceFromField(final Object instance, final Field field)
			throws IllegalAccessException {
		field.setAccessible(true);
		return field.get(instance);
	}

	private boolean containsAnnotation(final Field field,
			final Class<? extends Annotation> annotation) {
		return field.getAnnotation(annotation) != null;
	}

	public void initialize(final DateRange annotation) {

	}

	private class Interval {
		private DateTime startDate;
		private DateTime endDate;
		private long expectedDaysInterval;
		private long[] allowedRanges = {};
		private boolean duplicatedEndDate;

		public Interval(final Object startDate) {
			if (startDate != null)
				this.startDate = new DateTime(startDate);
		}

		public boolean isValid() {
			if (duplicatedEndDate || endDate == null || startDate == null) {
				return true;
			}
			Duration duration = new Duration(startDate, endDate);

			// Rounding fixes #1
			long durationInDays = Math.round(duration.getMillis()
					/ MILLILSPERDAY);

			if (this.allowedRanges.length == 0) {
				return durationInDays >= expectedDaysInterval;
			} else {
				for (int i = 0; i < this.allowedRanges.length; i++) {
					if (this.allowedRanges[i] == durationInDays) {
						return true;
					}
				}
				return false;
			}
		}

		public void intervalLimitInformation(final Object endDate,
				final long expectedDaysInterval, final long[] allowedRanges) {

			if (this.endDate == null) {
				if (endDate != null) {
					this.endDate = new DateTime(endDate);
				}
				if (allowedRanges.length == 0) {
					this.expectedDaysInterval = expectedDaysInterval;
				} else {
					this.allowedRanges = allowedRanges;
				}
			} else {
				duplicatedEndDate = true;
			}
		}
	}
}