/* JFXC-309:  Cannot resolve an overloaded method returning float in Annon inner class
 * @test
 */
import java.awt.*;

class Bar {
attribute layoutManager = LayoutManager2 { 
		public function addLayoutComponent(name : java.lang.String, comp:Component) : Void {}
	
                public function addLayoutComponent(comp:java.awt.Component, constraint:java.lang.Object):Void { 
                } 
                public function maximumLayoutSize(container:java.awt.Container):Dimension { 
                    return new Dimension(java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE); 
                } 
                public function minimumLayoutSize(container:java.awt.Container):Dimension { 
                    return new Dimension(0, 0); 
                } 
                public function preferredLayoutSize(container:java.awt.Container):Dimension { 
                    return new Dimension(0, 0); 
                } 
                public function getLayoutAlignmentX(container:java.awt.Container):Number { 
                    return 0; 
                } 
                public function getLayoutAlignmentY(container:java.awt.Container):Number { 
                    return 0; 
                } 
                public function invalidateLayout(container:java.awt.Container):Void { 
                } 
                public function layoutContainer(container:java.awt.Container):Void { 
                } 
                public function removeLayoutComponent(comp:java.awt.Component):Void { 
                } 
            }; 
}