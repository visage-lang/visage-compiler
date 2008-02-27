package com.sun.javafx.runtime.location;

/**
 * BoundOperators -- do not edit -- machine generated!
 * To regenerate, use the buildtools/GenerateBoundOperators m4 script
 *
 * @author Brian Goetz
 */

public class BoundOperators {


    public static IntLocation plus_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() + b.getAsInt();
            }
        }, a, b);
    }

    public static IntLocation plus_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.get() + b.get();
            }
        }, a, b);
    }

    public static DoubleLocation plus_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() + b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation plus_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() + b.get();
            }
        }, a, b);
    }

    public static DoubleLocation plus_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() + b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation plus_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() + b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation plus_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() + b.get();
            }
        }, a, b);
    }

    public static DoubleLocation plus_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() + b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation plus_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() + b.get();
            }
        }, a, b);
    }

    public static DoubleLocation plus_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() + b.get();
            }
        }, a, b);
    }


    public static IntLocation minus_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() - b.getAsInt();
            }
        }, a, b);
    }

    public static IntLocation minus_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.get() - b.get();
            }
        }, a, b);
    }

    public static DoubleLocation minus_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() - b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation minus_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() - b.get();
            }
        }, a, b);
    }

    public static DoubleLocation minus_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() - b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation minus_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() - b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation minus_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() - b.get();
            }
        }, a, b);
    }

    public static DoubleLocation minus_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() - b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation minus_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() - b.get();
            }
        }, a, b);
    }

    public static DoubleLocation minus_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() - b.get();
            }
        }, a, b);
    }


    public static IntLocation times_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() * b.getAsInt();
            }
        }, a, b);
    }

    public static IntLocation times_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.get() * b.get();
            }
        }, a, b);
    }

    public static DoubleLocation times_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() * b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation times_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() * b.get();
            }
        }, a, b);
    }

    public static DoubleLocation times_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() * b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation times_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() * b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation times_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() * b.get();
            }
        }, a, b);
    }

    public static DoubleLocation times_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() * b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation times_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() * b.get();
            }
        }, a, b);
    }

    public static DoubleLocation times_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() * b.get();
            }
        }, a, b);
    }


    public static IntLocation divide_ii(final IntLocation a, final IntLocation b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() / b.getAsInt();
            }
        }, a, b);
    }

    public static IntLocation divide_II(final ObjectLocation<Integer> a, final ObjectLocation<Integer> b) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.get() / b.get();
            }
        }, a, b);
    }

    public static DoubleLocation divide_dd(final DoubleLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() / b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation divide_DD(final ObjectLocation<Double> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() / b.get();
            }
        }, a, b);
    }

    public static DoubleLocation divide_id(final IntLocation a, final DoubleLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() / b.getAsDouble();
            }
        }, a, b);
    }

    public static DoubleLocation divide_di(final DoubleLocation a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() / b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation divide_iD(final IntLocation a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsInt() / b.get();
            }
        }, a, b);
    }

    public static DoubleLocation divide_Di(final ObjectLocation<Double> a, final IntLocation b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() / b.getAsInt();
            }
        }, a, b);
    }

    public static DoubleLocation divide_ID(final ObjectLocation<Integer> a, final ObjectLocation<Double> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() / b.get();
            }
        }, a, b);
    }

    public static DoubleLocation divide_DI(final ObjectLocation<Double> a, final ObjectLocation<Integer> b) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.get() / b.get();
            }
        }, a, b);
    }


    public static IntLocation negate_i(final IntLocation a) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return - a.getAsInt();
            }
        }, a);
    }

    public static IntLocation negate_I(final ObjectLocation<Integer> a) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return - a.get();
            }
        }, a);
    }

    public static DoubleLocation negate_d(final DoubleLocation a) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return - a.getAsDouble();
            }
        }, a);
    }

    public static DoubleLocation negate_D(final ObjectLocation<Double> a) {
        return DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return - a.get();
            }
        }, a);
    }



    public static BooleanLocation or_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.getAsBoolean() || b.getAsBoolean();
            }
        }, a, b);
    }

    public static BooleanLocation or_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.getAsBoolean() || b.get();
            }
        }, a, b);
    }

    public static BooleanLocation or_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.get() || b.getAsBoolean();
            }
        }, a, b);
    }

    public static BooleanLocation or_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.get() || b.get();
            }
        }, a, b);
    }


    public static BooleanLocation and_bb(final BooleanLocation a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.getAsBoolean() && b.getAsBoolean();
            }
        }, a, b);
    }

    public static BooleanLocation and_bB(final BooleanLocation a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.getAsBoolean() && b.get();
            }
        }, a, b);
    }

    public static BooleanLocation and_Bb(final ObjectLocation<Boolean> a, final BooleanLocation b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.get() && b.getAsBoolean();
            }
        }, a, b);
    }

    public static BooleanLocation and_BB(final ObjectLocation<Boolean> a, final ObjectLocation<Boolean> b) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return a.get() && b.get();
            }
        }, a, b);
    }


    public static BooleanLocation not_b(final BooleanLocation a) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ! a.getAsBoolean();
            }
        }, a);
    }

    public static BooleanLocation not_B(final ObjectLocation<Boolean> a) {
        return BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return ! a.get();
            }
        }, a);
    }

}
