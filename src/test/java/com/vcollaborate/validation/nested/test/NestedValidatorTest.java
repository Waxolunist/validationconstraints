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
package com.vcollaborate.validation.nested.test;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import junit.framework.Assert;
import lombok.Data;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.vcollaborate.validation.daterange.DateRange;
import com.vcollaborate.validation.daterange.EndDate;
import com.vcollaborate.validation.daterange.StartDate;
import com.vcollaborate.validation.nested.Nested;

@Slf4j
public class NestedValidatorTest {

    private ValidatorFactory factory;
    private Validator validator;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Data
    @SuppressWarnings("unused")
    private class ClassWithNestedDateRange {

        @NotNull
        @Nested(value = NestedDateRange.class, message = "{invalid.daterange}")
        @Delegate
        private NestedDateRange nestedDateRange = new NestedDateRange();
    }

    @Data
    @SuppressWarnings("unused")
    private class ClassWithNestedDateRange2 {

        @Nested(value = NestedDateRangeNullAllowed.class, message = "{invalid.daterange}")
        @Delegate
        private NestedDateRangeNullAllowed nestedDateRange = new NestedDateRangeNullAllowed();
    }

    @Data
    @DateRange
    private class NestedDateRange {
        @Future
        @NotNull
        @StartDate
        private Date begin;

        @Future
        @NotNull
        @EndDate(minimumDaysRange = 10)
        private Date end;
    }

    @Data
    @DateRange
    private class NestedDateRangeNullAllowed {
        @Future
        @StartDate
        private Date begin;

        @Future
        @EndDate(minimumDaysRange = 10)
        private Date end;
    }

    @Test
    public void testToShortRange() {
        log.info("testToShortRange");
        DateTime dt = new DateTime();
        ClassWithNestedDateRange instance = new ClassWithNestedDateRange();
        instance.setBegin(dt.plusDays(10).toDate());
        instance.setEnd(dt.plusDays(10 - 5).toDate());

        Set<ConstraintViolation<ClassWithNestedDateRange>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithNestedDateRange> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertFalse(errors.isEmpty());
    }

    @Test
    public void testExactRange() {
        log.info("testExactRange");
        DateTime dt = new DateTime();
        ClassWithNestedDateRange instance = new ClassWithNestedDateRange();
        instance.setBegin(dt.plusDays(10).toDate());
        instance.setEnd(dt.plusDays(10 + 10).toDate());

        Set<ConstraintViolation<ClassWithNestedDateRange>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithNestedDateRange> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());

        }

        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testExactRangeFieldsOnly() {
        log.info("testExactRangeFieldsOnly");
        DateTime dt = new DateTime();
        ClassWithNestedDateRange instance = new ClassWithNestedDateRange();
        instance.setBegin(dt.plusDays(10).toDate());
        instance.setEnd(dt.plusDays(10 + 9).toDate());

        Set<ConstraintViolation<ClassWithNestedDateRange>> errors = validator.validateProperty(instance, "nestedDateRange");

        for (ConstraintViolation<ClassWithNestedDateRange> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertFalse(errors.isEmpty());
    }

    @Test
    public void testNestedNullValue() {
        log.info("testNestedNullValue");
        ClassWithNestedDateRange instance = new ClassWithNestedDateRange();
        Assert.assertNotNull(instance.getNestedDateRange());

        Set<ConstraintViolation<ClassWithNestedDateRange>> errors = validator.validateProperty(instance, "nestedDateRange");

        for (ConstraintViolation<ClassWithNestedDateRange> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertFalse(errors.isEmpty());
    }

    @Test
    public void testNestedNullValue2() {
        log.info("testNestedNullValue2");
        ClassWithNestedDateRange2 instance = new ClassWithNestedDateRange2();
        Assert.assertNotNull(instance.getNestedDateRange());

        Set<ConstraintViolation<ClassWithNestedDateRange2>> errors = validator.validateProperty(instance, "nestedDateRange");

        for (ConstraintViolation<ClassWithNestedDateRange2> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertTrue(errors.isEmpty());
    }

}
