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
package com.vcollaborate.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.joda.time.DateTime;

/**
 * 
 * @author Christian Sterzl
 * @since 1.2.4
 * 
 */
public class FutureValidator implements ConstraintValidator<Future, Object> {

    private boolean today = false;

    /**
     * {@inheritDoc}
     * 
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(final Future constraintAnnotation) {
        this.today = constraintAnnotation.today();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
     *                  javax.validation.ConstraintValidatorContext)
     */
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        DateTime dateTime = new DateTime(value);
        if (!today) {
            return dateTime.isAfterNow();
        }
        return dateTime.isAfter(new DateTime().toDateMidnight());
    }
}
