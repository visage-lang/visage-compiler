/**
 * JFXC-3187 : support script-level variables on mixin script-class
 *
 * @subtest
 */

public var VERBOSE = "Yes, I'm oh so verbose";

public function jj() {
    VERBOSE;
}

public mixin class jfxc3187mixin { // note: name MUST match file name
}
