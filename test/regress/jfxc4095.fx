/*
 * Regression: JFXC-4095 - compile-bind: Duplicate children error in most GUI applications.
 *
 * @test
 * @run
 *
 */

class Node { var scene:Object = null on replace {}} 

class Parent extends Node { 

   override var scene = null on replace { 
      child.scene = scene; 
   } 

   var childSet = new java.util.HashSet(); 

   var child:Node on replace { 
      if (childSet == null) { 
      throw new java.lang.IllegalArgumentException("ERROR"); 
      } 
    } 
} 

Parent { child: bind Node{} } 
