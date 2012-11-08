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
package com.vcollaborate.validation.constraints.allowedvalues;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates an integer value against an array of allowed values.
 * 
 * @author Christian Sterzl
 * @since 1.0
 * 
 */
public class AllowdIntegersValidator implements ConstraintValidator<AllowedIntegers, Object> {

    private List<Integer> allowedValues;

    private boolean nullAllowed = true;

    /**
     * {@inheritDoc}
     * 
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(final AllowedIntegers constraintAnnotation) {
        allowedValues = new ArrayList<Integer>();
        for (int index = 0; index < constraintAnnotation.value().length; index++) {
            allowedValues.add(constraintAnnotation.value()[index]);
        }

        nullAllowed = constraintAnnotation.nullAllowed();
    }

    /**
     * {@inheritDoc}
     * 
     * Test, if null is allowed ({@link AllowedIntegers#nullAllowed()}), if the
     * value is contained in the array {@link AllowedIntegers#value()}.
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
     *      javax.validation.ConstraintValidatorContext)
     */
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = false;

        if (nullAllowed && value == null) {
            return true;
        }

        if (value instanceof Integer) {
            valid = allowedValues.contains(value);
        }

        return valid;
    }
}
