// COMPILATION_UNIT
package testfx;

// IMPORT
// MEMBER_SELECT
import java.lang.System;
import javafx.animation.Interpolator;

// CLASS_DECLARATION
public class AllTrees {
    
// INIT_DEFINITION
    init {
    }
// POSTINIT_DEFINITION
    postinit {
    }
    
// MODIFIERS
    public attribute attrS : String = "sssssss"
// ON_REPLACE
    on replace 
    oldV = newV {
        System.out.println("Changing from {oldV} to {newV}");
    }
    protected attribute attrN : Integer;
// FUNCTION_DEFINITION
// FUNCTION_VALUE
// TYPE_CLASS
    public function f(param1 : Integer) : Number {
// CONDITIONAL_EXPRESSION
// EQUAL_TO
        if (param1 == 0) {
// RETURN
            return 2.;
        }
// NOT_EQUAL_TO
        if (param1 != 1) 
// BLOCK_EXPRESSION
        {
// THROW
            throw 
            new java.lang.IllegalArgumentException();
        }
// TYPE_CAST
        1 as Number
    }
}
// VARIABLE
// INSTANTIATE_NEW
var x = new AllTrees();

// TRY
try {
// METHOD_INVOCATION
    x.f(0);
// CATCH
} catch (ex) {

}
var c  : Integer = 0;
// WHILE_LOOP
while (c < 10) {
    c++;
    if (c > 5) {
// BREAK
        break;
    } else {
// CONTINUE
        continue;
    }

}

// INSTANCE_OF
x instanceof AllTrees;

// PARENTHESIZED
// ???

// ASSIGNMENT
c = 2;

// MULTIPLY_ASSIGNMENT
c *= 2;

// DIVIDE_ASSIGNMENT
c /= 4;

// REMAINDER_ASSIGNMENT
//c %= 5;

// PLUS_ASSIGNMENT
c += 10;

// MINUS_ASSIGNMENT
c -= 5;

var b : Boolean = true;

// AND_ASSIGNMENT
// b &= true;

// XOR_ASSIGNMENT
// b ^= true;

// OR_ASSIGNMENT
// b |= true;

// PREFIX_INCREMENT
++c;

// POSTFIX_INCREMENT
c++;

// PREFIX_DECREMENT
--c;

// POSTFIX_DECREMENT
c--;

// UNARY_PLUS
//+c;

// UNARY_MINUS
-c;

// BITWISE_COMPLEMENT
//~c;

// LOGICAL_COMPLEMENT
not b;

// AND
//c & 7;

// CONDITIONAL_AND
b and true;

// CONDITIONAL_OR
b or true;

// DIVIDE
c / 2;

// GREATER_THAN
c > 10;

// GREATER_THAN_EQUAL
c >= 10;

// LESS_THAN
c < 10;

// LESS_THAN_EQUAL
c <= 10;

// MINUS
c - 5;

// MULTIPLY
c * 3;

// OR
//c|3;

// PLUS
c + 3;

// REMAINDER
c mod 5;

// XOR
//c ^ 3;

// BIND_EXPRESSION
var bbb = bind c;

// FOR_EXPRESSION_FOR
// FOR_EXPRESSION_IN_CLAUSE
var seq = 
for (i in [3..10]) {
    // INDEXOF
    System.out.println("{i} indexof i == {indexof i}");
    i
};

// FOR_EXPRESSION_PREDICATE
seq[v | v > 5];

// INSTANTIATE_OBJECT_LITERAL
// IDENTIFIER
AllTrees{
// OBJECT_LITERAL_PART
    attrS:"asfd"
    attrN: 400
};

// INTERPOLATE
// what is this? Is it the part after tween keyword?


// KEYFRAME_LITERAL
// TIME_LITERAL
at (1s) {
// INTERPOLATE_VALUE
    c => 1 tween Interpolator.LINEAR;
}

// SEQUENCE_DELETE
delete 8 from seq;

// SEQUENCE_EMPTY
[];

// SEQUENCE_EXPLICIT
[1,2,3];

// SEQUENCE_INDEXED
seq[1];

// SEQUENCE_RANGE
[1..10];

// SEQUENCE_INSERT
insert 10 into seq;
insert 10 after seq[1];
insert 10 before seq[2];

// SEQUENCE_SLICE
seq[1..2];

// STRING_EXPRESSION

" { 10 } ";

// TRIGGER_WRAPPER
// ???

// TYPE_UNKNOWN
function g() {
    ;
}

// TYPE_ANY
// ???

// TYPE_FUNCTIONAL
var tf : function () : String;

// EMPTY_STATEMENT
// ???

// INT_LITERAL
1;

// LONG_LITERAL
//100000000000000000000;

// FLOAT_LITERAL
1.;

// DOUBLE_LITERAL
//1.d;

// BOOLEAN_LITERAL
true;
false;

// CHAR_LITERAL
'a';

// STRING_LITERAL
"asfdasfd";

// NULL_LITERAL
null;

// SIZEOF
sizeof seq;

// REVERSE
reverse seq;

// ERRONEOUS
// not needed here, the line above is just for completness

// OTHER
// ??? any example?

