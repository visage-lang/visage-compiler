/**
 * Regression test for JFXC-2346
 *
 * using wildcard "*" to import all classes in a user defined 
 * package gives compilation error.
 *
 * @test
 * @compile addressbook/Address.fx
 * @compile jfxc2346.fx
 */

import addressbook.*;

Address {
     street: "1 Main Street";
     city: "Santa Clara";
     state: "CA";
     zip: "95050";
} 
