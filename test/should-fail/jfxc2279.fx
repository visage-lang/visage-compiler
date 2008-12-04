/**
 * JFXC-2279 - Illegal content of bound block caught 
 * in translation must be caught in attribution.
 *
 *
 * @test/compile-error
 */
import java.lang.System;

// not allowed in bind context 'System.out.println("hello")'

var v = bind { System.out.println("hello"); 2 }
