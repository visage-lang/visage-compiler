/*
 * Verify java.lang classes must be imported.
 * @test 
 * @compile/fail javaLangImport.fx
 */
System.out.println("hello, world"); // no java.lang.System import
