define(`loc_type', `loc_type_$1')dnl
define(`bind_type', `bind_type_$1')dnl
define(`ret_type', `ret_type_$1')dnl
define(`var_type', `var_type_$1')dnl
define(`getter', `$1.getter_$2')dnl
dnl
define(`binary_template', `
    public static loc_type($3) $4_$1$2(final loc_type($1) a, final loc_type($2) b) {
        return var_type($3).make(new bind_type($3)``()'' {
            public ret_type($3) computeValue() {
                return getter(`a', $1, ret_type($3))``()'' $5 getter(`b', $2, ret_type($3))``()'';
            }
        }, a, b);
    }
')dnl
define(`unary_template', `
    public static loc_type($3) $4_$1$2(final loc_type($1) a) {
        return var_type($3).make(new bind_type($3)``()'' {
            public ret_type($3) computeValue() {
                return $5 getter(`a', $1, ret_type($3))``()'';
            }
        }, a);
    }
')dnl
define(`loc_type_i', `IntLocation')dnl
define(`bind_type_i', `IntBindingExpression')dnl
define(`ret_type_i', `int')dnl
define(`var_type_i', `IntVariable')dnl
define(`getter_i', `getAsInt')dnl
define(`loc_type_d', `DoubleLocation')dnl
define(`bind_type_d', `DoubleBindingExpression')dnl
define(`ret_type_d', `double')dnl
define(`var_type_d', `DoubleVariable')dnl
define(`getter_d', `getAsDouble')dnl
define(`loc_type_b', `BooleanLocation')dnl
define(`bind_type_b', `BooleanBindingExpression')dnl
define(`ret_type_b', `boolean')dnl
define(`var_type_b', `BooleanVariable')dnl
define(`getter_b', `getAsBoolean')dnl
define(`loc_type_I', `ObjectLocation<Integer>')dnl
define(`bind_type_I', `ObjectBindingExpression<Integer>')dnl
define(`ret_type_I', `Integer')dnl
define(`var_type_I', `ObjectVariable<Integer>')dnl
define(`getter_I', `get')dnl
define(`loc_type_D', `ObjectLocation<Double>')dnl
define(`bind_type_D', `ObjectBindingExpression<Double>')dnl
define(`ret_type_D', `Double')dnl
define(`var_type_D', `ObjectVariable<Double>')dnl
define(`getter_D', `get')dnl
define(`loc_type_B', `ObjectLocation<Boolean>')dnl
define(`bind_type_B', `ObjectBindingExpression<Boolean>')dnl
define(`ret_type_B', `Boolean')dnl
define(`var_type_B', `ObjectVariable<Boolean>')dnl
define(`getter_B', `get')dnl
dnl
define(`bii', binary_template(`i', `i', `i', $1, $2))dnl
define(`bII', binary_template(`I', `I', `i', $1, $2))dnl
define(`bdd', binary_template(`d', `d', `d', $1, $2))dnl
define(`bDD', binary_template(`D', `D', `d', $1, $2))dnl
define(`bid', binary_template(`i', `d', `d', $1, $2))dnl
define(`bdi', binary_template(`d', `i', `d', $1, $2))dnl
define(`biD', binary_template(`i', `D', `d', $1, $2))dnl
define(`bDi', binary_template(`D', `i', `d', $1, $2))dnl
define(`bID', binary_template(`I', `D', `d', $1, $2))dnl
define(`bDI', binary_template(`D', `I', `d', $1, $2))dnl
define(`bbb', binary_template(`b', `b', `b', $1, $2))dnl
define(`bbB', binary_template(`b', `B', `b', $1, $2))dnl
define(`bBb', binary_template(`B', `b', `b', $1, $2))dnl
define(`bBB', binary_template(`B', `B', `b', $1, $2))dnl
define(`ui', unary_template(`i', `', `i', $1, $2))dnl
define(`ud', unary_template(`d', `', `d', $1, $2))dnl
define(`uI', unary_template(`I', `', `i', $1, $2))dnl
define(`uD', unary_template(`D', `', `d', $1, $2))dnl
define(`ub', unary_template(`b', `', `b', $1, $2))dnl
define(`uB', unary_template(`B', `', `b', $1, $2))dnl
dnl
define(`binary_numeric',dnl
bii($1, $2)dnl
bII($1, $2)dnl
bdd($1, $2)dnl
bDD($1, $2)dnl
bid($1, $2)dnl
bdi($1, $2)dnl
biD($1, $2)dnl
bDi($1, $2)dnl
bID($1, $2)dnl
bDI($1, $2)dnl
)dnl
define(`binary_boolean',dnl
bbb($1, $2)dnl
bbB($1, $2)dnl
bBb($1, $2)dnl
bBB($1, $2)dnl
)dnl
define(`unary_numeric',dnl
ui($1, $2)dnl
uI($1, $2)dnl
ud($1, $2)dnl
uD($1, $2)dnl
)dnl
define(`unary_boolean',dnl
ub($1, $2)dnl
uB($1, $2)dnl
)dnl
dnl
package com.sun.javafx.runtime.location;

/**
 * BoundOperators -- do not edit -- machine generated!
 * To regenerate, use the buildtools/GenerateBoundOperators m4 script
 *
 * @author Brian Goetz
 */

public class BoundOperators {

binary_numeric(`plus', `+')
binary_numeric(`minus', `-')
binary_numeric(`times', `*')
binary_numeric(`divide', `/')
unary_numeric(`negate', `-')

binary_boolean(`or', `||')
binary_boolean(`and', `&&')
unary_boolean(`not', `!')
}
