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
package com.vcollaborate.validation.constraints.allowedvalues;

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

import com.vcollaborate.validation.constraints.allowedvalues.AllowedStrings;

@Slf4j
public class AllowedStringsValidatorTest {

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
    private class ClassWithAllowedStrings {

        @AllowedStrings({ "a", "b", "c" })
        private String value;
    }

    @Data
    private class ClassWithAllowedStringsNotNull {

        @NotNull
        @AllowedStrings(value={ "a", "b", "c" }, nullAllowed = false)
        private String value;
    }

    @Test
    public void testAllowedValue() {
        log.info("testAllowedValue");
        ClassWithAllowedStrings instance = new ClassWithAllowedStrings();
        instance.setValue("a");

        Set<ConstraintViolation<ClassWithAllowedStrings>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithAllowedStrings> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testAllowedNullValue() {
        log.info("testAllowedNullValue");
        ClassWithAllowedStrings instance = new ClassWithAllowedStrings();
        instance.setValue(null);

        Set<ConstraintViolation<ClassWithAllowedStrings>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithAllowedStrings> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testNotAllowedNullValue() {
        log.info("testNotAllowedNullValue");
        ClassWithAllowedStringsNotNull instance = new ClassWithAllowedStringsNotNull();
        instance.setValue(null);

        Set<ConstraintViolation<ClassWithAllowedStringsNotNull>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithAllowedStringsNotNull> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertFalse(errors.isEmpty());
    }

    @Test
    public void testNotAllowedValue() {
        log.info("testNotAllowedValue");
        ClassWithAllowedStrings instance = new ClassWithAllowedStrings();
        instance.setValue("d");

        Set<ConstraintViolation<ClassWithAllowedStrings>> errors = validator.validate(instance);

        for (ConstraintViolation<ClassWithAllowedStrings> error : errors) {
            log.info("{}: {}", error.getPropertyPath(), error.getMessage());
        }

        Assert.assertFalse(errors.isEmpty());
    }

}
