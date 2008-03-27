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

// This should print the originals
aTest.print();

// This should print the translated strings in 
// foo/bar/FXPropTestResources_ja.fxproperties
StringLocalizer.associate("foo.bar.FXPropTestResources", "foo.bar", "FXPropTest.fx");
StringLocalizer.associate("foo.FooResources", "foo.bar");
aTest.print();

// This should print the translated strings in 
// foo/FooResources_ja.fxproperties
StringLocalizer.dissociate("foo.bar", "FXPropTest.fx");
aTest.print();

// This should print the originals
StringLocalizer.associate("foo.bar.FXPropTestResources", "foo.bar", "FXPropTest.fx");
StringLocalizer.dissociate("foo.bar");
aTest.print();

// restore the default locale
Locale.setDefault(curLoc);
