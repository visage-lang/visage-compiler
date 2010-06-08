/*
 * Regression - JFXC-4375 - Compilation fails when using java classes with generics.
 *
 * @subtest
 *
 */

public interface jfxc4375Getter<T> {
    public T get();
}
