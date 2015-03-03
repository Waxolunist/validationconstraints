/**
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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This class is basically a copy of {@link org.hibernate.validator.constraints.Email}.
 * 
 * The annotated element must be a valid email address. This class adds the field
 * {@link #requiressuffix()}, thus only email adresses with a domain suffix will be valid.
 * To achieve the behaviour of {@link org.hibernate.validator.constraints.Email} set
 * this parameter to false.
 *
 * @author Christian Sterzl
 * @since 1.2.6
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@Pattern(regexp = "")
public @interface Email {
	String message() default "{org.hibernate.validator.constraints.Email.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return an additional regular expression the annotated string must match. The default is any string ('.*')
	 */
	@OverridesAttribute(constraint = Pattern.class, name = "regexp") String regexp() default ".*";

	/**
	 * @return used in combination with {@link #regexp()} in order to specify a regular expression option
	 */
	@OverridesAttribute(constraint = Pattern.class, name = "flags") Pattern.Flag[] flags() default { };

	boolean requiressuffix() default true;
	
	/**
	 * Defines several {@code @Email} annotations on the same element.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		Email[] value();
	}
}
