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

package com.vcollaborate.validation.constraints.daterange;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class DateRangeValidatorTest {

  private static DateTime[] datesToTest = { new DateTime(2011, 1, 27, 0, 0, 0, 0),
      new DateTime(2011, 3, 25, 0, 0, 0, 0), new DateTime(2011, 10, 28, 0, 0, 0, 0) };

  @Test
  public void shouldBeValidIfUsageIsWrong() throws Exception {
    NoEndDateCase wrongUsageInstance1 = new NoEndDateCase();

    Assert.assertTrue(isValid(wrongUsageInstance1));
    Assert.assertTrue(isValidAccordingToBeanValidation(wrongUsageInstance1));

    NoStartDateCase wrongUsageInstance2 = new NoStartDateCase();

    Assert.assertTrue(isValid(wrongUsageInstance2));
    Assert.assertTrue(isValidAccordingToBeanValidation(wrongUsageInstance2));
  }

  @Test
  public void shouldBeValidIfFieldValuesAreNull() throws Exception {
    StantardCaseDaysRangeEquals5 nullValuesInstance = new StantardCaseDaysRangeEquals5(null, null);

    Assert.assertTrue(isValid(nullValuesInstance));
    Assert.assertTrue(isValidAccordingToBeanValidation(nullValuesInstance));

    StantardCaseDaysRangeEquals5 standartCaseWithEndDateNull = new StantardCaseDaysRangeEquals5(
        datesToTest[0].toDate(), null);

    Assert.assertTrue(isValid(standartCaseWithEndDateNull));
    Assert.assertTrue(isValidAccordingToBeanValidation(standartCaseWithEndDateNull));

    StantardCaseDaysRangeEquals5 standartCaseWithStartDateNull = new StantardCaseDaysRangeEquals5(
        null, datesToTest[0].toDate());

    Assert.assertTrue(isValid(standartCaseWithStartDateNull));
    Assert.assertTrue(isValidAccordingToBeanValidation(standartCaseWithStartDateNull));
  }

  @Test
  public void shouldBeValidIfDateRangeIsEqualChosenDateRange() throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime fiveDaysAfter = startDate.plusDays(5);

      StantardCaseDaysRangeEquals5 fiveDaysAfterInterval5 = new StantardCaseDaysRangeEquals5(
          startDate.toDate(), fiveDaysAfter.toDate());

      Assert.assertTrue(isValid(fiveDaysAfterInterval5));
      Assert.assertTrue(isValidAccordingToBeanValidation(fiveDaysAfterInterval5));
    }
  }

  @Test
  public void shouldBeValidIfDateRangeIsEqualChosenDateRangeWithTime() throws Exception {
    // This should be possible, because these are 10 days.
    DateTime startDate = new DateTime(2012, 11, 18, 0, 0, 0, 0);
    DateTime endDate = new DateTime(2012, 11, 27, 23, 59, 59, 0);

    StantardCaseDaysRangeEquals10 daterange = new StantardCaseDaysRangeEquals10(startDate.toDate(),
        endDate.toDate());

    Assert.assertTrue(isValid(daterange));
    Assert.assertTrue(isValidAccordingToBeanValidation(daterange));
  }

  @Test
  public void shouldBeValidIfDateRangeIsGreaterThanChosenDateRange() throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime sixDaysAfter = startDate.plusDays(6);

      StantardCaseDaysRangeEquals5 sixDaysAfterInterval5 = new StantardCaseDaysRangeEquals5(
          startDate.toDate(), sixDaysAfter.toDate());

      Assert.assertTrue(isValid(sixDaysAfterInterval5));
      Assert.assertTrue(isValidAccordingToBeanValidation(sixDaysAfterInterval5));
    }
  }

  @Test
  public void shouldNotBeValidIfDateIntervalIsLessThanChosenDateInterval() throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime threeDaysAfter = startDate.plusDays(3);

      StantardCaseDaysRangeEquals5 instanceIntervalEquals5 = new StantardCaseDaysRangeEquals5(
          startDate.toDate(), threeDaysAfter.toDate());

      Assert.assertFalse(isValid(instanceIntervalEquals5));
      Assert.assertFalse(isValidAccordingToBeanValidation(instanceIntervalEquals5));
    }
  }

  @Test
  public void shouldBeValidIfDateIntervalIsGreaterThanOrEqualChosenDateIntervalForPairsOfDates()
      throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime threeDaysAfter = startDate.plusDays(3);
      DateTime sixDaysAfter = startDate.plusDays(6);

      FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
          startDate.toDate(), threeDaysAfter.toDate(), startDate.toDate(), sixDaysAfter.toDate());

      Assert.assertTrue(isValid(twoValidPairsOfDates));
      Assert.assertTrue(isValidAccordingToBeanValidation(twoValidPairsOfDates));
    }
  }

  @Test
  public void shouldNotBeValidIfDateIntervalIsSmallerThanChosenDateIntervalForOneOfThePairsOfDates()
      throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime threeDaysAfter = startDate.plusDays(3);
      DateTime sixDaysBefore = startDate.minusDays(6);

      FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
          startDate.toDate(), threeDaysAfter.toDate(), startDate.toDate(), sixDaysBefore.toDate());

      Assert.assertFalse(isValid(twoValidPairsOfDates));
      Assert.assertFalse(isValidAccordingToBeanValidation(twoValidPairsOfDates));
    }
  }

  @Test
  public void shouldBeValidIfDateIntervalIsValidFor3FieldsAnd2Ranges() throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDateRageOneStartDateRangeTwo = startDate.plusDays(3);
      DateTime endDateRangeTwo = endDateRageOneStartDateRangeTwo.plusDays(2);

      ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
          startDate.toDate(), endDateRageOneStartDateRangeTwo.toDate(), endDateRangeTwo.toDate());

      Assert.assertTrue(isValid(validThreeFieldsAndTwoRangesInstance));
      Assert.assertTrue(isValidAccordingToBeanValidation(validThreeFieldsAndTwoRangesInstance));
    }
  }

  @Test
  public void shouldNotBeValidIfOnDateIntervalIsInvalidFor3FieldsAnd2Ranges() throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDateRageOneStartDateRangeTwo = startDate.plusDays(3);
      DateTime endDateRangeTwo = endDateRageOneStartDateRangeTwo.minusDays(1);

      ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
          startDate.toDate(), endDateRageOneStartDateRangeTwo.toDate(), endDateRangeTwo.toDate());

      Assert.assertFalse(isValid(validThreeFieldsAndTwoRangesInstance));
      Assert.assertFalse(isValidAccordingToBeanValidation(validThreeFieldsAndTwoRangesInstance));
    }
  }

  @Test
  public void shouldBeValidIfIdFromStartDateAndEndDateAnnotationsUsageIsWrong() throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDateRangeOneStartDateRangeTwo = startDate.plusDays(3);
      DateTime endDateRangeTwo = endDateRangeOneStartDateRangeTwo.minusDays(6);

      TwoDateIntervalsThreeFieldsWrongUsage threeFieldsAndTwoRangesWrongUsageInstance = new TwoDateIntervalsThreeFieldsWrongUsage(
          startDate.toDate(), endDateRangeOneStartDateRangeTwo.toDate(), endDateRangeTwo.toDate());

      Assert.assertTrue(isValid(threeFieldsAndTwoRangesWrongUsageInstance));
      Assert
          .assertTrue(isValidAccordingToBeanValidation(threeFieldsAndTwoRangesWrongUsageInstance));
    }
  }

  @Test
  public void shouldBeValidAllowedRanges() throws Exception {
    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDate = startDate.plusDays(10);

      AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(
          startDate.toDate(), endDate.toDate());
      AllowedIntervalsStandardCase2 allowedIntervalsStandardCase2 = new AllowedIntervalsStandardCase2(
          startDate.toDate(), endDate.toDate());

      Assert.assertTrue(isValid(allowedIntervalsStandardCase));
      Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));

      Assert.assertTrue(isValid(allowedIntervalsStandardCase2));
      Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase2));
    }

    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDate = startDate.plusDays(15);

      AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(
          startDate.toDate(), endDate.toDate());
      AllowedIntervalsStandardCase2 allowedIntervalsStandardCase2 = new AllowedIntervalsStandardCase2(
          startDate.toDate(), endDate.toDate());

      Assert.assertTrue(isValid(allowedIntervalsStandardCase));
      Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));

      Assert.assertTrue(isValid(allowedIntervalsStandardCase2));
      Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase2));
    }

    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDate = startDate.plusDays(20);

      AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(
          startDate.toDate(), endDate.toDate());
      AllowedIntervalsStandardCase2 allowedIntervalsStandardCase2 = new AllowedIntervalsStandardCase2(
          startDate.toDate(), endDate.toDate());

      Assert.assertTrue(isValid(allowedIntervalsStandardCase));
      Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));

      Assert.assertTrue(isValid(allowedIntervalsStandardCase2));
      Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase2));
    }

    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDate = startDate.plusDays(25);

      AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(
          startDate.toDate(), endDate.toDate());
      AllowedIntervalsStandardCase2 allowedIntervalsStandardCase2 = new AllowedIntervalsStandardCase2(
          startDate.toDate(), endDate.toDate());

      Assert.assertFalse(isValid(allowedIntervalsStandardCase));
      Assert.assertFalse(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));

      Assert.assertFalse(isValid(allowedIntervalsStandardCase2));
      Assert.assertFalse(isValidAccordingToBeanValidation(allowedIntervalsStandardCase2));
    }

    for (int i = 0; i < datesToTest.length; i++) {
      DateTime startDate = datesToTest[i];
      DateTime endDate = startDate.plusDays(-5);

      AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(
          startDate.toDate(), endDate.toDate());
      AllowedIntervalsStandardCase2 allowedIntervalsStandardCase2 = new AllowedIntervalsStandardCase2(
          startDate.toDate(), endDate.toDate());

      Assert.assertFalse(isValid(allowedIntervalsStandardCase));
      Assert.assertFalse(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));

      Assert.assertFalse(isValid(allowedIntervalsStandardCase2));
      Assert.assertFalse(isValidAccordingToBeanValidation(allowedIntervalsStandardCase2));
    }
  }
  
  @Test
  public void allDefaultDateCase() throws Exception {
    AllDefaultDateCase testDatecase = new AllDefaultDateCase();
    testDatecase.startdata = new Date();
    testDatecase.enddata = testDatecase.startdata;

    Assert.assertTrue(isValid(testDatecase));
    Assert.assertTrue(isValidAccordingToBeanValidation(testDatecase));

    AllDefaultDateCase testDatecase2 = new AllDefaultDateCase();
    DateTime startDate =  new DateTime(2011, 1, 27, 0, 0, 0, 0);
    DateTime endDate = new DateTime(2011, 1, 27, 0, 30, 0, 0);

    testDatecase2.startdata = startDate.toDate();
    testDatecase2.enddata = endDate.toDate();

    Assert.assertTrue(isValid(testDatecase2));
    Assert.assertTrue(isValidAccordingToBeanValidation(testDatecase2));
    
    
    AllDefaultDateCase testDatecase3 = new AllDefaultDateCase();

    testDatecase3.startdata = new DateTime(2011, 1, 27, 0, 0, 0, 0).toDate();
    testDatecase3.enddata = new DateTime(2011, 1, 26, 0, 0, 0, 0).toDate();

    Assert.assertFalse(isValid(testDatecase3));
    Assert.assertFalse(isValidAccordingToBeanValidation(testDatecase3));
    
    
    AllDefaultDateCase testDatecase4 = new AllDefaultDateCase();

    testDatecase4.startdata = new DateTime(2011, 1, 27, 4, 0, 0, 0).toDate();
    testDatecase4.enddata = new DateTime(2011, 1, 27, 3, 0, 0, 0).toDate();

    Assert.assertFalse(isValid(testDatecase4));
    Assert.assertFalse(isValidAccordingToBeanValidation(testDatecase4));
  }

  @Test
  public void allZonedDateTimeCase() throws Exception {
	  ZonedDateTime start1 = ZonedDateTime.of(2011, 1, 10, 0, 0, 0, 0, ZoneId.systemDefault());
	  ZonedDateTime end1 = ZonedDateTime.of(2011, 1, 20, 0, 0, 0, 0, ZoneId.systemDefault());
	  ZonedDateTime end2 = ZonedDateTime.of(2011, 1, 11, 0, 0, 0, 0, ZoneId.systemDefault());

	  AllowedIntervalsZonedDateTime testDate1 = new AllowedIntervalsZonedDateTime(start1, end1);
	  Assert.assertTrue(isValid(testDate1));
	  
	  AllowedIntervalsZonedDateTime testDate2 = new AllowedIntervalsZonedDateTime(start1, end2);
	  Assert.assertFalse(isValid(testDate2));
  }

  @DateRange
  class NoEndDateCase {
    @StartDate
    Date data;
  }

  @DateRange
  class NoStartDateCase {
    @EndDate
    Date data;
  }

  @DateRange
  class AllDefaultDateCase {
    @StartDate
    Date startdata;
    @EndDate(minimumDaysRange = 0)
    Date enddata;
  }


  @DateRange
  private class StantardCaseDaysRangeEquals5 {
    @StartDate
    Date startDate;

    @EndDate(minimumDaysRange = 5)
    Date endDate;

    public StantardCaseDaysRangeEquals5(Date startDate, Date endDate) {
      this.startDate = startDate;
      this.endDate = endDate;
    }
  }

  @DateRange
  private class StantardCaseDaysRangeEquals10 {
    @StartDate
    Date startDate;

    @EndDate(minimumDaysRange = 10)
    Date endDate;

    public StantardCaseDaysRangeEquals10(Date startDate, Date endDate) {
      this.startDate = startDate;
      this.endDate = endDate;
    }
  }

  @DateRange
  private class FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum {
    @StartDate
    private Date firstRangeStartDate;

    @EndDate(minimumDaysRange = 3)
    private Date firstRangeEndDate;

    @StartDate(id = 1)
    private Date secondPairDate1;

    @EndDate(minimumDaysRange = 2, id = 1)
    private Date secondPairDate2;

    public FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(Date firstPairDate1,
        Date firstPairDate2, Date secondPairDate1, Date secondPairDate2) {

      this.firstRangeStartDate = firstPairDate1;
      this.firstRangeEndDate = firstPairDate2;
      this.secondPairDate1 = secondPairDate1;
      this.secondPairDate2 = secondPairDate2;
    }
  }

  @DateRange
  private class ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum {
    @StartDate
    private Date startDateRangeOne;

    @EndDate(minimumDaysRange = 2)
    @StartDate(id = 2)
    private Date endDateRangeOneAndStartDateRangeTwo;

    @EndDate(minimumDaysRange = 1, id = 2)
    private Date endDateRangeTwo;

    public ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(Date date1, Date date2,
        Date date3) {
      this.startDateRangeOne = date1;
      this.endDateRangeOneAndStartDateRangeTwo = date2;
      this.endDateRangeTwo = date3;
    }
  }

  @DateRange
  private class TwoDateIntervalsThreeFieldsWrongUsage {
    @StartDate
    private Date startDateRangeOne;

    @EndDate(minimumDaysRange = 2)
    @StartDate(id = 2)
    private Date endDateRangeOneAndStartDateRangeTwo;

    @EndDate(minimumDaysRange = 1)
    private Date endDateRangeTwo;

    public TwoDateIntervalsThreeFieldsWrongUsage(Date date1, Date date2, Date date3) {
      this.startDateRangeOne = date1;
      this.endDateRangeOneAndStartDateRangeTwo = date2;
      this.endDateRangeTwo = date3;
    }
  }

  @DateRange
  private class AllowedIntervalsStandardCase {
    @StartDate
    private Date startDate;

    @EndDate(allowedDayRanges = { 10, 15, 20 })
    private Date endDate;

    public AllowedIntervalsStandardCase(Date date1, Date date2) {
      this.startDate = date1;
      this.endDate = date2;
    }
  }

  @DateRange
  private class AllowedIntervalsStandardCase2 {
    @StartDate
    private Date startDate;

    @EndDate(allowedDayRanges = { 10, 15, 20 }, minimumDaysRange = 25)
    private Date endDate;

    public AllowedIntervalsStandardCase2(Date date1, Date date2) {
      this.startDate = date1;
      this.endDate = date2;
    }
  }
  
  @DateRange
  private class AllowedIntervalsZonedDateTime {
    @StartDate
    private ZonedDateTime startDate;

    @EndDate(allowedDayRanges = { 10, 15, 20 }, minimumDaysRange = 25)
    private ZonedDateTime endDate;

    public AllowedIntervalsZonedDateTime(ZonedDateTime date1, ZonedDateTime date2) {
      this.startDate = date1;
      this.endDate = date2;
    }
  }  

  private boolean isValid(Object instance) {
    return new DateRangeValidator().isValid(instance, null);
  }

  // For integration tests:
  private boolean isValidAccordingToBeanValidation(Object instance) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Object>> errors = validator.validate(instance);

    for (ConstraintViolation<Object> error : errors) {
      System.out.println(error.getMessage());
    }
    return errors.isEmpty();
  }
}
