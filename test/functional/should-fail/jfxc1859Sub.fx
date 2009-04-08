/*
 * jfxc-1859 : public-read variable can be written from outside the script using bind with inverse
 *   
 * @compilefirst ./jfxc1859.fx 
 * @test/compile-error
 *  
 */

import java.lang.System;

class jfxc1859Sub extends jfxc1859
{
    var x:String;
 }
 def f1=new jfxc1859Sub;
 var f2=jfxc1859Sub{ x:bind f1.s with inverse; };  
 f2.x="update  script only bind access variable";
 System.out.println(f1.s);
