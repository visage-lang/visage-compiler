/*
 * @subtest jfxc741
 */

import java.lang.System;

public class jfxc741Base {
  package var frame = "default";
  package var title: String on replace {
        System.out.println("Frame: {frame}, Title: {title}");
  }
}
