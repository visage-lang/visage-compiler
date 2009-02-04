package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.TypeInfo;

/**
 * PrimitiveChangeListener
 *
 * @author Brian Goetz
 */
public class PrimitiveChangeListener<T> extends AbstractLocationDependency {
    public void onChange(byte oldValue, byte newValue) {
        throw new UnsupportedOperationException();
    }

    public void onChange(short oldValue, short newValue) {
        throw new UnsupportedOperationException();
    }

    public void onChange(int oldValue, int newValue) {
        throw new UnsupportedOperationException();
    }

    public void onChange(long oldValue, long newValue) {
        throw new UnsupportedOperationException();
    }

    public void onChange(float oldValue, float newValue) {
        throw new UnsupportedOperationException();
    }

    public void onChange(double oldValue, double newValue) {
        throw new UnsupportedOperationException();
    }

    public void onChange(boolean oldValue, boolean newValue) {
        throw new UnsupportedOperationException();
    }

    public void onChange(char oldValue, char newValue) {
        throw new UnsupportedOperationException();
    }

    public int getDependencyKind() {
        return AbstractLocation.CHILD_KIND_TRIGGER;
    }

    public ObjectChangeListener<T> asObjectListener(final TypeInfo<T, ?> typeInfo) {
        return new ObjectChangeListener<T>() {
            public void onChange(T oldValue, T newValue) {
                switch (typeInfo.type) {
                    case BYTE:
                        PrimitiveChangeListener.this.onChange((Byte) oldValue, (Byte) newValue);
                        break;

                    case SHORT:
                        PrimitiveChangeListener.this.onChange((Short) oldValue, (Short) newValue);
                        break;

                    case INT:
                        PrimitiveChangeListener.this.onChange((Integer) oldValue, (Integer) newValue);
                        break;

                    case LONG:
                        PrimitiveChangeListener.this.onChange((Long) oldValue, (Long) newValue);
                        break;

                    case FLOAT:
                        PrimitiveChangeListener.this.onChange((Float) oldValue, (Float) newValue);
                        break;

                    case DOUBLE:
                        PrimitiveChangeListener.this.onChange((Double) oldValue, (Double) newValue);
                        break;

                    case BOOLEAN:
                        PrimitiveChangeListener.this.onChange((Boolean) oldValue, (Boolean) newValue);
                        break;

                    case CHAR:
                        PrimitiveChangeListener.this.onChange((Character) oldValue, (Character) newValue);
                        break;

                    default: throw new UnsupportedOperationException();
                }
            }
        };
    }

    public static<T> PrimitiveChangeListener<T> make(final ObjectChangeListener<T> listener) {
        return new PrimitiveChangeListener<T>() {
            public void onChange(byte oldValue, byte newValue) {
                listener.onChange((T) (Byte) oldValue, (T) (Byte) newValue);
            }

            public void onChange(short oldValue, short newValue) {
                listener.onChange((T) (Short) oldValue, (T) (Short) newValue);
            }

            public void onChange(int oldValue, int newValue) {
                listener.onChange((T) (Integer) oldValue, (T) (Integer) newValue);
            }

            public void onChange(long oldValue, long newValue) {
                listener.onChange((T) (Long) oldValue, (T) (Long) newValue);
            }

            public void onChange(float oldValue, float newValue) {
                listener.onChange((T) (Float) oldValue, (T) (Float) newValue);
            }

            public void onChange(double oldValue, double newValue) {
                listener.onChange((T) (Double) oldValue, (T) (Double) newValue);
            }

            public void onChange(boolean oldValue, boolean newValue) {
                listener.onChange((T) (Boolean) oldValue, (T) (Boolean) newValue);
            }

            public void onChange(char oldValue, char newValue) {
                listener.onChange((T) (Character) oldValue, (T) (Character) newValue);
            }
        };
    }
}
