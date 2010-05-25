/*
 * Regression - JFXC-4375 - Compilation fails when using java classes with generics.
 *
 * @compilefirst jfxc4375Getter.java
 * @compilefirst jfxc4375Processor.java
 * @compilefirst jfxc4375DataContainer.fx
 * @test
 *
 */


jfxc4375DataContainer{data: "compilation not working"}.perform(new jfxc4375Processor());