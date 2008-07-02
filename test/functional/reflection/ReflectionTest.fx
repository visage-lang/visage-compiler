/**
@test
@run
**Functional Testcase for checking Type inference of attributes and function return types based on their assignment expressions and return/last statements,verifcation done using Reflection Apis. Currently only basic functionalities like getAttributes(),getMethods() ,getSuperClasses() are supported.
**/

import javafx.reflect.*;
import java.lang.*;
class TypeInference  {
   bound function func0(s:Integer) { s+2};
   function func1 (input:String) :String {input};
   function func2 (input:String) {func1(func1(input));};
   function func3(input:Integer[]){input [ n | n / 2 ==0]};
   function func4(par1:Integer,par2:Integer){par1=1+par2;TypeInference{}}
   function func5(par1:Integer[],par2:Integer) { insert par2 into par1;par1};
   function func6(){function(i:Integer){return i}}
   function func7(f:function():javafx.lang.Duration) {f()}; // Type of f() expression is javafx.lang.Duration hence result type of func7 is inferred to javafx.lang.Duration

   attribute fnAttr1= function(t:Integer,r:Integer) {r + t};  // Result type of function attribute inferred to Integer
   attribute fnAttr2 : function(:Number,:String)= function(v:Number,w:String){if(true){ new java.lang.Integer(5) } else if(false) {2}else {4}};
   attribute fnAttr3 : function(:Number,:String):Integer;
   //   attribute funAttr2: function():function(); //Bug JFXC-1425
   attribute fnAttr4 = function():function() {function(){}};
   
   attribute attr1 on replace {};
   attribute stringrAttr1 = func1("dummy1");
   attribute stringrAttr2 = {(((((func2("dummy2"))))));}
   attribute stringAttr3 = if(true){ new java.lang.String() } else if(false) {"String"}else {""};
   attribute numberAttr1 ;
   attribute numberAttr2 =if(true){(numberAttr1 +1)} else {numberAttr1 + numberAttr1};

   attribute intAttr1 = fnAttr1(1,2);
   attribute intAttr2 = if(true){ new java.lang.Integer(5) } else if(false) {2}else {-2};
   attribute intAttr3 = sizeof (for(i in [9..100] ) {i} );
   attribute intSeq1= func3([1..100]);
   attribute seq2 =[TypeInference{},TypeInference3{},new TypeInference3()]; //Type infered to TypeInference 
   attribute seq3 =[new String (),"ty1","ty2"];
   attribute seq4 =[1,0.2,new Integer(2)];
   attribute objAttr1 = null;
   attribute objAttr2 = if(true){1}else {"av"};
   attribute booleanAttr1;// Initialized in init block and infered to BooleanVariable
   attribute type1Attr1 = if(true){ TypeInference{} } else if (true) { null}else {new TypeInference};
   init{
	booleanAttr1 = type1Attr1 instanceof TypeInference;
   }	
};

abstract class  TypeInference2 extends ReflectionTest.TypeInference{
attribute myAttr=func8(); //Type inferred to String
abstract function func8():String;
}
class TypeInference3 extends TypeInference,TypeInference2{
function func8(){ myAttr};
override attribute intAttr1 = 20;
override attribute myAttr = "t3";
attribute newAttr = func3([2]);
attribute anotherAttr = func8 ();
}
var context : LocalReflectionContext = LocalReflectionContext.getInstance();
var classRef = context.findClass("ReflectionTest.TypeInference3");
System.out.println("Reflecting class {classRef.getName()}");
System.out.println("Inherited SuperClasses of TypeInference3 => {classRef.getSuperClasses(true)}");
for (attr in classRef.getAttributes(true)) {
  System.out.println(" Type of {attr.getName()} is inferred as {attr.getType()}") ;
}
var itr : java.util.Iterator = classRef.getMethods(false).iterator();
while(itr.hasNext()){
    var mr:MethodRef =(itr.next() as MethodRef);
    System.out.println(" Return Type of method {mr.getName()} inferred as {mr.getType().getReturnType()}");
}

