/*
 * Regression: JFXC-4163 - Mixin class static function values are not compiling. Blocking build.
 *
 * @subtest
 *
 */
 
 
public var sf1 = function():String { "jfxc4163super sf1" };
public var sf2 = function():String { "jfxc4163super sf2" };
public var sf3 = function():String { "jfxc4163super sf3" };
public var sf4 = function():String { "jfxc4163super sf4" };

public class jfxc4163super {
    public var cf1 = function():String { "jfxc4163super cf1" };
    public var cf2 = function():String { "jfxc4163super cf2" };
}