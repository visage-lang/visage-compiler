/*
 * Regression: JFXC-1219 - Issue when French quotes used with class names.
 *
 * @test
 *
 */

class <<class/**/>>{}
var obj :<<class/**/>> =<<class/**/>>{};
