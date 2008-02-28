define(`loc_type', `loc_type_$1')dnl
define(`bind_type', `bind_type_$1')dnl
define(`ret_type', `ret_type_$1')dnl
define(`get_type', `get_type_$1')dnl
define(`var_type', `var_type_$1')dnl
define(`getter', ```((''$3``)'' $1.getter_$2``())''')dnl
dnl
define(`binary_template', `
    public static loc_type($3) $4_$1$2(final loc_type($1) a, final loc_type($2) b) {
        return var_type($3).make(new bind_type($3)``()'' {
            public ret_type($3) computeValue() {
                return getter(`a', $1, get_type($1)) $5 getter(`b', $2, get_type($2));
            }
        }, a, b);
    }
')dnl
define(`unary_template', `
    public static loc_type($3) $4_$1$2(final loc_type($1) a) {
        return var_type($3).make(new bind_type($3)``()'' {
            public ret_type($3) computeValue() {
                return $5 getter(`a', $1, ret_type($1));
            }
        }, a);
    }
')dnl
define(`loc_type_i', `IntLocation')dnl
define(`bind_type_i', `IntBindingExpression')dnl
define(`ret_type_i', `int')dnl
define(`get_type_i', `int')dnl
define(`var_type_i', `IntVariable')dnl
define(`getter_i', `getAsInt')dnl
define(`loc_type_d', `DoubleLocation')dnl
define(`bind_type_d', `DoubleBindingExpression')dnl
define(`ret_type_d', `double')dnl
define(`get_type_d', `double')dnl
define(`var_type_d', `DoubleVariable')dnl
define(`getter_d', `getAsDouble')dnl
define(`loc_type_b', `BooleanLocation')dnl
define(`bind_type_b', `BooleanBindingExpression')dnl
define(`ret_type_b', `boolean')dnl
define(`get_type_b', `boolean')dnl
define(`var_type_b', `BooleanVariable')dnl
define(`getter_b', `getAsBoolean')dnl
define(`loc_type_I', `ObjectLocation<Integer>')dnl
define(`bind_type_I', `ObjectBindingExpression<Integer>')dnl
define(`ret_type_I', `Integer')dnl
define(`get_type_I', `int')dnl
define(`var_type_I', `ObjectVariable<Integer>')dnl
define(`getter_I', `get')dnl
define(`loc_type_D', `ObjectLocation<Double>')dnl
define(`bind_type_D', `ObjectBindingExpression<Double>')dnl
define(`ret_type_D', `Double')dnl
define(`get_type_D', `double')dnl
define(`var_type_D', `ObjectVariable<Double>')dnl
define(`getter_D', `get')dnl
define(`loc_type_B', `ObjectLocation<Boolean>')dnl
define(`bind_type_B', `ObjectBindingExpression<Boolean>')dnl
define(`ret_type_B', `Boolean')dnl
define(`get_type_B', `boolean')dnl
define(`var_type_B', `ObjectVariable<Boolean>')dnl
define(`getter_B', `get')dnl
dnl
define(`nii', binary_template(`i', `i', `i', $1, $2))dnl
define(`nII', binary_template(`I', `I', `i', $1, $2))dnl
define(`ndd', binary_template(`d', `d', `d', $1, $2))dnl
define(`nDD', binary_template(`D', `D', `d', $1, $2))dnl
define(`nid', binary_template(`i', `d', `d', $1, $2))dnl
define(`ndi', binary_template(`d', `i', `d', $1, $2))dnl
define(`niD', binary_template(`i', `D', `d', $1, $2))dnl
define(`nDi', binary_template(`D', `i', `d', $1, $2))dnl
define(`nID', binary_template(`I', `D', `d', $1, $2))dnl
define(`nDI', binary_template(`D', `I', `d', $1, $2))dnl
dnl
define(`cii', binary_template(`i', `i', `b', $1, $2))dnl
define(`cII', binary_template(`I', `I', `b', $1, $2))dnl
define(`cdd', binary_template(`d', `d', `b', $1, $2))dnl
define(`cDD', binary_template(`D', `D', `b', $1, $2))dnl
define(`cid', binary_template(`i', `d', `b', $1, $2))dnl
define(`cdi', binary_template(`d', `i', `b', $1, $2))dnl
define(`ciD', binary_template(`i', `D', `b', $1, $2))dnl
define(`cDi', binary_template(`D', `i', `b', $1, $2))dnl
define(`cID', binary_template(`I', `D', `b', $1, $2))dnl
define(`cDI', binary_template(`D', `I', `b', $1, $2))dnl
dnl
define(`bbb', binary_template(`b', `b', `b', $1, $2))dnl
define(`bbB', binary_template(`b', `B', `b', $1, $2))dnl
define(`bBb', binary_template(`B', `b', `b', $1, $2))dnl
define(`bBB', binary_template(`B', `B', `b', $1, $2))dnl
dnl
define(`cbb', binary_template(`b', `b', `b', $1, $2))dnl
define(`cbB', binary_template(`b', `B', `b', $1, $2))dnl
define(`cBb', binary_template(`B', `b', `b', $1, $2))dnl
define(`cBB', binary_template(`B', `B', `b', $1, $2))dnl
dnl
define(`ni', unary_template(`i', `', `i', $1, $2))dnl
define(`nd', unary_template(`d', `', `d', $1, $2))dnl
define(`nI', unary_template(`I', `', `i', $1, $2))dnl
define(`nD', unary_template(`D', `', `d', $1, $2))dnl
dnl
define(`bb', unary_template(`b', `', `b', $1, $2))dnl
define(`bB', unary_template(`B', `', `b', $1, $2))dnl
dnl
define(`binary_numeric',dnl
nii($1, $2)dnl
nII($1, $2)dnl
ndd($1, $2)dnl
nDD($1, $2)dnl
nid($1, $2)dnl
ndi($1, $2)dnl
niD($1, $2)dnl
nDi($1, $2)dnl
nID($1, $2)dnl
nDI($1, $2)dnl
)dnl
define(`comparison_numeric',dnl
cii($1, $2)dnl
cII($1, $2)dnl
cdd($1, $2)dnl
cDD($1, $2)dnl
cid($1, $2)dnl
cdi($1, $2)dnl
ciD($1, $2)dnl
cDi($1, $2)dnl
cID($1, $2)dnl
cDI($1, $2)dnl
)dnl
define(`binary_boolean',dnl
bbb($1, $2)dnl
bbB($1, $2)dnl
bBb($1, $2)dnl
bBB($1, $2)dnl
)dnl
define(`comparison_boolean',dnl
cbb($1, $2)dnl
cbB($1, $2)dnl
cBb($1, $2)dnl
cBB($1, $2)dnl
)dnl
define(`unary_numeric',dnl
ni($1, $2)dnl
nI($1, $2)dnl
nd($1, $2)dnl
nD($1, $2)dnl
)dnl
define(`unary_boolean',dnl
bb($1, $2)dnl
bB($1, $2)dnl
)dnl
dnl
package com.sun.javafx.runtime.location;

/**
 * BoundOperators -- do not edit -- machine generated!
 * To regenerate, use the buildtools/GenerateBoundOperators m4 script
 * Add hand-coded methods to BoundOperators.java
 *
 * @author Brian Goetz
 */

public class BoundOperators {

binary_numeric(`plus', `+')
binary_numeric(`minus', `-')
binary_numeric(`times', `*')
binary_numeric(`divide', `/')
binary_numeric(`modulo', `%')
comparison_numeric(`eq', `==')
comparison_numeric(`ne', `!=')
comparison_numeric(`ge', `>=')
comparison_numeric(`le', `<=')
comparison_numeric(`lt', `<')
comparison_numeric(`gt', `>')
unary_numeric(`negate', `-')

binary_boolean(`or', `||')
binary_boolean(`and', `&&')
comparison_boolean(`eq', `==')
comparison_boolean(`ne', `!=')
unary_boolean(`not', `!')
}
