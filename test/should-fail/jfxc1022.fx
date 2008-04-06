/*
 * @test/compile-error
 */

class Box {
  attribute holders: Holder[] = bind for (i in [0..5]) Holder {
          box: this
          index: i
  }
}

class Holder {
  attribute box : Box;
  attribute index : Integer;
  attribute x = bind
      if (index <= 0) 0.0
      else box.holders[index-1].x + 1.0
}

Box{} 
