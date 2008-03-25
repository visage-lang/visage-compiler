/*
 * Should complain that:
 * "non-static method bar() cannot be referenced from a static context"
 *
 * @test/fail
 */
class Foo {
    attribute value;
    function bar() { 123 }
    static attribute notbug = Foo{ value:123 }
    static attribute bug = Foo{ value:bar() }
}
