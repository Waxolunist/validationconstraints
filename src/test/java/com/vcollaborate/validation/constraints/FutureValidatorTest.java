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

import org.joda.time.DateTime;
import org.junit.Test;

public class FutureValidatorTest {

    @Test
    public void testsWithToday() throws Exception {
        FutureDate fd = new FutureDate(new Date());

        Assert.assertFalse(isValidAccordingToBeanValidation(fd));
        
        FutureDateWithToday fdwt = new FutureDateWithToday(new Date());

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
