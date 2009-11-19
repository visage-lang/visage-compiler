mixin class B { 
   var x = 10; 
   var y = 10; 
} 

class C extends B { 
   override var y = bind x; 
} 

println(C{}.y);
