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
        
package com.vcollaborate.validation.constraints.nested;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.junit.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.vcollaborate.validation.constraints.daterange.DateRange;
import com.vcollaborate.validation.constraints.daterange.EndDate;
import com.vcollaborate.validation.constraints.daterange.StartDate;
import com.vcollaborate.validation.constraints.nested.Nested;

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
  @SuppressWarnings("deprecation")
  private class ClassWithNestedDateRange {

    @NotNull
    @Nested(value = NestedDateRange.class, message = "{invalid.daterange}")
    @lombok.Delegate
    private NestedDateRange nestedDateRange = new NestedDateRange();
  }

  @Data
  @SuppressWarnings("deprecation")
  private class ClassWithListOfNestedDateRanges {

    @NotNull
    @Nested(value = NestedDateRange.class, message = "{invalid.daterange}")
    @lombok.Delegate
    private List<NestedDateRange> nestedDateRangeList = new ArrayList<NestedDateRange>();
  }

  @Data
  @SuppressWarnings("deprecation")
  private class ClassWithNestedDateRange2 {

    @Nested(value = NestedDateRangeNullAllowed.class, message = "{invalid.daterange}")
    @lombok.Delegate
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

    Set<ConstraintViolation<ClassWithNestedDateRange>> errors = validator.validateProperty(
        instance, "nestedDateRange");

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

    Set<ConstraintViolation<ClassWithNestedDateRange>> errors = validator.validateProperty(
        instance, "nestedDateRange");

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

    Set<ConstraintViolation<ClassWithNestedDateRange2>> errors = validator.validateProperty(
        instance, "nestedDateRange");

    for (ConstraintViolation<ClassWithNestedDateRange2> error : errors) {
      log.info("{}: {}", error.getPropertyPath(), error.getMessage());
    }

    Assert.assertTrue(errors.isEmpty());
  }

  /**
   * This tests fills a list of objects to validate. The first 5 are valid, the next 5 are invalid.
   * After the first invalid object the validation stops and returns false. The error list has thus
   * a size of 1.
   */
  @Test
  public void testNestedList() {
    ClassWithListOfNestedDateRanges instance = new ClassWithListOfNestedDateRanges();

    DateTime dt = new DateTime();
    for (int i = 0; i < 5; i++) {
      NestedDateRange nestedDateRange = new NestedDateRange();
      nestedDateRange.setBegin(dt.plusDays(10).toDate());
      nestedDateRange.setEnd(dt.plusDays(10 + 10).toDate());

      instance.add(nestedDateRange);
    }

    Set<ConstraintViolation<ClassWithListOfNestedDateRanges>> errors1 = validator.validateProperty(
        instance, "nestedDateRangeList");

    for (ConstraintViolation<ClassWithListOfNestedDateRanges> error : errors1) {
      log.info("{}: {}", error.getPropertyPath(), error.getMessage());
    }

    Assert.assertTrue(errors1.isEmpty());
    Assert.assertTrue(errors1.size() == 0);

    for (int i = 0; i < 5; i++) {
      NestedDateRange nestedDateRange = new NestedDateRange();
      nestedDateRange.setBegin(dt.plusDays(10).toDate());
      nestedDateRange.setEnd(dt.plusDays(10 + 5).toDate());

      instance.add(nestedDateRange);
    }

    Set<ConstraintViolation<ClassWithListOfNestedDateRanges>> errors2 = validator.validateProperty(
        instance, "nestedDateRangeList");

    for (ConstraintViolation<ClassWithListOfNestedDateRanges> error : errors2) {
      log.info("{}: {}", error.getPropertyPath(), error.getMessage());
    }

    Assert.assertFalse(errors2.isEmpty());
    Assert.assertTrue(errors2.size() == 1);
  }

}
