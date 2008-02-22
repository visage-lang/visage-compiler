/*
 * @subtest jfxc741
 */

import java.lang.System;

class jfxc741Base {
  attribute frame = "default";
  attribute title: String on replace {
        System.out.println("Frame: {frame}, Title: {title}");
  }
}
