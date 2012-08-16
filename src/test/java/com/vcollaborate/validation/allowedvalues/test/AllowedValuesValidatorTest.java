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
package com.vcollaborate.validation.allowedvalues.test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import junit.framework.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import com.vcollaborate.validation.allowedvalues.AllowedIntegers;

@Slf4j
public class AllowedValuesValidatorTest {

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
    private class ClassWithAllowedIntegers {

        @NotNull
        @AllowedIntegers({0,10,20})
        private int value;
    }

    @Test
    public void testAllowedValue() {
        log.info("testAllowedValue");
        ClassWithAllowedIntegers instance = new ClassWithAllowedIntegers();
        instance.setValue(0);

        Set<ConstraintViolation<ClassWithAllowedIntegers>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithAllowedIntegers> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testNotAllowedValue() {
        log.info("testNotAllowedValue");
        ClassWithAllowedIntegers instance = new ClassWithAllowedIntegers();
        instance.setValue(5);

        Set<ConstraintViolation<ClassWithAllowedIntegers>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithAllowedIntegers> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertFalse(errors.isEmpty());
    }

}
