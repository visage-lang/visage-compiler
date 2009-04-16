/**
 * Regress test for JFXC-3064
 *  Comma is optional when separating object literals in a sequence but
 *  non optional for expressions.
 *
 * @test/compile-error
 */

import javafx.animation.*;

var x: Integer;
Timeline {
keyFrames: [
  at (1s) { x => 5  }
  at (2s) { x => 13 },
  at (3s) { x => 17 }

  42 42
]
} 
