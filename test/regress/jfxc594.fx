/* 
 * @test
 *
 * Verify that a default font is set in Text class. 
 */

var t = javafx.ui.canvas.Text{};     // use default font
if (t.font == null) {
    throw new java.lang.AssertionError();
}
