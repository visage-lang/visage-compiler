/*
 * Regression test JFXC-1609 : overloaded script-level functions generate error message
 *
 * @test
 */

public function bar(): Void { }
public function bar(str: String): Void { }
