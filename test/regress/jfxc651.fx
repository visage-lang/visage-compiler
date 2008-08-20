/**
 * Regression test JFXC-651 : Compiler Assertion in bind translation
 *
 * @test
 */

import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

class ThumbnailView {
   var alphaValue: Number = 1.0;
   var pf: PointerFactory = PointerFactory{};
   var selectedPhotoIndex: Number on replace {
       var bpAlphaValue = bind pf.make(alphaValue);
   };
}
