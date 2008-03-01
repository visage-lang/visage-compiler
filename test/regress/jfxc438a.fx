/* test for the string literal translation
 *
 * @test
 * @run
 */

import java.lang.System;
import java.util.*;

// save the default locale for testing
var curLoc = Locale.getDefault();

// set the default locale to Japan
Locale.setDefault(Locale.JAPAN);

System.out.println(##'Hello, World!');

var bday = new GregorianCalendar(1995, Calendar.MAY, 23);
System.out.println(##"Duke's birthday: {%1$tm bday} {%2$te bday}, {%3$tY bday}.");

System.out.println(##'deceiving key1: /*');
System.out.println(##'deceiving key2: //');

// explicit key tests
System.out.println(##[FILE_VERB]'File');
System.out.println(##[FILE_NOUN]'File');
System.out.println(##[NON_EXISTENT_KEY]'non-existent');
System.out.println(##[KEY_WITH_SAME_VALUE]'key with same value default');

// multiple line tests
System.out.println(##'Unix style
new line');
System.out.println(##'Windows style
new line');
System.out.println(##'Mac stylenew line');

// restore the default locale
Locale.setDefault(curLoc);
