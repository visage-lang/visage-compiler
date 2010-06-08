/*
 * Regression - JFXC-4375 - Compilation fails when using java classes with generics.
 *
 * @subtest
 *
 */

public class jfxc4375Processor {
    public <T> T process(jfxc4375Getter<T> getter) {
        return getter.get();
    }
}
