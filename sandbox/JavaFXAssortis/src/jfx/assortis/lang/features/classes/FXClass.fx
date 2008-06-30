/*
 * FXClass.fx
 *
 * Created on Mar 3, 2008, 11:49:33 AM
 */

package jfx.assortis.lang.features.classes;

import java.lang.System;

public abstract class Animal{
    public attribute name: String;
    public  abstract function talk(): String;
}

class Cat extends Animal{
    override attribute name = "Cat";
    public function talk(): String { return "Meow!"; };
}

  public  class Dog extends Animal {
    override attribute name = "Dog";
    public function talk(): String { return "Bark!"; };
  }
  
  
var cat = Cat {};
var dog = Dog {};

System.out.println("{cat.name}: {cat.talk()}"); // print: Cat: Meow!
System.out.println("{dog.name}: {dog.talk()}"); // print: Dog: Bark!
