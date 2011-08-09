package br.com.caelum.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
				Calendar startDate = calendarInstanceFromField(instance, field);
				intervals.put(idAnnotated, new Interval(startDate));
			}

			for (Field field : endDateFields) {
				int idAnnotated = idFromEndDate(field);
				Calendar endDate = calendarInstanceFromField(instance, field);
				int expectedInterval = expectedInterval(field);

				Interval intervalWithStartDate = intervals.get(idAnnotated);
				
				intervalWithStartDate.intervalLimitInformation(endDate, expectedInterval);
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
		private Calendar startDate;
		private Calendar endDate;
		private Integer expectedDaysInterval;
		private boolean duplicatedEndDate;

		public Interval(Calendar startDate) {
			this.startDate = startDate;
		}

		public boolean isValid() {
			if (duplicatedEndDate || endDate == null || startDate == null) {
				return true;
			}
			Calendar mininumExpectedDate = daysAfter(startDate, expectedDaysInterval);

			return endDate.after(mininumExpectedDate) || endDate.equals(mininumExpectedDate);
		}

		public void intervalLimitInformation(Calendar endDate,
				int expectedDaysInterval) {
			if (this.endDate == null) {
				this.endDate = endDate;
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

	private Calendar daysAfter(Calendar date, int days) {
		Calendar dateAfter = (Calendar) date.clone();
		dateAfter.add(Calendar.DAY_OF_MONTH, days);
		return dateAfter;
	}

	private int expectedInterval(Field field) {
		return field.getAnnotation(EndDate.class).minimumDaysRange();
	}

	private int idFromStartDate(Field field) {
		return field.getAnnotation(StartDate.class).id();
	}

	private int idFromEndDate(Field field) {
		return field.getAnnotation(EndDate.class).id();
	}

	private Calendar calendarInstanceFromField(Object instance, Field field)
			throws IllegalAccessException {
		field.setAccessible(true);
		return (Calendar) field.get(instance);
	}

	private boolean containsAnnotation(Field field,
			Class<? extends Annotation> annotation) {
		return field.getAnnotation(annotation) != null;
	}

	public void initialize(DateRange annotation) {

	}
}
