/**
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
package com.vcollaborate.validation.constraints;

import javax.validation.Validation;

import junit.framework.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

import org.junit.Test;

public class EmailValidatorTest {

	@Test
	public void testEmailWithoutSuffix() {
		val email = new EmailWithoutSuffix("test@v-collaborate");
		Assert.assertTrue(isValidAccordingToBeanValidation(email));
	}
	
	@Test
	public void testEmailWithSuffix() {
		val email1 = new EmailWithSuffix("test@v-collaborate");
		Assert.assertFalse(isValidAccordingToBeanValidation(email1));
		
		val email2 = new EmailWithSuffix("test@v-collaborate.com");
		Assert.assertTrue(isValidAccordingToBeanValidation(email2));
	}
	
    @Data
    @AllArgsConstructor
    private class EmailWithoutSuffix {
        @Email(requiressuffix=false)
        private String email;
    }
    
    @Data
    @AllArgsConstructor
    private class EmailWithSuffix {
        @Email
        private String email;
    }
    
    private boolean isValidAccordingToBeanValidation(final Object instance) {
        val factory = Validation.buildDefaultValidatorFactory();
        val validator = factory.getValidator();
        val errors = validator.validate(instance);

        for (val error : errors) {
            System.out.println(error.getMessage());
        }
        return errors.isEmpty();
    }
}
