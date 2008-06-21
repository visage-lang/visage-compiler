package assortis.core;

import java.lang.Class;
import java.lang.Error;
import java.lang.Throwable;
import javax.swing.JApplet;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.Entry;
import com.sun.javafx.gui.InternalHelper;

import javafx.gui.*;
import javafx.gui.swing.*;
import java.lang.System;

import java.net.URLClassLoader;

public class AssortisApplet extends JApplet {

    private attribute app:Application;

    private function launchApplication() {
        System.out.println("[applet] start");

        //for(prop in ){
        //}
        
        var appClassName = getParameter("ApplicationClass");
        var errorPrefix = "Couldn't launch FX Application";
        
        var javafxLibURL   = getParameter("JavaFXLibraryURL");
        var assortisLibURL = getParameter("AssortisProjectURL");
        
        System.out.println("[applet] lib url: '{javafxLibURL}'");
        System.out.println("[applet] app url: '{assortisLibURL}'");
        
        var urls = for(lib in ["javafxc","javafxrt","javafxgui","Scenario"]) new java.net.URL("{javafxLibURL}/lib/{lib}.jar");
        
        insert new java.net.URL("{assortisLibURL}/dist/Assortis.jar") into urls;
        
        //System.out.println("[applet] urls: {urls}");

        for(url in urls){
            System.out.println("[applet] url: '{url}'");
        }
        
        //var urlClassLoader = new URLClassLoader(urls, java.lang.Thread.currentThread().getContextClassLoader());
        var urlClassLoader = new URLClassLoader(urls);

        
        if (appClassName != null) {
            try {
                var appClass:Class = Class.forName(appClassName);
                var name = Entry.entryMethodName();
                var args = Sequences.make(java.lang.String.<<class>>) as java.lang.Object;
                var appObject = appClass.getMethod(name, Sequence.<<class>>).invoke(null, args);
                System.out.println("[applet] run fx file");

                //var appObject = ProjectManager.runFXFile("assortis.core.Assortis", urlClassLoader);
                //var appObject = ProjectManager.runFXFile("assortis.sources.language.javafx.api.gui.FXLabel", urlClassLoader);

                System.out.println("[applet] get application");
                app = appObject as Application;
                
            }
            catch (e:Throwable) {
                throw new Error("{errorPrefix} {appClassName}", e);
            }
        }
        else {
            throw new Error("{errorPrefix}: no ApplicationClass applet param specified");
        }
        if (app != null) {
            setContentPane(app.content.getJComponent());
        }
    }

    public function <<init>>() {
        launchApplication();
//        DeferredTask {
//            action: launchApplication
//        }

    }

}
