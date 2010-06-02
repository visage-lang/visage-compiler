/*
 * Regression: JFXC-3182 - assignment of conditionally bound java array causes compiler crash on upcast.
 *
 * @test
 * @run
 *
 */

import java.io.*;

var selectedParent = new File(".");
var roots = File.listRoots();
var selectedFiles = bind selectedParent.listFiles(FileFilter {
    override function accept(file) {
        return file.isDirectory();
    }
});
var files:File[] = bind if (selectedParent == null) roots else selectedFiles;
