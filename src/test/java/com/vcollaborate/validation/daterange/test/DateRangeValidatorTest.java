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
package com.vcollaborate.validation.daterange.test;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.vcollaborate.validation.daterange.DateRange;
import com.vcollaborate.validation.daterange.DateRangeValidator;
import com.vcollaborate.validation.daterange.EndDate;
import com.vcollaborate.validation.daterange.StartDate;

/**
 * @author Christian Sterzl
 */
public class DateRangeValidatorTest {

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
	DateTime startDate = startDate();
	DateTime fiveDaysAfter = startDate.plusDays(5);

	StantardCaseDaysRangeEquals5 fiveDaysAfterCaseWhereIntervalEquals5 = new StantardCaseDaysRangeEquals5(startDate.toDate(),
		fiveDaysAfter.toDate());

	Assert.assertTrue(isValid(fiveDaysAfterCaseWhereIntervalEquals5));
	Assert.assertTrue(isValidAccordingToBeanValidation(fiveDaysAfterCaseWhereIntervalEquals5));
    }

    @Test
    public void shouldBeValidIfDateRangeIsGreaterThanChosenDateRange() throws Exception {
	DateTime startDate = startDate();
	DateTime sixDaysAfter = startDate.plusDays(6);

	StantardCaseDaysRangeEquals5 sixDaysAfterCaseWhereIntervalEquals5 = new StantardCaseDaysRangeEquals5(startDate.toDate(),
		sixDaysAfter.toDate());

	Assert.assertTrue(isValid(sixDaysAfterCaseWhereIntervalEquals5));
	Assert.assertTrue(isValidAccordingToBeanValidation(sixDaysAfterCaseWhereIntervalEquals5));
    }

    @Test
    public void shouldNotBeValidIfDateIntervalIsLessThanChosenDateInterval() throws Exception {
	DateTime startDate = startDate();
	DateTime threeDaysAfter = startDate.plusDays(3);

	StantardCaseDaysRangeEquals5 instanceIntervalEquals5 = new StantardCaseDaysRangeEquals5(startDate.toDate(), threeDaysAfter.toDate());

	Assert.assertFalse(isValid(instanceIntervalEquals5));
	Assert.assertFalse(isValidAccordingToBeanValidation(instanceIntervalEquals5));
    }

    @Test
    public void shouldBeValidIfDateIntervalIsGreaterThanOrEqualChosenDateIntervalForPairsOfDates() throws Exception {
	DateTime startDate = startDate();
	DateTime threeDaysAfter = startDate.plusDays(3);
	DateTime sixDaysAfter = startDate.plusDays(6);

	FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
		startDate.toDate(), threeDaysAfter.toDate(), startDate.toDate(), sixDaysAfter.toDate());

	Assert.assertTrue(isValid(twoValidPairsOfDates));
	Assert.assertTrue(isValidAccordingToBeanValidation(twoValidPairsOfDates));
    }

    @Test
    public void shouldNotBeValidIfDateIntervalIsSmallerThanChosenDateIntervalForOneOfThePairsOfDates() throws Exception {
	DateTime startDate = startDate();
	DateTime threeDaysAfter = startDate.plusDays(3);
	DateTime sixDaysBefore = startDate.minusDays(6);

	FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
		startDate.toDate(), threeDaysAfter.toDate(), startDate.toDate(), sixDaysBefore.toDate());

	Assert.assertFalse(isValid(twoValidPairsOfDates));
	Assert.assertFalse(isValidAccordingToBeanValidation(twoValidPairsOfDates));
    }

    @Test
    public void shouldBeValidIfDateIntervalIsValidFor3FieldsAnd2Ranges() throws Exception {
	DateTime startDateRangeOne = startDate();
	DateTime endDateRageOneStartDateRangeTwo = startDateRangeOne.plusDays(3);
	DateTime endDateRangeTwo = endDateRageOneStartDateRangeTwo.plusDays(2);

	ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
		startDateRangeOne.toDate(), endDateRageOneStartDateRangeTwo.toDate(), endDateRangeTwo.toDate());

	Assert.assertTrue(isValid(validThreeFieldsAndTwoRangesInstance));
	Assert.assertTrue(isValidAccordingToBeanValidation(validThreeFieldsAndTwoRangesInstance));
    }

    @Test
    public void shouldNotBeValidIfOnDateIntervalIsInvalidFor3FieldsAnd2Ranges() throws Exception {
	DateTime startDateRangeOne = startDate();
	DateTime endDateRageOneStartDateRangeTwo = startDateRangeOne.plusDays(3);
	DateTime endDateRangeTwo = endDateRageOneStartDateRangeTwo.minusDays(1);

	ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
		startDateRangeOne.toDate(), endDateRageOneStartDateRangeTwo.toDate(), endDateRangeTwo.toDate());

	Assert.assertFalse(isValid(validThreeFieldsAndTwoRangesInstance));
	Assert.assertFalse(isValidAccordingToBeanValidation(validThreeFieldsAndTwoRangesInstance));
    }

    @Test
    public void shouldBeValidIfIdFromStartDateAndEndDateAnnotationsUsageIsWrong() throws Exception {
	DateTime startDateRangeOne = startDate();
	DateTime endDateRageOneStartDateRangeTwo = startDateRangeOne.plusDays(3);
	DateTime endDateRangeTwo = endDateRageOneStartDateRangeTwo.minusDays(6);

	TwoDateIntervalsThreeFieldsWrongUsage threeFieldsAndTwoRangesWrongUsageInstance = new TwoDateIntervalsThreeFieldsWrongUsage(
		startDateRangeOne.toDate(), endDateRageOneStartDateRangeTwo.toDate(), endDateRangeTwo.toDate());

	Assert.assertTrue(isValid(threeFieldsAndTwoRangesWrongUsageInstance));
	Assert.assertTrue(isValidAccordingToBeanValidation(threeFieldsAndTwoRangesWrongUsageInstance));
    }

    @DateRange
    class NoEndDateCase {
	@StartDate
	Date data;
    }

    @SuppressWarnings("unused")
    @DateRange
    private class StantardCaseDaysRangeEquals5 {
	@StartDate
	Date starDate;

	@EndDate(minimumDaysRange = 5)
	Date endDate;

	public StantardCaseDaysRangeEquals5(Date starDate, Date endDate) {
	    this.starDate = starDate;
	    this.endDate = endDate;
	}
    }

    @SuppressWarnings("unused")
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

	public FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(Date firstPairDate1, Date firstPairDate2, Date secondPairDate1,
		Date secondPairDate2) {

	    this.firstRangeStartDate = firstPairDate1;
	    this.firstRangeEndDate = firstPairDate2;
	    this.secondPairDate1 = secondPairDate1;
	    this.secondPairDate2 = secondPairDate2;
	}
    }

    @SuppressWarnings("unused")
    @DateRange
    private class ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum {
	@StartDate
	private Date startDateRangeOne;

	@EndDate(minimumDaysRange = 2)
	@StartDate(id = 2)
	private Date endDateRangeOneAndStartDateRangeTwo;

	@EndDate(minimumDaysRange = 1, id = 2)
	private Date endDateRangeTwo;

	public ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(Date date1, Date date2, Date date3) {
	    this.startDateRangeOne = date1;
	    this.endDateRangeOneAndStartDateRangeTwo = date2;
	    this.endDateRangeTwo = date3;
	}
    }

    @SuppressWarnings("unused")
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

    private boolean isValid(Object instance) {
	return new DateRangeValidator().isValid(instance, null);
    }

    private DateTime startDate() {
	return new DateTime(2011, 1, 27, 0, 0, 0);
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
