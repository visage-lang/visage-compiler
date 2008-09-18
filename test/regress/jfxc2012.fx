/* JFXC-2012: compilation fails if sequence contains an embedded array returned from java
 *
 * @test
 * @run
 */

import java.lang.System;
import java.lang.Character;

var c:Integer[] = [1,2,'3456'.toCharArray()];
insert '789'.toCharArray() into c;
System.out.println(c);
c = '789'.toCharArray();
System.out.println(c);
c = Character.forDigit(2,10);
System.out.println(c);