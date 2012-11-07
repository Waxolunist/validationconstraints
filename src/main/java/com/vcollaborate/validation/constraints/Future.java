package com.vcollaborate.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This class is basically a copy of {@link javax.validation.constraints.Future}. 
 * 
 * The annotated element must be a date in the future or if {@link #today()} is set to
 * true, the date has to be in the future or today.
 * 
 * <code>null</code> elements are considered valid.
 *
 * @author Christian Sterzl
 */
@Constraint(validatedBy = FutureValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Future {
        String message() default "{javax.validation.constraints.Future.message}";

        Class<?>[] groups() default { };

        Class<? extends Payload>[] payload() default {};

        boolean today() default false;
}
