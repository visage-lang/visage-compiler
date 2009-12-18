/**
 * Regression test JFXC-651 : Compiler Assertion in bind translation
 *
 * @test
 */

import com.sun.javafx.runtime.Pointer;

class ThumbnailView {
   var alphaValue: Number = 1.0;
   var selectedPhotoIndex: Number on replace {
       var bpAlphaValue = bind Pointer.make(alphaValue);
   };
}
