#About

JSR-303 Validators to validate a daterange and other constraints.

This is a fork of [erichegt/dateRangeValidator](https://github.com/erichegt/dateRangeValidator).

#Examples 

For examples please see the tests or this projects homepage: [http://waxolunist.github.com/validationconstraints/](http://waxolunist.github.com/validationconstraints/).

#What is different

The original version did only allow Calendar to validate. This fork uses the library [joda-time](http://joda-time.sourceforge.net/).
Thus it supports validating every class which can be converted to [DateTime](http://joda-time.sourceforge.net/api-release/index.html).

Besides this fork will support a lot more constraints than its original.

#Versions

##1.0.x - 1.2.x Series

This series targets weblogic installations up to version 10.3.6, due to its dependency on joda-time Version 1.2.1.

##1.3.x Series

This series targets more modern application servers like wildfly 10, due to its dependency on joda-time Version 2.7.

#License

ValidationConstraints is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ValidationConstraints is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ValidationConstraints.  If not, see <http://www.gnu.org/licenses/>.
