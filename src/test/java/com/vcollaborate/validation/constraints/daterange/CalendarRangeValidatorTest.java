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
package com.vcollaborate.validation.constraints.daterange;

import java.util.Calendar;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import junit.framework.Assert;

import org.junit.Test;

import com.vcollaborate.validation.constraints.daterange.DateRange;
import com.vcollaborate.validation.constraints.daterange.DateRangeValidator;
import com.vcollaborate.validation.constraints.daterange.EndDate;
import com.vcollaborate.validation.constraints.daterange.StartDate;

/**
 * @author Christian Sterzl
 */
public class CalendarRangeValidatorTest {

    private static Calendar getCalendar(int year, int month, int day) {
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.set(year, month, day);
        return dataInicio;
    }

    private static Calendar[] datesToTest = { getCalendar(2011, 1, 27), getCalendar(2011, 3, 25),
            getCalendar(2011, 10, 28) };

    @Test
    public void shouldBeValidIfUsageIsWrong() throws Exception {
        NoEndDateCase wrongUsageInstance = new NoEndDateCase();

        Assert.assertTrue(isValid(wrongUsageInstance));
        Assert.assertTrue(isValidAccordingToBeanValidation(wrongUsageInstance));
    }

    @Test
    public void shouldBeValidIfFieldValuesAreNull() throws Exception {
        StantardCaseDaysRangeEquals5 standartCaseWithNullValuesInstance = new StantardCaseDaysRangeEquals5(null, null);

        Assert.assertTrue(isValid(standartCaseWithNullValuesInstance));
        Assert.assertTrue(isValidAccordingToBeanValidation(standartCaseWithNullValuesInstance));
    }

    @Test
    public void shouldBeValidIfDateRangeIsEqualChosenDateRange() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar fiveDaysAfter = daysAfter(startDate, 5);

            StantardCaseDaysRangeEquals5 fiveDaysAfterCaseWhereIntervalEquals5 = new StantardCaseDaysRangeEquals5(
                    startDate, fiveDaysAfter);

            Assert.assertTrue(isValid(fiveDaysAfterCaseWhereIntervalEquals5));
            Assert.assertTrue(isValidAccordingToBeanValidation(fiveDaysAfterCaseWhereIntervalEquals5));
        }
    }

    @Test
    public void shouldBeValidIfDateRangeIsGreaterThanChosenDateRange() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar sixDaysAfter = daysAfter(startDate, 6);

            StantardCaseDaysRangeEquals5 sixDaysAfterCaseWhereIntervalEquals5 = new StantardCaseDaysRangeEquals5(
                    startDate, sixDaysAfter);

            Assert.assertTrue(isValid(sixDaysAfterCaseWhereIntervalEquals5));
            Assert.assertTrue(isValidAccordingToBeanValidation(sixDaysAfterCaseWhereIntervalEquals5));
        }
    }

    @Test
    public void shouldNotBeValidIfDateIntervalIsLessThanChosenDateInterval() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar threeDaysAfter = daysAfter(startDate, 3);

            StantardCaseDaysRangeEquals5 instanceIntervalEquals5 = new StantardCaseDaysRangeEquals5(startDate,
                    threeDaysAfter);

            Assert.assertFalse(isValid(instanceIntervalEquals5));
            Assert.assertFalse(isValidAccordingToBeanValidation(instanceIntervalEquals5));
        }
    }

    @Test
    public void shouldBeValidIfDateIntervalIsGreaterThanOrEqualChosenDateIntervalForPairsOfDates() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar threeDaysAfter = daysAfter(startDate, 3);
            Calendar sixDaysAfter = daysAfter(startDate, 6);

            FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
                    startDate, threeDaysAfter, startDate, sixDaysAfter);

            Assert.assertTrue(isValid(twoValidPairsOfDates));
            Assert.assertTrue(isValidAccordingToBeanValidation(twoValidPairsOfDates));
        }
    }

    @Test
    public void shouldNotBeValidIfDateIntervalIsSmallerThanChosenDateIntervalForOneOfThePairsOfDates() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar threeDaysAfter = daysAfter(startDate, 3);
            Calendar sixDaysBefore = daysBefore(startDate, 6);

            FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
                    startDate, threeDaysAfter, startDate, sixDaysBefore);

            Assert.assertFalse(isValid(twoValidPairsOfDates));
            Assert.assertFalse(isValidAccordingToBeanValidation(twoValidPairsOfDates));
        }
    }

    @Test
    public void shouldBeValidIfDateIntervalIsValidFor3FieldsAnd2Ranges() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDateRageOneStartDateRangeTwo = daysAfter(startDate, 3);
            Calendar endDateRangeTwo = daysAfter(endDateRageOneStartDateRangeTwo, 2);

            ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
                    startDate, endDateRageOneStartDateRangeTwo, endDateRangeTwo);

            Assert.assertTrue(isValid(validThreeFieldsAndTwoRangesInstance));
            Assert.assertTrue(isValidAccordingToBeanValidation(validThreeFieldsAndTwoRangesInstance));
        }
    }

    @Test
    public void shouldNotBeValidIfOnDateIntervalIsInvalidFor3FieldsAnd2Ranges() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDateRageOneStartDateRangeTwo = daysAfter(startDate, 3);
            Calendar endDateRangeTwo = daysBefore(endDateRageOneStartDateRangeTwo, 1);

            ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
                    startDate, endDateRageOneStartDateRangeTwo, endDateRangeTwo);

            Assert.assertFalse(isValid(validThreeFieldsAndTwoRangesInstance));
            Assert.assertFalse(isValidAccordingToBeanValidation(validThreeFieldsAndTwoRangesInstance));
        }
    }

    @Test
    public void shouldBeValidIfIdFromStartDateAndEndDateAnnotationsUsageIsWrong() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDateRageOneStartDateRangeTwo = daysAfter(startDate, 3);
            Calendar endDateRangeTwo = daysBefore(endDateRageOneStartDateRangeTwo, 6);

            TwoDateIntervalsThreeFieldsWrongUsage threeFieldsAndTwoRangesWrongUsageInstance = new TwoDateIntervalsThreeFieldsWrongUsage(
                    startDate, endDateRageOneStartDateRangeTwo, endDateRangeTwo);

            Assert.assertTrue(isValid(threeFieldsAndTwoRangesWrongUsageInstance));
            Assert.assertTrue(isValidAccordingToBeanValidation(threeFieldsAndTwoRangesWrongUsageInstance));
        }
    }

    @Test
    public void shouldBeValidAllowedRanges() throws Exception {
        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDate = daysAfter(startDate, 10);

            AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(startDate,
                    endDate);

            Assert.assertTrue(isValid(allowedIntervalsStandardCase));
            Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));
        }

        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDate = daysAfter(startDate, 15);

            AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(startDate,
                    endDate);

            Assert.assertTrue(isValid(allowedIntervalsStandardCase));
            Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));
        }

        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDate = daysAfter(startDate, 20);

            AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(startDate,
                    endDate);

            Assert.assertTrue(isValid(allowedIntervalsStandardCase));
            Assert.assertTrue(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));
        }

        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDate = daysAfter(startDate, 25);

            AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(startDate,
                    endDate);

            Assert.assertFalse(isValid(allowedIntervalsStandardCase));
            Assert.assertFalse(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));
        }

        for (int i = 0; i < datesToTest.length; i++) {
            Calendar startDate = datesToTest[i];
            Calendar endDate = daysBefore(startDate, 10);

            AllowedIntervalsStandardCase allowedIntervalsStandardCase = new AllowedIntervalsStandardCase(startDate,
                    endDate);

            Assert.assertFalse(isValid(allowedIntervalsStandardCase));
            Assert.assertFalse(isValidAccordingToBeanValidation(allowedIntervalsStandardCase));
        }
    }

    @DateRange
    class NoEndDateCase {
        @StartDate
        Calendar data;
    }

    @DateRange
    private class StantardCaseDaysRangeEquals5 {
        @StartDate
        Calendar starDate;

        @EndDate(minimumDaysRange = 5)
        Calendar endDate;

        public StantardCaseDaysRangeEquals5(Calendar starDate, Calendar endDate) {
            this.starDate = starDate;
            this.endDate = endDate;
        }
    }

    @DateRange
    private class FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum {
        @StartDate
        private Calendar firstRangeStartDate;

        @EndDate(minimumDaysRange = 3)
        private Calendar firstRangeEndDate;

        @StartDate(id = 1)
        private Calendar secondPairDate1;

        @EndDate(minimumDaysRange = 2, id = 1)
        private Calendar secondPairDate2;

        public FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(Calendar firstPairDate1,
                Calendar firstPairDate2, Calendar secondPairDate1, Calendar secondPairDate2) {

            this.firstRangeStartDate = firstPairDate1;
            this.firstRangeEndDate = firstPairDate2;
            this.secondPairDate1 = secondPairDate1;
            this.secondPairDate2 = secondPairDate2;
        }
    }

    @DateRange
    private class ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum {
        @StartDate
        private Calendar startDateRangeOne;

        @EndDate(minimumDaysRange = 2)
        @StartDate(id = 2)
        private Calendar endDateRangeOneAndStartDateRangeTwo;

        @EndDate(minimumDaysRange = 1, id = 2)
        private Calendar endDateRangeTwo;

        public ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(Calendar date1, Calendar date2, Calendar date3) {
            this.startDateRangeOne = date1;
            this.endDateRangeOneAndStartDateRangeTwo = date2;
            this.endDateRangeTwo = date3;
        }
    }

    @DateRange
    private class TwoDateIntervalsThreeFieldsWrongUsage {
        @StartDate
        private Calendar startDateRangeOne;

        @EndDate(minimumDaysRange = 2)
        @StartDate(id = 2)
        private Calendar endDateRangeOneAndStartDateRangeTwo;

        @EndDate(minimumDaysRange = 1)
        private Calendar endDateRangeTwo;

        public TwoDateIntervalsThreeFieldsWrongUsage(Calendar date1, Calendar date2, Calendar date3) {
            this.startDateRangeOne = date1;
            this.endDateRangeOneAndStartDateRangeTwo = date2;
            this.endDateRangeTwo = date3;
        }
    }

    @DateRange
    private class AllowedIntervalsStandardCase {
        @StartDate
        private Calendar startDate;

        @EndDate(allowedDayRanges = { 10, 15, 20 })
        private Calendar endDate;

        public AllowedIntervalsStandardCase(Calendar date1, Calendar date2) {
            this.startDate = date1;
            this.endDate = date2;
        }
    }

    private boolean isValid(Object instance) {
        return new DateRangeValidator().isValid(instance, null);
    }

    private Calendar daysBefore(Calendar date, int days) {
        return daysAfter(date, -days);
    }

    private Calendar daysAfter(Calendar date, int days) {
        Calendar dateAfter = (Calendar) date.clone();
        dateAfter.add(Calendar.DAY_OF_MONTH, days);
        return dateAfter;
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
