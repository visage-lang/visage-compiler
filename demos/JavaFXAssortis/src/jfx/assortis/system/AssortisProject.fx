/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */


/*
 * AssortisProject.fx
 *
 * Created on Feb 11, 2008, 5:55:40 PM
 */

package jfx.assortis.system;

/**
 * @author Alexandr Scherbatiy
 */

import java.io.*;


import java.lang.Class;
import java.lang.Object;
import java.lang.System;

import java.lang.Thread;
import java.lang.Runnable;
import java.lang.StringBuffer;

import java.net.URI;

import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;

import javafx.ui.*;
import javafx.ui.canvas.*;

import jfx.assortis.system.structure.*;


class ProjectModule {
    attribute name: String;
    public attribute samples: ProjectSample[];
    static
    function createProjectModule(module: Module): ProjectModule{
        return ProjectModule{
            name: module.name
            samples: for(sample in module.samples) ProjectSample.createProjectSample(sample)
        };
    }
}


class ProjectSample {
    public attribute name: String;
    public attribute className: String;
    public attribute project: AssortisProject;
    public attribute visible: Boolean;
    public attribute selected: Boolean on replace {
        //System.out.println("[sample] name: \"{name}\", selected: {selected}");
        if(selected){
            project.selectTab(name);            
        }
    } ;
        
    public attribute frame: InternalFrame;
    //attribute isExecuted: Boolean;

    private attribute propTabs:Tab[];
    private attribute propLocales:List;

    
    static
    function createProjectSample(sample: Sample): ProjectSample{
        return ProjectSample{
            name: sample.name
            className: sample.className
            visible: sample.visible
        };
    }
}

class LookAndFeelCell{
    attribute name: String;
    attribute className: String;
}

class KeyTimer{
    attribute name: String;
    attribute time: Number;
    attribute action: function();
}

public class AssortisProject  extends CompositeWidget{
    
    private attribute treeNode: TreeNode;
    
    private attribute treeCell: TreeCell;
    private attribute frames: InternalFrame[];
    private attribute samples:ProjectSample[];
    private attribute executedSamples:ProjectSample[];
    
    private attribute codeTabs:Tab[];
    
    private attribute selectedCodeIndex: Number on replace{
        
        if ( codeTabs <> [] and 0 <= selectedCodeIndex ){
            var title = codeTabs[selectedCodeIndex.intValue()].title;
            selectFrame(title);
            
        }
    };
    
    private attribute currentPropTabs:Tab[];
    private attribute currentPropLocales:List;
    private attribute defLocale:Locale = Locale.getDefault();

    private attribute selectedPropIndex: Number on replace {
        if (currentPropTabs <> [] and 0 <= selectedPropIndex ){
            var index = selectedPropIndex.intValue();
            var codeIndex = selectedCodeIndex.intValue();
            var l = currentPropLocales.get(index) as Locale;
            if (l <> null) {
                Locale.setDefault(l);
            }
            createFrame(executedSamples[codeIndex], 
                        (codeTabs[codeIndex].content as EditorPane).text, 
                        (currentPropTabs[index].content as EditorPane).text);
        }
    };

    private attribute selectedSampleIndex: Integer;
    
    
    private attribute selectedModule:Object on replace{
        //System.out.println("[selected module] {selectedModule}");
        selectedSampleIndex = -1;
        
        var cell = selectedModule as TreeCell;
        var m = cell.value as ProjectModule;
        samples = m.samples;
        //System.out.println("[selected value] {m}");
        selectedSampleIndex = sizeof samples - 1;
        
    };
        
    attribute lafIndex:Integer = -1 on replace{
        if( 0 <= lafIndex ){
            javax.swing.UIManager.setLookAndFeel( lafs[lafIndex].className );
        }
    };
    
    
    attribute lafs: LookAndFeelCell[] = [
        //LookAndFeelCell{ name: "Default" className: ProjectManager.DEFAULT_LOOK_AND_FEEL  },
        LookAndFeelCell{ name: "Nebula" className: "org.jvnet.substance.skin.SubstanceNebulaLookAndFeel" },
        LookAndFeelCell{ name: "Autumn" className: "org.jvnet.substance.skin.SubstanceAutumnLookAndFeel" },
        LookAndFeelCell{ name: "Business" className: "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel" },
        LookAndFeelCell{ name: "Green Magic" className: "org.jvnet.substance.skin.SubstanceGreenMagicLookAndFeel" }
        
    ];
    
    public attribute rootModule:String on replace{
        initProject();
    };
    
    private attribute keyTimers:KeyTimer[];
    private attribute KEY_TIMEOUT = 600.0;
    private attribute KEY_PROCESS_TIMEOUT = 400;
    
    private attribute DEFAULT_FRAME_WIDTH  = 300.0;
    private attribute DEFAULT_FRAME_HEIGHT = 200.0;
    
    private
    function initProject(){
        //System.out.println("[init]");
        
        var moduleTreeStructure = TreeStructure{
            value: function(object: Object){
                var module = object as Module;
                return ProjectModule.createProjectModule(module);
            }
            nodes: function(object: Object): Object[]{
                var module = object as Module;
                
                var objects:Object[];
                
                for(node in module.modules){
                    insert  ProjectManager.runFXFile(node) into objects;
                }
                
                return objects;
            }
        };
        
        
        
        var moduleTreeNode = moduleTreeStructure.create( ProjectManager.runFXFile(rootModule));
        //moduleTreeNode.handle( function(value:Object) { System.out.println("value: {(value as ProjectModule).name}")} );
        
        moduleTreeNode.handle(
        function(value:Object) {
            //System.out.println("value: {(value as Module).name}")}
            
            var module = value as ProjectModule;
            
            for(sample in module.samples){
                //System.out.println("sample: {sample.name} visible: {sample.visible}");
                
                if(sample.visible){
                    executeSample(sample);
                }
            }
        }
        );
        
        treeCell = convert(moduleTreeNode);
        //  see JFX JFXC-658
        samples = (((treeCell.cells[0].cells[0].cells[1]).value) as ProjectModule ).samples;
        runKeyTimerHandler();
    }
    
    function convert(treeNode: TreeNode): TreeCell{
        var module = (treeNode.value) as ProjectModule;
        return TreeCell{
            value: module
            text: module.name
            cells: for(node in treeNode.nodes) convert(node)
        };
    }
    
    
    function runKeyTimerHandler(){
        
        var runnable = Runnable {
            public
            function run(){
                while(true) {
                    Thread.sleep(KEY_PROCESS_TIMEOUT);
                    
                    var time = System.currentTimeMillis();
                    for(keyTimer in keyTimers){
                        if ( ( keyTimer.time + KEY_TIMEOUT ) < time.doubleValue() ){
                            delete keyTimer from keyTimers;
                            keyTimer.action();
                        }
                    }
                }
            }
        };
        
        (new Thread(runnable)).start();
        
    }
    
    function createFrame(sample: ProjectSample, code: String, props: String){
        
        //System.out.println("----------------------------------------------------");
        //System.out.println("[code] {code}");
        var obj = ProjectManager.runFXCode(sample.className, code, sample.className.replaceAll("\\.", "/")+"_"+sample.propLocales.get(selectedPropIndex.intValue()).toString()+".fxproperties", props );
        
        //System.out.println("[execute sample] {sample.name}: \"" + obj + "\"");
        
        
        var internalFrame = sample.frame;
        
        if(obj == null){
            internalFrame.content = Label{ text: "Compiler Error!"};            
        }else if(obj instanceof Widget){
            
            var widget = obj as Widget;

            internalFrame.content = widget; 
            
        }else if(obj instanceof Node){
            
            var node = obj as Node;

            internalFrame.content = Canvas{ content: node}; 
            
        }else if(obj instanceof Frame){
            
            var frame = obj as Frame;
            var  bg = (frame.background).getColor();
            
            var background =  if ( bg == null) 
                    then internalFrame.background 
                    else Color{ 
                        red: bg.getRed() 
                        green: bg.getGreen() 
                        blue: bg.getBlue() 
                    }

            frame.visible = false;

            var w = frame.width;
            var h = frame.height;
            if ( 0 < w ){ internalFrame.width = w; }
            if ( 0 < h ){ internalFrame.height = h; }
            
            var title =  frame.title;
            if (title <> "") { internalFrame.title = title; }

            internalFrame.menubar = frame.menubar;
            internalFrame.content = frame.content;
            internalFrame.background = background;
            //internalFrame.onClose = frame.onClose;
            
            internalFrame.visible = true;
            
            
        } else if (obj instanceof DiagnosticCollector ){
            var diagnostics = obj as DiagnosticCollector;
            
            var errorMessage = "Compiler Error:";
            var iterator = diagnostics.getDiagnostics().iterator();
            
            while(iterator.hasNext()){
                var diagnostic = iterator.next() as Diagnostic;
                errorMessage = "{errorMessage}\nline:{diagnostic.getLineNumber()}"; 
                errorMessage = "{errorMessage}\n{diagnostic.getMessage(Locale.getDefault())}"; 
            }
            
            internalFrame.content = Label{ text: errorMessage };

         }
        
    }
    
    function executeSample(sample: ProjectSample){
        sample.project = this;

        insert sample into executedSamples;
        var className = sample.className;
        
        var fileName = ProjectManager.getFileName(className);
        
        var code = ProjectManager.readResource(className, fileName);        
        
        var textArea: EditorPane;
	var propArea: EditorPane;
        
        textArea =  EditorPane{
            contentType: ContentType.HTML
            text: code
            editable: true
            background: Color.WHITE
            onKeyUp: function(keyEvent :KeyEvent){
                previewTimer(sample, textArea.text, propArea.text)
            };
        }
        
        insert Tab{ title: sample.name content: textArea } into codeTabs;
        
	propArea = insertPropertiesTabs(sample, textArea); 

        selectedCodeIndex = sizeof codeTabs - 1;
        
        sample.frame = InternalFrame { 
            x: x 
            y: y  
            width: 175  //DEFAULT_FRAME_WIDTH 
            height: 100 //DEFAULT_FRAME_HEIGHT 
            selected: bind  sample.selected with inverse
            onClose: function(){
                //System.out.println("Close frame: {sample.name}");
                for(tab in codeTabs){
                    if(tab.title == sample.name){
                        delete tab from codeTabs;
                    }
                }
                selectedCodeIndex = sizeof codeTabs - 1;
                delete  sample.frame from frames;
                delete sample from executedSamples;
                currentPropTabs = null;
                currentPropLocales = null;

                if (0 <= selectedCodeIndex){
                    var tabTitle =  codeTabs[selectedCodeIndex.intValue()].title;
                    for(sample in samples){
                        if(sample.name ==  tabTitle) { 
                            sample.frame.selected = true; 
                            currentPropTabs = 
                                executedSamples[selectedCodeIndex.intValue()].propTabs;
                            currentPropLocales = 
                                executedSamples[selectedCodeIndex.intValue()].propLocales;
                            break; 
                        }
                    }
                }
                x = 0;
                y = 0;
                sample.frame = null;                
                sample.propTabs = null;                
                sample.propLocales = null;                
            }

            title: sample.name
            visible: true
        };
        
        x += 30;
        y += 30;
        
        insert sample.frame into frames;
        createFrame(sample, code, propArea.text);
        
    }

    function insertPropertiesTabs(sample: ProjectSample, textArea: EditorPane): EditorPane {
	var baseName = "{__DIR__}../../../{sample.className.replaceAll("\\.", "/")}";
        sample.propLocales = ProjectManager.getFXPropertiesLocales(
	    URI.create(baseName.substring(0, baseName.lastIndexOf('/'))), 
	    baseName.substring(baseName.lastIndexOf('/')+1));
        var propArea: EditorPane;

        for (index in [0..sample.propLocales.size() - 1]) {
	    var l:Locale = sample.propLocales.get(index) as Locale;
            var props = ProjectManager.readResource(sample.className, 
                ProjectManager.getFXPropertiesName(sample.className, l));
	    propArea = EditorPane {
                contentType: ContentType.HTML
                text: props
                editable: true
                background: Color.WHITE
                onKeyUp: function(keyEvent :KeyEvent){
                    previewTimer(sample, textArea.text, propArea.text)
                };
            }
            insert Tab{ title: l.getDisplayName(defLocale) 
                        content: propArea } 
		into sample.propTabs;
	}
	currentPropTabs = sample.propTabs;
	currentPropLocales = sample.propLocales;
	return sample.propTabs[selectedPropIndex.intValue()].content as EditorPane;
    }
    
    function previewTimer(sample: ProjectSample, code: String, props: String):Void {
        var timer = keyTimers[t | t.name == sample.name ];
        
        var action = function(){
                    createFrame(sample, code, 
                        (currentPropTabs[0].content as EditorPane).text);
                };
        
        if (0 < sizeof timer ){
            var keyTimer = timer[0];
            keyTimer.time = System.currentTimeMillis();
            keyTimer.action = action;
            
        } else{
            insert KeyTimer{
                name: sample.name
                time: System.currentTimeMillis()
                action: action
            } into keyTimers;
        }
    }
    
    function selectFrame(name: String):Void{
        //System.out.println("[select sample] name: {name}");
        var sample = executedSamples[s| s.name == name];
        if (sample <> [] ) {
            var f = sample[0].frame;
            if ( not f.selected) {
                f.selected = true;
            }
        }
    }
    
    function selectTab(name: String):Void{        
        //System.out.println("[select tab] title: {name}");
        for( tab in codeTabs){
            if (tab.title == name){
                var ind = indexof tab;                
                if ( ind <> selectedCodeIndex ){
                    selectedCodeIndex = ind;
                }
                currentPropTabs = executedSamples[ind].propTabs;
                currentPropLocales = executedSamples[ind].propLocales;
                if (currentPropTabs <> null) {
                    selectedPropIndex = 0;
                }
                break;
            }
        }
    }
    
    
    public
    function composeWidget(): Widget{
        return BorderPanel{

            center: SplitPane {
                orientation: Orientation.HORIZONTAL
                content:  [
                SplitView{
                    weight: 0.5
                    
                    content: SplitPane {
                        orientation: Orientation.VERTICAL
                        content: [ SplitView{
                            weight: 0.5
                            content: BorderPanel{
                                border:  TitledBorder {} 
                                top: Label { text: "Categories:"}
                                center: Tree{
                                    rootVisible: false
                                    root: treeCell
                                    selectedValue: bind selectedModule with inverse
                                    //onSelectionChange: function(){ System.out.println("[tree] selection changed!");}
                                }
                            }
                        }, SplitView{
                            weight: 0.5
                            content: BorderPanel{
                                border:  TitledBorder {} 
                                top: BorderPanel{
                                    border:  TitledBorder {}
                                    left: Label{ text: "Look and Feel"}
                                    center: ComboBox{
                                        selection: bind lafIndex with inverse
                                        cells:  for ( laf in lafs ) ComboBoxCell {
                                            text: laf.name
                                        }
                                    }
                                }
                                center: BorderPanel{ 
                                    top: Label { text: "Samples:"}
                                    center: ListBox{
                                        selection: bind selectedSampleIndex with inverse
                                        cells: bind for( sample in samples)
                                            ListCell{
                                                text: sample.name
                                            }
                                            action: function(){
                                                var sample = samples[selectedSampleIndex.intValue()];
                                                if(sample.frame ==  null){
                                                    executeSample(sample);
                                                }else{
                                                    var name = sample.name;
                                                    for(frame in frames){
                                                        System.out.println("[execute] frame: {frame.title} name: {name}");
                                                        if (frame.title == sample.frame.title ){ frame.selected = true; } else { frame.selected = false;}
                                                    }
                                                    for(tab in codeTabs ){
                                                        if(tab.title == sample.name){ selectedCodeIndex = indexof tab;
                                                        break; }
                                                    }

                                                }
                                            }
                                    }
                                }
                            }
                        } ]
                    }
                    
                },
                SplitView{
                    weight: 0.5
                    content: SplitPane {
                        orientation: Orientation.VERTICAL
                        content: [ SplitView{
                            weight: 0.6
                            content: BorderPanel{
                                center: BorderPanel { 
                                    border:  TitledBorder { title: "Preview" }
                                    center: DesktopPane{
                                        frames: bind frames
                                        background: Color.WHITE
                                    }
                                    
                                }
                            }
                        }, SplitView{
                            weight: 0.3
                            content: BorderPanel{
                                border:  TitledBorder {} 
                                center: TabbedPane{
                                    selectedIndex: bind selectedCodeIndex with inverse
                                    tabs: bind codeTabs
                                }
                            }
                        }, SplitView{
                            weight: 0.1
                            content: BorderPanel{
                                border:  TitledBorder { title: bind "String Literal Translations" }
                                center: TabbedPane{
                                    selectedIndex: bind selectedPropIndex with inverse
                                    tabs: bind currentPropTabs
                                }
                            }
                        } ]
                    }
                    
                }]
            }
            
            
            
        };
    }
}
