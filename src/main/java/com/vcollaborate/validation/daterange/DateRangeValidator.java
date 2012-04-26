/**
 * This file is part of DateRangeValidator.
 *
 *  DateRangeValidator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  DateRangeValidator is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with DateRangeValidator.  If not, see <http://www.gnu.org/licenses/>.
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
	
	public boolean isValid(Object instance, ConstraintValidatorContext ctx) {
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
				intervals.put(idAnnotated, new Interval(calendarInstanceFromField(instance, field)));
			}

			for (Field field : endDateFields) {
				int idAnnotated = idFromEndDate(field);
				long expectedInterval = expectedInterval(field);

				Interval intervalWithStartDate = intervals.get(idAnnotated);
				
				intervalWithStartDate.intervalLimitInformation(calendarInstanceFromField(instance, field), expectedInterval);
			}
		} catch (IllegalAccessException e) {
			return true;
		}

		for (Interval interval : intervals.values()) {
			if (!interval.isValid()) {
				return false;
			}
		}

		return true;
	}
	
	private class Interval {
		private DateTime startDate;
		private DateTime endDate;
		private long expectedDaysInterval;
		private boolean duplicatedEndDate;

		public Interval(Object startDate) {
			if(startDate != null)
				this.startDate = new DateTime(startDate);
		}

		public boolean isValid() {
			if (duplicatedEndDate || endDate == null || startDate == null) {
				return true;
			}
			Duration duration = new Duration(startDate, endDate);
			return duration.getStandardDays() >= expectedDaysInterval;
		}

		public void intervalLimitInformation(Object endDate,
				long expectedDaysInterval) {
			if (this.endDate == null) {
				if(endDate != null)
					this.endDate = new DateTime(endDate);
				this.expectedDaysInterval = expectedDaysInterval;
			} else {
				duplicatedEndDate = true;
			}
		}
	}

	private void annotatedFields(Object instance, List<Field> startDateFields,
			List<Field> endDateFields) {
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

	private long expectedInterval(Field field) {
		return field.getAnnotation(EndDate.class).minimumDaysRange();
	}

	private int idFromStartDate(Field field) {
		return field.getAnnotation(StartDate.class).id();
	}

	private int idFromEndDate(Field field) {
		return field.getAnnotation(EndDate.class).id();
	}

	private Object calendarInstanceFromField(Object instance, Field field)
			throws IllegalAccessException {
		field.setAccessible(true);
		return field.get(instance);
	}

	private boolean containsAnnotation(Field field,
			Class<? extends Annotation> annotation) {
		return field.getAnnotation(annotation) != null;
	}

	public void initialize(DateRange annotation) {

	}
}
