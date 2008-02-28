package com.sun.javafx.runtime.location;

/**
 * BoundOperators -- do not edit -- machine generated!
 * To regenerate, use the buildtools/GenerateBoundOperators m4 script
 * Add hand-coded methods to BoundOperators.java
 *
 * @author Brian Goetz
 */

public class GeneratedBoundOperators {


    public static IntLocation plus_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.getAsInt()) + ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static IntLocation plus_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.get()) + ((int) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation plus_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) + ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation plus_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) + ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation plus_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) + ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation plus_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) + ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation plus_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) + ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation plus_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) + ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation plus_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.get()) + ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation plus_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) + ((int) b.get());
            }
        }, a, b);
    }


    public static IntLocation minus_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.getAsInt()) - ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static IntLocation minus_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.get()) - ((int) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation minus_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) - ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation minus_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) - ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation minus_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) - ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation minus_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) - ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation minus_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) - ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation minus_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) - ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation minus_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.get()) - ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation minus_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) - ((int) b.get());
            }
        }, a, b);
    }


    public static IntLocation times_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.getAsInt()) * ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static IntLocation times_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.get()) * ((int) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation times_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) * ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation times_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) * ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation times_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) * ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation times_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) * ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation times_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) * ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation times_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) * ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation times_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.get()) * ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation times_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) * ((int) b.get());
            }
        }, a, b);
    }


    public static IntLocation divide_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.getAsInt()) / ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static IntLocation divide_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.get()) / ((int) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation divide_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) / ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation divide_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) / ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation divide_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) / ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation divide_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) / ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation divide_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) / ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation divide_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) / ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation divide_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.get()) / ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation divide_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) / ((int) b.get());
            }
        }, a, b);
    }


    public static IntLocation modulo_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.getAsInt()) % ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static IntLocation modulo_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return ((int) a.get()) % ((int) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) % ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) % ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) % ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.getAsDouble()) % ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.getAsInt()) % ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) % ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((int) a.get()) % ((double) b.get());
            }
        }, a, b);
    }

    public static DoubleLocation modulo_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return ((double) a.get()) % ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation eq_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) == ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation eq_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) == ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) == ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation eq_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) == ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) == ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation eq_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) == ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation eq_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) == ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) == ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation eq_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) == ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) == ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation ne_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) != ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ne_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) != ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) != ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation ne_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) != ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) != ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation ne_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) != ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ne_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) != ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) != ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ne_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) != ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) != ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation ge_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) >= ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ge_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) >= ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ge_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) >= ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation ge_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) >= ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ge_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) >= ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation ge_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) >= ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ge_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) >= ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ge_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) >= ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation ge_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) >= ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ge_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) >= ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation le_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) <= ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation le_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) <= ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation le_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) <= ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation le_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) <= ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation le_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) <= ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation le_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) <= ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation le_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) <= ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation le_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) <= ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation le_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) <= ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation le_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) <= ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation lt_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) < ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation lt_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) < ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation lt_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) < ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation lt_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) < ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation lt_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) < ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation lt_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) < ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation lt_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) < ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation lt_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) < ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation lt_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) < ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation lt_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) < ((int) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation gt_ii(final IntLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) > ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation gt_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) > ((int) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation gt_dd(final DoubleLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) > ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation gt_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) > ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation gt_id(final IntLocation a, final DoubleLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) > ((double) b.getAsDouble());
            }
        }, a, b);
    }

    public static BooleanLocation gt_di(final DoubleLocation a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.getAsDouble()) > ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation gt_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.getAsInt()) > ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation gt_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) > ((int) b.getAsInt());
            }
        }, a, b);
    }

    public static BooleanLocation gt_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((int) a.get()) > ((double) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation gt_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((double) a.get()) > ((int) b.get());
            }
        }, a, b);
    }


    public static IntLocation negate_i(final IntLocation a) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return - ((int) a.getAsInt());
            }
        }, a);
    }

    public static IntLocation negate_I(final ObjectLocation<Integer> a) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return - ((Integer) a.get());
            }
        }, a);
    }

    public static DoubleLocation negate_d(final DoubleLocation a) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return - ((double) a.getAsDouble());
            }
        }, a);
    }

    public static DoubleLocation negate_D(final ObjectLocation<Double> a) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return - ((Double) a.get());
            }
        }, a);
    }



    public static BooleanLocation or_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) || ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation or_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) || ((boolean) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation or_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) || ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation or_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) || ((boolean) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation and_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) && ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation and_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) && ((boolean) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation and_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) && ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation and_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) && ((boolean) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation eq_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) == ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation eq_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) == ((boolean) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation eq_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) == ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation eq_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) == ((boolean) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation ne_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) != ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation ne_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.getAsBoolean()) != ((boolean) b.get());
            }
        }, a, b);
    }

    public static BooleanLocation ne_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) != ((boolean) b.getAsBoolean());
            }
        }, a, b);
    }

    public static BooleanLocation ne_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ((boolean) a.get()) != ((boolean) b.get());
            }
        }, a, b);
    }


    public static BooleanLocation not_b(final BooleanLocation a) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ! ((boolean) a.getAsBoolean());
            }
        }, a);
    }

    public static BooleanLocation not_B(final ObjectLocation<Boolean> a) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ! ((Boolean) a.get());
            }
        }, a);
    }
}
