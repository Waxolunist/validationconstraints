/**
 * Copyright (C) 2012-2013 Christian Sterzl <christian.sterzl@gmail.com>
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

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import junit.framework.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

@Slf4j
public class FutureValidatorTest {

    @Test
    public void testsWithToday() throws Exception {
        FutureDate fd = new FutureDate(new Date());

        Assert.assertFalse(isValidAccordingToBeanValidation(fd));
        
        FutureDateWithToday fdwt = new FutureDateWithToday(new Date());

        Assert.assertTrue(isValidAccordingToBeanValidation(fdwt));
    }

    @Test
    public void testsWithTodayAtMidnight() throws Exception {
    	DateMidnight dateMidnight = new DateMidnight();
    	log.info("dateMidnight: {}", dateMidnight);
        FutureDateWithToday fdwt = new FutureDateWithToday(dateMidnight.toDate());

        Assert.assertTrue(isValidAccordingToBeanValidation(fdwt));
    }
    
    @Test
    public void testsInThePast() throws Exception {
        DateTime yesterday = new DateTime().minusDays(1);
        FutureDate fd = new FutureDate(yesterday.toDate());

        Assert.assertFalse(isValidAccordingToBeanValidation(fd));
        
        FutureDateWithToday fdwt = new FutureDateWithToday(yesterday.toDate());

        Assert.assertFalse(isValidAccordingToBeanValidation(fdwt));
    }
    
    @Test
    public void testsInTheFuture() throws Exception {
        DateTime tomorrow = new DateTime().plusDays(1);
        FutureDate fd = new FutureDate(tomorrow.toDate());

        Assert.assertTrue(isValidAccordingToBeanValidation(fd));
        
        FutureDateWithToday fdwt = new FutureDateWithToday(tomorrow.toDate());

        Assert.assertTrue(isValidAccordingToBeanValidation(fdwt));
    }

    @Data
    @AllArgsConstructor
    private class FutureDateWithToday {
        @Future(today = true)
        private Date date;
    }

    @Data
    @AllArgsConstructor
    private class FutureDate {
        @Future
        private Date date;
    }
    
    private boolean isValidAccordingToBeanValidation(final Object instance) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> errors = validator.validate(instance);

        for (ConstraintViolation<Object> error : errors) {
            System.out.println(error.getMessage());
        }
        return errors.isEmpty();
    }
}
