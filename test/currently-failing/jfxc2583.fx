/*
 * Compilation error:
 *
 * setAsSequence(com.sun.javafx.runtime.sequence.Sequence<? extends java.lang.Character>) 
 * in com.sun.javafx.runtime.location.SequenceVariable<java.lang.Character> 
 * cannot be applied to (com.sun.javafx.runtime.sequence.Sequence<java.lang.Integer>)
 *    [java] var cSeq1 : Character[] = [ 100..103 ];
 *    [java] ^
 *    [java] 1 error
 *
 * Please uncomment corresponding lines in test/features/F26-numerics/Sequences.fx
 * when this issue is resolved.
 *
 * @test/fail
 */

var cSeq1 : Character[] = [ 100..103 ];