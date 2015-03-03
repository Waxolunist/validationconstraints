/*
 * Copyright (C) 2012-2015 Christian Sterzl <christian.sterzl@gmail.com>
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This class is basically a copy of {@link javax.validation.constraints.Future} .
 * 
 * The annotated element must be a date in the future or if {@link #today()} is set to true, the
 * date has to be in the future or today.
 * 
 * <code>null</code> elements are considered valid.
 * 
 * @author Christian Sterzl
 * @since 1.2.4
 */
@Constraint(validatedBy = FutureValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Future {
  String message() default "{javax.validation.constraints.Future.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean today() default false;
}
