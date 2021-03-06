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

package com.vcollaborate.validation.constraints;

import java.net.IDN;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Christian Sterzl
 * @since 1.2.6
 * 
 * @see org.hibernate.validator.internal.constraintvalidators.EmailValidator
 * @see com.vcollaborate.validation.constraints.Email
 */
public class EmailValidator implements ConstraintValidator<Email, CharSequence> {
  private static String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
  private static String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
  private static String DOMAIN_WITHSUFFIX = "(" + ATOM + "+(\\." + ATOM + "+)+";
  private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

  private Pattern pattern = Pattern.compile("^" + ATOM + "+(\\."
      + ATOM + "+)*@" + DOMAIN + "|" + IP_DOMAIN + ")$", Pattern.CASE_INSENSITIVE);

  private Pattern patternWithSuffix = Pattern.compile("^" + ATOM
      + "+(\\." + ATOM + "+)*@" + DOMAIN_WITHSUFFIX + "|" + IP_DOMAIN + ")$",
      Pattern.CASE_INSENSITIVE);

  Email annotation;

  @Override
  public void initialize(Email annotation) {
    this.annotation = annotation;
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null || value.length() == 0) {
      return true;
    }
    String asciiString = IDN.toASCII(value.toString());
    Matcher matcher;
    if (annotation.requiressuffix()) {
      matcher = patternWithSuffix.matcher(asciiString);
    } else {
      matcher = pattern.matcher(asciiString);
    }
    return matcher.matches();
  }
}
