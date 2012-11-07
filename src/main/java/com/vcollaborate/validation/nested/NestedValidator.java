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
package com.vcollaborate.validation.nested;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

public class NestedValidator implements ConstraintValidator<Nested, Object> {

	private Class<?> classToValidate;

	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private transient javax.validation.Validator validator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
	 * Annotation)
	 */
	public void initialize(final Nested constraintAnnotation) {
		classToValidate = constraintAnnotation.value();
		validator = factory.getValidator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
	 * javax.validation.ConstraintValidatorContext)
	 */
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		boolean valid = true;

		if (value instanceof Iterable) {
			Iterator<?> iterator = ((Iterable<?>) value).iterator();
			while (iterator.hasNext()) {
				Object validatee = iterator.next();
				valid = valid && validate(validatee);
				if (!valid) {
					break;
				}
			}
		} else {
			valid = validate(value);
		}

		return valid;
	}

	private boolean validate(final Object validatee) {
		if (validatee.getClass().isAssignableFrom(classToValidate)) {
			Set<ConstraintViolation<Object>> violations = validator
					.validate(validatee);
			return (violations.size() == 0);
		} else {
			return false;
		}
	}

}
