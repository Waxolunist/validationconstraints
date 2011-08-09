package br.com.caelum.validation;

import java.util.Calendar;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import junit.framework.Assert;

import org.junit.Test;

import br.com.caelum.validation.DateRange;
import br.com.caelum.validation.DateRangeValidator;
import br.com.caelum.validation.EndDate;
import br.com.caelum.validation.StartDate;

public class DateRangeValidatorTest {

	@Test
	public void shouldBeValidIfUsageIsWrong() throws Exception {
		NoEndDateCase wrongUsageInstance = new NoEndDateCase();

		Assert.assertTrue(isValid(wrongUsageInstance));
	}

	@Test
	public void shouldBeValidIfFieldValuesAreNull() throws Exception {
		StantardCaseDaysRangeEquals5 standartCaseWithNullValuesInstance =
			new StantardCaseDaysRangeEquals5(null, null);

		Assert.assertTrue(isValid(standartCaseWithNullValuesInstance));
	}

	@Test
	public void shouldBeValidIfDateRangeIsEqualChosenDateRange()
			throws Exception {
		Calendar startDate = startDate();
		Calendar fiveDaysAfter = daysAfter(startDate, 5);

		StantardCaseDaysRangeEquals5 fiveDaysAfterCaseWhereIntervalEquals5 = 
			new StantardCaseDaysRangeEquals5(startDate, fiveDaysAfter);

		Assert.assertTrue(isValid(fiveDaysAfterCaseWhereIntervalEquals5));
	}
	
	@Test
	public void shouldBeValidIfDateRangeIsGreaterThanChosenDateRange()
	throws Exception {
		Calendar startDate = startDate();
		Calendar sixDaysAfter = daysAfter(startDate, 6);
		
		StantardCaseDaysRangeEquals5 sixDaysAfterCaseWhereIntervalEquals5 =
			new StantardCaseDaysRangeEquals5(startDate, sixDaysAfter);
		
		Assert.assertTrue(isValid(sixDaysAfterCaseWhereIntervalEquals5));
	}

	@Test
	public void shouldNotBeValidIfDateIntervalIsLessThanChosenDateInterval()
			throws Exception {
		Calendar startDate = startDate();
		Calendar threeDaysAfter = daysAfter(startDate, 3);

		StantardCaseDaysRangeEquals5 instanceIntervalEquals5 =
				new StantardCaseDaysRangeEquals5(startDate, threeDaysAfter);

		Assert.assertFalse(isValid(instanceIntervalEquals5));
		Assert.assertFalse(isValidAccordingToBeanValidation(instanceIntervalEquals5));
	}

	@Test
	public void shouldBeValidIfDateIntervalIsGreaterThanOrEqualChosenDateIntervalForPairsOfDates()
			throws Exception {
		Calendar startDate = startDate();
		Calendar threeDaysAfter = daysAfter(startDate, 3);
		Calendar sixDaysAfter = daysAfter(startDate, 6);

		FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = 
			new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
					startDate, threeDaysAfter, startDate, sixDaysAfter);

		Assert.assertTrue(isValid(twoValidPairsOfDates));
	}

	@Test
	public void shouldNotBeValidIfDateIntervalIsSmallerThanChosenDateIntervalForOneOfThePairsOfDates()
			throws Exception {
		Calendar startDate = startDate();
		Calendar threeDaysAfter = daysAfter(startDate, 3);
		Calendar sixDaysBefore = daysBefore(startDate, 6);

		FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum twoValidPairsOfDates = 
			new FourFieldsFirstRange3DaysMinimumSecondRange2DaysMinimum(
					startDate, threeDaysAfter, startDate, sixDaysBefore);

		Assert.assertFalse(isValid(twoValidPairsOfDates));
	}

	@Test
	public void shouldBeValidIfDateIntervalIsValidFor3FieldsAnd2Ranges()
			throws Exception {
		Calendar startDateRangeOne = startDate();
		Calendar endDateRageOneStartDateRangeTwo = daysAfter(startDateRangeOne,	3);
		Calendar endDateRangeTwo = daysAfter(endDateRageOneStartDateRangeTwo, 2);

		ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = 
			new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
				startDateRangeOne, endDateRageOneStartDateRangeTwo,	endDateRangeTwo);

		Assert.assertTrue(isValid(validThreeFieldsAndTwoRangesInstance));
	}
	
	@Test
	public void shouldNotBeValidIfOnDateIntervalIsInvalidFor3FieldsAnd2Ranges()
	throws Exception {
		Calendar startDateRangeOne = startDate();
		Calendar endDateRageOneStartDateRangeTwo = daysAfter(startDateRangeOne, 3);
		Calendar endDateRangeTwo = daysBefore(endDateRageOneStartDateRangeTwo, 1);
		
		ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum validThreeFieldsAndTwoRangesInstance = 
			new ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(
				startDateRangeOne, endDateRageOneStartDateRangeTwo,	endDateRangeTwo);
		
		Assert.assertFalse(isValid(validThreeFieldsAndTwoRangesInstance));
	}
	
	@Test
	public void shouldBeValidIfIdFromStartDateAndEndDateAnnotationsUsageIsWrong()
	throws Exception {
		Calendar startDateRangeOne = startDate();
		Calendar endDateRageOneStartDateRangeTwo = daysAfter(startDateRangeOne,	3);
		Calendar endDateRangeTwo = daysBefore(endDateRageOneStartDateRangeTwo, 6);
		
		TwoDateIntervalsThreeFieldsWrongUsage threeFieldsAndTwoRangesWrongUsageInstance = 
			new TwoDateIntervalsThreeFieldsWrongUsage(
				startDateRangeOne, endDateRageOneStartDateRangeTwo,	endDateRangeTwo);
		
		Assert.assertTrue(isValid(threeFieldsAndTwoRangesWrongUsageInstance));
	}
	
	@DateRange
	class NoEndDateCase {
		@StartDate
		Calendar data;
	}

	@SuppressWarnings("unused")
	@DateRange
	private class StantardCaseDaysRangeEquals5 {
		@StartDate
		Calendar starDate;

		@EndDate(minimumDaysRange = 5)
		Calendar endDate;

		public StantardCaseDaysRangeEquals5(Calendar starDate,
				Calendar endDate) {
			this.starDate = starDate;
			this.endDate = endDate;
		}
	}

	@SuppressWarnings("unused")
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
				Calendar firstPairDate2, Calendar secondPairDate1,
				Calendar secondPairDate2) {
			
			this.firstRangeStartDate = firstPairDate1;
			this.firstRangeEndDate = firstPairDate2;
			this.secondPairDate1 = secondPairDate1;
			this.secondPairDate2 = secondPairDate2;
		}
	}

	@SuppressWarnings("unused")
	private class ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum {
		@StartDate
		private Calendar startDateRangeOne;

		@EndDate(minimumDaysRange = 2)
		@StartDate(id = 2)
		private Calendar endDateRangeOneAndStartDateRangeTwo;

		@EndDate(minimumDaysRange = 1, id = 2)
		private Calendar endDateRangeTwo;

		public ThreeFieldsFirstRange2DaysMinimumSecondRange1DayMinimum(Calendar date1, Calendar date2,
				Calendar date3) {
			this.startDateRangeOne = date1;
			this.endDateRangeOneAndStartDateRangeTwo = date2;
			this.endDateRangeTwo = date3;
		}
	}
	
	@SuppressWarnings("unused")
	private class TwoDateIntervalsThreeFieldsWrongUsage {
		@StartDate
		private Calendar startDateRangeOne;
		
		@EndDate(minimumDaysRange = 2)
		@StartDate(id = 2)
		private Calendar endDateRangeOneAndStartDateRangeTwo;
		
		@EndDate(minimumDaysRange = 1)
		private Calendar endDateRangeTwo;
		
		public TwoDateIntervalsThreeFieldsWrongUsage(Calendar date1, Calendar date2,
				Calendar date3) {
			this.startDateRangeOne = date1;
			this.endDateRangeOneAndStartDateRangeTwo = date2;
			this.endDateRangeTwo = date3;
		}
	}
	
	private boolean isValid(Object instance) {
		return new DateRangeValidator().isValid(instance, null);
	}

	private Calendar startDate() {
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.set(2011, 1, 27);
		return dataInicio;
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
