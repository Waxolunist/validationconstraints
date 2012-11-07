#About

This is a fork of [erichegt/dateRangeValidator](https://github.com/erichegt/dateRangeValidator).

#What is different

The original version did only allow Calendar to validate. This fork uses the library [joda-time](http://joda-time.sourceforge.net/).
Thus it supports validating every class which can be converted to [DateTime](http://joda-time.sourceforge.net/api-release/index.html).

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



ValidationConstraints ist Freie Software: Sie können es unter den Bedingungen
der GNU General Public License, wie von der Free Software Foundation,
Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
veröffentlichten Version, weiterverbreiten und/oder modifizieren.

ValidationConstraints wird in der Hoffnung, dass es nützlich sein wird, aber
OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
Siehe die GNU General Public License für weitere Details.

Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.

#Maven Commands used

##Signing
For signing your build with gpg use the profile -Prelease-sign-artifacts

##Deployment
mvn clean deploy
mvn clean deploy -Prelease-sign-artifacts

##Upload to Github
mvn clean install ghDownloads:upload

##Generate site and upload to github
mvn site
mvn site -Pupload-github-site

##Generate javadoc
mvn javadoc:javadoc

##License Header files
mvn license:format
