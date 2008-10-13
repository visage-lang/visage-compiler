/**
 * @test
 * @run
 * Functional Testcase for checking Type inference of vars and function return types based on their assignment expressions and return/last statements,verifcation done using Reflection Apis. Currently only basic functionalities like getvars(),getFunctions() ,getSuperClasses() are supported.
 */
import javafx.reflect.*;
import java.lang.*;

class BaseTestClass {
   var vB_attr1 on replace {};
	var vB_name:String = "TestClass";
	var vB_i:Integer = 1;
	var vB_n:Number = 1.23;
	var vB_is:Integer[] = [1,2,3];
	var vB_b:Boolean = true;

	bound function func0(s:Integer) { s+2};
	function fbFunc0 () {}
   function fbFunc1 (input:String) :String {input};
   function fbFunc2 (input:String) {fbFunc1(fbFunc1(input));};
   function fbFunc3(input:Integer[]){input [ n | n / 2 ==0]};
	function fbFunc4(input1:String[],input2:String[]):Integer[] {
	  var matches = 0;
	  for(s in input1)
	    for ( s2 in input2)
		   if(s.compareTo( s2 )==0 ){ matches++ }
	  return matches;
	}
   function fbFunc5(par1:Integer[],par2:Integer) { var seq = par1; insert par2 into seq; seq};
   function fbFunc6(){function(i:Integer){return i}}
   function fbFunc7(f:function():javafx.lang.Duration) {f()}; // Type of f() expression is javafx.lang.Duration hence result type of func7 is inferred to javafx.lang.Duration
	function fbFunc8(b1:Boolean, b2:Boolean):Boolean {(b1==b2)}
	function fbFunc9(i:Integer):Number { i * 1.0 }
   var fbFnAttr1 = function(t:Integer,r:Integer) {r + t};  // Result type of function var inferred to Integer
   var fbFnAttr2 : function(:Number,:String)= function(v:Number,w:String){if(true){ new java.lang.Integer(5) } else if(false) {2}else {4}};
   var fbFnAttr3 : function(:Number,:String):Integer;
//   var funAttr2: function():function(); //Bug JFXC-1425
   var fbFnAttr4 = function():function() {function(){}};
}

class TestClass extends BaseTestClass  {
   var vattr1 on replace {};
	var vname:String = "TestClass";
	var vi:Integer = 1;
	var vn:Number = 1.23;
	var vis:Integer[] = [1,2,3];
	var vb:Boolean = true;

	var vstringrAttr1 = fbFunc1("dummy1");
   var vstringrAttr2 = {(((((fbFunc2("dummy2"))))));}
   var vstringAttr3 = if(true){ new java.lang.String() } else if(false) {"String"}else {""};
	var vnumberAttr1 ;
   var vnumberAttr2 =if(true){(vnumberAttr1 +1)} else {vnumberAttr1 + vnumberAttr1};

   var vintAttr1 = fbFnAttr1(1,2);
   var vintAttr2 = if(true){ new java.lang.Integer(5) } else if(false) {2}else {-2};
   var vintAttr3 = sizeof (for(i in [9..100] ) {i} );
   var vintSeq1= fbFunc3([1..100]);
   var vseq3 =[new String (),"ty1","ty2"];
   var vseq4 =[1,0.2,new Integer(2)];
   var vobjAttr1 = null;
   var vobjAttr2 = if(true){1}else {"av"};
   var vbooleanAttr1;
};

function inspectMember( fxo: FXObjectValue ) {

}

function inspectClass( context: FXLocal.Context, clzName:String, fxov: FXObjectValue) { 
		var clsTestClass: FXClassType;
	   var clsObject =  fxov;
		try {
		clsTestClass = context.findClass(clzName);
		} catch( e: java.lang.Exception) {println("PASS findClass Exception: did not find {clzName}"); }
		try {
		clsTestClass = context.findClass(clzName);
		} catch( e2: java.lang.Exception) {println("Exception: did not find {clzName}");  }
/*
		try {
		clsObject = context.mirrorOf( clsTestClass );
		println("-------get mirror of {clsTestClass}");
		} catch( e3: java.lang.Exception) {println("EXCEPTION: mirrorof() failed"); }
*/

		//
		System.out.println("About class {clsTestClass.getName()}");
		var cname = clsTestClass.toString();
		println("{cname}");
		print("  Super classes of {clsTestClass.getName()}:"); println( clsTestClass.getSuperClasses(true) );
		println("  Compound class: { clsTestClass.isCompoundClass() }");
		println("  JavaFX Type   : {clsTestClass.isJfxType()}");
		for( clz in clsTestClass.getSuperClasses(true) )
		  if( not clz.equals( clsTestClass )) {
			 if(clz.isAssignableFrom( clsTestClass )){ println("  {clz.toString()} is assignable from {cname}") 
			 }else { println("  {clz.toString()} is not assignable from {cname}") }
		  if(clsTestClass.isAssignableFrom ( clz )){ println("  {cname} is assignable from {clz.toString()}") 
		  } else { println("  {cname} is not assignable from {clz.toString()}") }
		}
		// public boolean isAssignableFrom(FXClassType cls) {}
		println("MEMBERS of {clsTestClass.toString()}");
		for (attr in clsTestClass.getMembers(true)) {
		  println("   {attr.getName()}  isStatic={attr.isStatic()}; Declaring class: {attr.getDeclaringClass().getName()} ");
		}
		// variable member
		println("VARIABLES of {clsTestClass.toString()}");
		for (vmem in clsTestClass.getVariables(true)) {
		  var fxv:FXValue = vmem.getValue(clsObject);
		  var fxT = vmem.getType().getName();
		  print("  {vmem.getName()} type:{fxT}  static={vmem.isStatic()}; Decl.class: {vmem.getDeclaringClass().getName()} ");  
		  try {println("  value=[{vmem.getValue(clsObject).getValueString()}], itemcount={fxv.getItemCount()}");
		  }catch(iae:java.lang.IllegalArgumentException){println(iae); }
		}

		// functions
		println("FUNCTIONS of {clsTestClass.toString()}");
		for (func in clsTestClass.getFunctions(true)) {
			var rtype = func.getType().getReturnType();
			var fname = func.getName();
			var margs = func.getType().minArgs();
			var vargs = func.getType().isVarArgs();
			println("  {rtype} {fname}() min_args={margs}; var_args: {vargs}");
		}

}
//  System.out.println(" Type of {attr.getName()} is inferred as {attr.getType()}") ;
//}
//for (mr in classRef.getFunctions(true)) {
//    System.out.println(" Return Type of method {mr.getName()} inferred as {mr.getType().getReturnType()}");
//}
///////////////////////////////////////////////////
	function _func0 () {}
   function _func1 (input:String) :String {input};
   function _func2 (input:String) {_func1(_func1(input));};
   function _func3(input:Integer[]){input [ n | n / 2 ==0]};
	function _func4(input1:String[],input2:String[]):Integer[] {
	  var matches = 0;
	  for(s in input1)
	    for ( s2 in input2)
		   if(s.compareTo( s2 )==0 ){ matches++ }
	  return matches;
	}
   function _func5(par1:Integer[],par2:Integer) { var seq = par1; insert par2 into seq; seq};
   function _func6(){function(i:Integer){return i}}
   function _func7(f:function():javafx.lang.Duration) {f()}; // Type of f() expression is javafx.lang.Duration hence result type of func7 is inferred to javafx.lang.Duration
	function _func8(b1:Boolean, b2:Boolean):Boolean {(b1==b2)}
	function _func9(i:Integer):Number { i * 1.0 }

/////////////////////////////////////////////////////
var _context : FXLocal.Context = FXLocal.getContext();
var _clzStrings  = ["ReflectionTest2","ReflectionTest2.TestClass"];
function run() {
	var rf2 : ReflectionTest2 = new ReflectionTest2();
	var rf2m = _context.mirrorOf(rf2);
	inspectClass(_context,"ReflectionTest2",rf2m);
   println("___________________________________________________________");
	var tc  : TestClass = new TestClass();
	var tcm  = _context.mirrorOf(tc);
  	inspectClass(_context,"ReflectionTest2.TestClass",tcm );
}

