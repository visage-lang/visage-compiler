/*
 * @subtest F24
 */

public abstract class MyShape {
  public var shapeStr : String;
  public var shapeInt : Integer = 10;
  public var shapeNum : Number;
  public var shapeFun1 : function(:Integer,:String):String;
  public abstract function transformed(tr:java.awt.geom.AffineTransform):MyShape;
  public function times1(x: Number): Number {
    return shapeInt*shapeNum*x;
  }
};
