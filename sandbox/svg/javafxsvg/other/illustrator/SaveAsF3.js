/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

var f3Home = $.getenv("F3_HOME") + "/";
var javaHome = $.getenv("JAVA_HOME");

if (app.activeDocument != null) {
    var sourceDoc = app.activeDocument;
    var destFolder = getDestinationFolder();
    var originalName = sourceDoc.name;
    var baseOriginalName = basename(originalName);
    var originalFile = sourceDoc.fullName;
    var baseSourceName = basename(originalFile.fsName);

    var f3File = new File(baseSourceName + ".f3");
    f3File = f3File.saveDlg("Save as F3", "F3 Files:*.f3");

    if (f3File != null) {
        if (!sourceDoc.saved) {
		    sourceDoc.save();
        }

		var svgFile = new File(destFolder + "/" + baseOriginalName + ".svg");
	
		var opts = new ExportOptionsSVG();
		opts.embedRasterImages = true;
		sourceDoc.exportFile(svgFile, ExportType.SVG, opts);
	
		sourceDoc.close();
		app.open(originalFile);
	
	    var batchFile = new File(destFolder + "/" + baseOriginalName + ".bat");
	    batchFile.open("w");
	    batchFile.writeln("@ECHO OFF");
	    batchFile.writeln("set JAVA_HOME=", javaHome);
	    batchFile.writeln("set PATH=%JAVA_HOME%/bin;%PATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "f3rt.jar;", "%CLASSPATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "lib/Filters.jar;", "%CLASSPATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "lib/swing-layout.jar;", "%CLASSPATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "f3svg/f3svg.jar;", "%CLASSPATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "f3svg/lib/batik-awt-util.jar;", "%CLASSPATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "f3svg/lib/batik-parser.jar;", "%CLASSPATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "f3svg/lib/batik-util.jar;", "%CLASSPATH%");
	    batchFile.writeln("SET CLASSPATH=", f3Home, "f3svg/lib/resolver.jar;", "%CLASSPATH%");
	    batchFile.writeln("ECHO Saving in F3 format...");
	    batchFile.writeln("java ",
	                      "-Xss1M ",
	                      "-Xmx512M ",
	                      "net.java.f3.svg.translator.main.Main ",
	                      '"file:///' + svgFile.fsName.replace(/\\/g, '/') + '" ',
	                      '"' + f3File.fsName + '"');
        batchFile.writeln("DEL ", svgFile.fsName);
        batchFile.writeln("DEL ", batchFile.fsName);
        batchFile.close();
        batchFile.execute();
    }
}

function basename(filename) {
	if (filename.indexOf('.') < 0) {
		return filename;
	} else {
		return filename.substring(0, filename.lastIndexOf('.'));
	}
}

function getDestinationFolder() {
    var destFolder = new Folder(Folder.temp + "/F3");
    if (!destFolder.exists) {
        destFolder.create();
    }
    var destFolder = new Folder(destFolder + "/Illustrator");
    if (!destFolder.exists) {
        destFolder.create();
    }

    return destFolder
}

