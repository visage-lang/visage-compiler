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
    
    private attribute codeTabs:Tab[];
    private attribute selectedCodeIndex: Number;
    private attribute code:String;
    
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
        samples = (((treeCell.cells[0].cells[0].cells[0]).value) as ProjectModule ).samples;
    }
    
    function convert(treeNode: TreeNode): TreeCell{
        var module = (treeNode.value) as ProjectModule;
        return TreeCell{
            value: module
            text: module.name
            cells: for(node in treeNode.nodes) convert(node)
        };
    }
    
    
    function executeSample(sample: ProjectSample){
        
        //insert InternalFrame{} into frameList;
        
        var className = sample.className;
        var obj = ProjectManager.runFXFile(className);
        
        var fileName = className.substring(className.lastIndexOf('.') + 1) + ".fx";
        
        code = ProjectManager.readResource(className, fileName);
        
        insert Tab{
            title: sample.name
            //content: Label{ text: "MyLabel!"}
            content: TextArea{
                text: code
                editable: false
                background: Color.WHITE
            }
        } into codeTabs;
        
        selectedCodeIndex = sizeof codeTabs - 1;
        
        
        
        var internalFrame: InternalFrame;
        
        if(obj instanceof Frame){
            
            var frame = obj as Frame;
            var background = (frame.background).getColor();
            //if(background==null){  background = Color.WHITE; }
            //System.out.println("background: {background}");
            
            
            internalFrame =  InternalFrame{
                x: x
                y: y
                title: frame.title
                width: frame.width
                height: frame.height
                onClose: function(){ System.out.println("Close frame: {sample.name}");
                sample.frame = null; }
                content: frame.content
                background: if (background==null) then Color.WHITE else Color{ red: background.getRed() green: background.getGreen() blue: background.getBlue() }
            };
            frame.visible = false;
            
            x += 40;
            y += 40;
            
        }
        
        insert internalFrame into frames;
        sample.frame = internalFrame;
        
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
                                    //selectedValue: bind val
                                }
                            }
                        }, SplitView{
                            weight: 0.5
                            content: BorderPanel{
                                top: Label { text: "Samples"}
                                center: ListBox{
                                    var n = 0.0
                                    selection: bind n with inverse
                                    cells: bind for( sample in samples)
                                        ListCell{
                                            text: sample.name
                                        }
                                        action: function(){
                                            var sample = samples[n.intValue()];
                                            System.out.println("Sample: {sample.name}");
                                            //if(sample.frame ==  null){
                                            executeSample(sample);
                                            //}
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
                                    selectedIndex: bind selectedCodeIndex
                                    tabs: bind codeTabs
                                }
                        } ]
                    }
                    
                }]
            }
            
            
            
        };
    }
}