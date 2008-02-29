/*
 * AssortieProject.fx
 *
 * Created on Feb 11, 2008, 5:55:40 PM
 */

package jfx.assortie.system;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.Object;
import java.lang.System;
import java.lang.Class;
import java.lang.StringBuffer;
import java.io.*;

import javafx.ui.*;
import jfx.assortie.system.structure.*;


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
    public attribute visible: Boolean;
    public attribute selected: Boolean on replace {
        if(selected){
            frame.selected = true;
        }
    } ;
    
    public attribute frame: InternalFrame;
    //attribute isExecuted: Boolean;
    
    
    static
    function createProjectSample(sample: Sample): ProjectSample{
        return ProjectSample{
            name: sample.name
            className: sample.className
            visible: sample.visible
        };
    }
}



public class AssortieProject  extends CompositeWidget{
    
    private attribute treeNode: TreeNode;
    
    private attribute treeCell: TreeCell;
    private attribute frames: InternalFrame[];
    private attribute samples:ProjectSample[];
    private attribute executedSamples:ProjectSample[];
    
    private attribute codeTabs:Tab[];
    
    private attribute selectedCodeIndex: Number on replace{
        
        if ( codeTabs <> [] and 0 <= selectedCodeIndex ){
              var title = codeTabs[selectedCodeIndex.intValue()].title;
              tabSelection(title);
        }
    };
    
    private attribute selectedSampleIndex: Integer;

    //private attribute code:String;
    
    private attribute selectedModule:Object on replace{
        System.out.println("[selected module] {selectedModule}");
        selectedSampleIndex = -1;

        var cell = selectedModule as TreeCell;
        var m = cell.value as ProjectModule;
        samples = m.samples;
        selectedSampleIndex = sizeof samples - 1;
        System.out.println("[selected value] {m}");
        
    };

    
    public attribute rootModule:String on replace{
        initProject();
    };
    
    
    private
    function initProject(){
        System.out.println("[init]");
        
        var moduleTreeStructure = TreeStructure{
            value: function(object: Object){
                var module = object as Module;
                
                //return object;
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
        moduleTreeNode.handle(
        function(value:Object) { System.out.println("value: {(value as ProjectModule).name}")} );
        
        moduleTreeNode.handle(
        function(value:Object) {
            //System.out.println("value: {(value as Module).name}")}
            
            var module = value as ProjectModule;
            
            for(sample in module.samples){
                System.out.println("sample: {sample.name} visible: {sample.visible}");
                
                if(sample.visible){
                    executeSample(sample);
                }
            }
        }
        );
        
        treeCell = convert(moduleTreeNode);
        //  see JFX JFXC-658
        samples = (((treeCell.cells[0].cells[0].cells[1]).value) as ProjectModule ).samples;
    }
    
    function convert(treeNode: TreeNode): TreeCell{
        var module = (treeNode.value) as ProjectModule;
        return TreeCell{
            value: module
            text: module.name
            cells: for(node in treeNode.nodes) convert(node)
        };
    }
    
    
    function createFrame(sample: ProjectSample, code: String){

        System.out.println("----------------------------------------------------");
        System.out.println("[code] {code}");
        var obj = ProjectManager.runFXCode(sample.className, code);

        System.out.println("[execute sample] {sample.name}:" + obj);

        
        var internalFrame = sample.frame;
        
        if(obj instanceof Frame){
            
            var frame = obj as Frame;
            var background = (frame.background).getColor();
            //if(background==null){  background = Color.WHITE; }
            //System.out.println("background: {background}");
            frame.visible = false;
            
            
            internalFrame.title = frame.title;
            internalFrame.width = frame.width;
            internalFrame.height = frame.height;
            //internalFrame.x = x;
            //internalFrame.y = y;            
            internalFrame.content = frame.content;
            internalFrame.visible = true;
 
            var content = frame.content;
            if ( content instanceof Label){
                var label = content as Label;
                System.out.println("[label ] \"{label.text}\"");
                
            }
            System.out.println("[content] {content}");

            
            
//            internalFrame =  InternalFrame{
//                x: x
//                y: y
//                title: frame.title
//                width: frame.width
//                height: frame.height
//                onClose: function(){ 
//                    System.out.println("Close frame: {sample.name}");
//                    sample.frame = null; 
//                    for(tab in codeTabs){
//                        if(tab.title == sample.name){
//                            delete tab from codeTabs;
//                        }
//                    }
//                    selectedCodeIndex = sizeof codeTabs - 1;
//                    delete internalFrame from frames;
//                    delete sample from executedSamples;
//                    
//                    if (0 <= selectedCodeIndex){
//                        var tabTitle =  codeTabs[selectedCodeIndex.intValue()].title;
//                        for(sample in samples){
//                            if(sample.name ==  tabTitle) { sample.frame.selected = true; break; }
//                        }
//                    }
//                }
//                content: frame.content
//                background: if (background==null) then Color.WHITE else Color{ red: background.getRed() green: background.getGreen() blue: background.getBlue() }
//            };
//            frame.visible = false;
//            
//            x += 40;
//            y += 40;
            
        }
        
        //insert internalFrame into frames;        
    }
    
    function executeSample(sample: ProjectSample){
        
        insert sample into executedSamples;
        var className = sample.className;
                
        //var fileName = className.substring(className.lastIndexOf('.') + 1) + ".fx";
        var fileName = ProjectManager.getFilePath(className);
        
        var code = ProjectManager.readResource(className, fileName);

        //var obj = ProjectManager.runFXFile(className);
        //var obj = ProjectManager.runFXCode(className, code);
        
        
        var textArea: TextArea;
        
        textArea =  TextArea{
                text: code
                editable: true
                background: Color.WHITE
                onKeyUp: function(keyEvent :KeyEvent){
                    createFrame(sample, textArea.text);
                };
            }
            
        insert Tab{ title: sample.name content: textArea } into codeTabs;
        
        selectedCodeIndex = sizeof codeTabs - 1;
                        
        //var internalFrame: InternalFrame;
        
        
        sample.frame = InternalFrame { x: x y: y};
        x += 40;
        y += 40;

        insert sample.frame into frames;
        createFrame(sample, code);

        
//        if(obj instanceof Frame){
//            
//            var frame = obj as Frame;
//            var background = (frame.background).getColor();
//            //if(background==null){  background = Color.WHITE; }
//            //System.out.println("background: {background}");
//            
//            internalFrame =  InternalFrame{
//                x: x
//                y: y
//                title: frame.title
//                width: frame.width
//                height: frame.height
//                onClose: function(){ 
//                    System.out.println("Close frame: {sample.name}");
//                    sample.frame = null; 
//                    for(tab in codeTabs){
//                        if(tab.title == sample.name){
//                            delete tab from codeTabs;
//                        }
//                    }
//                    selectedCodeIndex = sizeof codeTabs - 1;
//                    delete internalFrame from frames;
//                    delete sample from executedSamples;
//                    
//                    if (0 <= selectedCodeIndex){
//                        var tabTitle =  codeTabs[selectedCodeIndex.intValue()].title;
//                        for(sample in samples){
//                            if(sample.name ==  tabTitle) { sample.frame.selected = true; break; }
//                        }
//                    }
//                }
//                content: frame.content
//                background: if (background==null) then Color.WHITE else Color{ red: background.getRed() green: background.getGreen() blue: background.getBlue() }
//            };
//            frame.visible = false;
//            
//            x += 40;
//            y += 40;
//            
//        }
//        
//        insert internalFrame into frames;
//        sample.frame = internalFrame;
//        insert internalFrame into frames;
        
    }

    function tabSelection(name: String):Void{
        //System.out.println("[select sample] name: {name}");
        var sample = executedSamples[s| s.name == name];
        if (sample <> [] ) { 
            sample[0].frame.selected = true;
        }   
    }
    
    function frameSelection(title: String):Void{
        
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
                                top: Label { text: "Categories:"}
                                center: Tree{
                                    rootVisible: false
                                    root: treeCell
                                    selectedValue: bind selectedModule with inverse
                                    onSelectionChange: function(){ System.out.println("[tree] selection changed!");} 
                                }
                            }
                        }, SplitView{
                            weight: 0.5
                            content: BorderPanel{
                                top: Label { text: "Samples"}
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
                                                    if(tab.title == sample.name){ selectedCodeIndex = indexof tab; break; }
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
                                border:  TitledBorder { title: "Preview" }
                                center: DesktopPane{
                                    frames: bind frames
                                    background: Color.WHITE
                                    
                                }
                            }
                        }, SplitView{
                            weight: 0.4
                            content:
                                TabbedPane{
                                    selectedIndex: bind selectedCodeIndex with inverse
                                    tabs: bind codeTabs
                                }
                        } ]
                    }
                    
                }]
            }
            
            
            
        };
    }
}