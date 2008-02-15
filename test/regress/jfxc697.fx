/* test for the StringLocalizer.associate() function
 *
 * @test
 * @compile foo/bar/FXPropTest.fx
 * @run
 */

import java.lang.System;
import java.util.*;

import javafx.util.StringLocalizer;
import foo.bar.FXPropTest;

// save the default locale for testing
var curLoc = Locale.getDefault();

// set the default locale to Japan
Locale.setDefault(Locale.JAPAN);

var aTest = FXPropTest{};

// These should print the originals
aTest.print();

StringLocalizer.associate("foo.bar", "FXPropTest.fx", "foo.bar.FXPropTestResources");
StringLocalizer.associate("foo.bar", null, "foo.FooResources");

// These should print the translated strings in 
// foo/bar/FXPropTestResources_ja.fxproperties
aTest.print();

StringLocalizer.associate("foo.bar", "FXPropTest.fx", null);

// These should print the translated strings in 
// foo/FooResources_ja.fxproperties
aTest.print();

// restore the default locale
Locale.setDefault(curLoc);
