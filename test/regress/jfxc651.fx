/**
 * Regression test JFXC-651 : Compiler Assertion in bind translation
 *
 * @test
 */

import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

class ThumbnailView {
   attribute alphaValue: Number = 1.0;
   attribute pf: PointerFactory = PointerFactory{};
   attribute selectedPhotoIndex: Number on replace {
       var bpAlphaValue = bind pf.make(alphaValue);
   };
}
