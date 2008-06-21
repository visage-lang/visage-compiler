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

package assortis.core;

/**
* @author Alexandr Scherbatiy, Naoto Sato
*/

import java.io.*;


import java.lang.Object;
import java.lang.System;

import javafx.gui.*;
import javafx.gui.swing.*;
import javafx.gui.swing.Layout.*;

import assortis.core.ui.*;
import assortis.core.util.*;

import java.util.Locale;
import java.awt.ComponentOrientation;


public class AssortisProject  extends MyCompositeComponent{


    public attribute rootModule:String on replace{ initProject(); };
    
    private attribute debug: Boolean = true;
    public attribute editable: Boolean = true;
    
    private attribute timer: Timer;
    private attribute defaultLocale:Locale;

    private attribute screenX: Integer;
    private attribute screenY: Integer;
    
    protected attribute loadedSamples:ProjectSample[];

    
    protected attribute rootNode: MyTreeCell;
    protected attribute codeTabs:MyTab[]; 
    protected attribute frames: MyInternalFrame[];
    

    protected attribute selectedCodeIndex: Integer on replace{
        if( -1 < selectedCodeIndex and selectedCodeIndex < sizeof codeTabs){
            var title = codeTabs[selectedCodeIndex].title;
            for(sample in loadedSamples){
                if(sample.name == title){
                    sample.frame.selected = true;
                    break;
                }
            }        
        }
    };
    
    private function initProject(){
        out("init");
        debug = true;
        defaultLocale = Locale.getDefault();

        var moduleSTree = STree{
            value: function(object: Object){ ProjectModule.createProjectModule(object as Module) }
            nodes: function(object: Object): Object[]{

                var module = object as Module;

                var objects:Object[];

                for(node in module.modules){
                    insert  ProjectManager.runFXFile(node) into objects;
                }
                return objects;
            }
        };
        var moduleSTreeNode = moduleSTree.create( ProjectManager.runFXFile(rootModule));
        //moduleSTreeNode.handle( function(value:Object) { out(" STree node: {(value as ProjectModule).name}")} );

        moduleSTreeNode.handle(
            function(value:Object) {
                var module = value as ProjectModule;
                for(sample in module.samples){
                    if(sample.visible){
                        loadSample(sample);
                    }
                }
            }
        );


        
        rootNode = convertModuleTree(moduleSTreeNode);
        
        timer = Timer{};
        timer.start();
        
    }

    function convertModuleTree(STreeNode: STreeNode): MyTreeCell{
        var module = (STreeNode.value) as ProjectModule;
        return MyTreeCell{
            value: module
            text: module.name
            cells: [
                    for(node in STreeNode.nodes) convertModuleTree(node),
                    for(sample in module.samples) { MyTreeCell{ text: sample.name, value: sample } } 
                   ]
        };
    }

    
    protected function loadSample (sample: ProjectSample) {
        //out();
        //out("load sample: '{sample}'");
        sample.project = this;
        
        insert sample into loadedSamples;
        
        var className = sample.className;

        sample.code = ProjectManager.readResource(className);

        //out("code: {sample.code}");

        var localeItems = ProjectManager.getFXPropertiesLocales(sample.className);
        
        var iterator  = localeItems.iterator();
        while(iterator.hasNext()){
            insert PropertyItem.createItem((iterator.next() as LocaleItem)) into sample.propertyItems;
        }
  
        
        insert MyTab{ 
            title: sample.name 
            content: MySplitPane{
                weight: 0.7
                one: MyEditorPane{
                    editable: bind editable
                    text: bind sample.code with inverse
                    onKeyUp: function(keyEvent :KeyEvent){
                                timer.addTask(sample.name, function():Void{ reloadSample(sample) } 
                            );
                    } 
                }
                two: ScrollPane{ 
                    scrollable: false 
                    view: BorderPanel{
                        top: Label { text: "String Literal Translations:"}
                        center:  MyTabbedPane{
                            selectedIndex: bind sample.propertyIndex with inverse
                            tabs: for (item in  sample.propertyItems ) MyTab{
                                title: item.locale.getDisplayName(defaultLocale);
                                content: MyEditorPane{
                                    editable: bind editable
                                    text: bind item.text with inverse
                                    onKeyUp: function(keyEvent :KeyEvent){
                                                timer.addTask(sample.name, function():Void{ reloadSample(sample) } 
                                            );
                                    } 
                                }
                            }
                        }
                    } 
                }
            }  
        } into codeTabs;
    
        selectedCodeIndex = sizeof codeTabs - 1;
        
        
        sample.frame = MyInternalFrame {
            x: screenX
            y: screenY
            title: sample.name
            selected: bind sample.selected with inverse
            onClose: function(){
                for(tab in codeTabs){
                    if(tab.title == sample.name) { delete tab from codeTabs;}
                }
                delete sample from loadedSamples;
                sample.frame = null;
            }
        };

        screenX += 30;
        screenY += 30;

        insert sample.frame into frames;

        reloadSample(sample); 
        
    } 
    
    protected function reloadSample (sample: ProjectSample) {
        //out("reload sample: '{sample}'");

        var propertyItem = sample.getPropertyItem();
        
        var obj = if (editable) then {
            ProjectManager.runFXCode(sample.className, sample.code, propertyItem.getName(), propertyItem.text );
        }else{
            ProjectManager.runFXFile(sample.className);
        }   
         
        
        var unit = FXUnit.createUnit(obj);
        var internalFrame = sample.frame;
        
        var title =  unit.title;
        if (title != "") { internalFrame.title = title; }

        if ( unit.isWindow ){
            internalFrame.width = unit.width;
            internalFrame.height = unit.height;
        } else if (internalFrame.height == 0 or internalFrame.height == 0) {
            internalFrame.width = unit.width;
            internalFrame.height = unit.height;
        }
    
    
        internalFrame.menus = unit.menus;
        internalFrame.content = unit.content;
        internalFrame.getJComponent().applyComponentOrientation( ComponentOrientation.getOrientation(Locale.getDefault()));
        internalFrame.background = unit.background;
        internalFrame.visible = true;
        internalFrame.selected = true;

    }
    
    
    private function out(){
        if(debug){ System.out.println();}
    }

    private function out(obj: Object){
        out("{obj}");
    }
    
    private function out(text: String){
        if(debug){ System.out.println("[assortis] {text}");}
    }
    
    public function composeComponent(): Component{
        return BorderPanel{
        
        center: MySplitPane {
            weight: 0.2
            orientation: Orientation.HORIZONTAL
            one:  BorderPanel{
                    top: Label { text: "Samples:"}
                    center: MyTree{
                        //rootVisible: false
                        root: bind rootNode
                        onMouseClicked: function(e: MyMouseEvent){

                                var treeCell = e.source as MyTreeCell;
                                if ((e.clickCount == 2) and  ( treeCell.value instanceof ProjectSample)){
                                    var sample = treeCell.value as ProjectSample;
                                    if(sample.frame ==  null){ loadSample(sample); } else{ sample.selected = true; }
                                }
                            }
                        }
                    }


        two: MySplitPane {
                weight: 0.9
                orientation: Orientation.VERTICAL
                one: BorderPanel{
                        center: BorderPanel {
                            center: MyDesktopPane{
                                frames: bind frames
                            }
                        }
                    }
                two: BorderPanel{
                        center: MyTabbedPane{
                            selectedIndex: bind selectedCodeIndex with inverse
                            tabs: bind codeTabs
                        } 
                    }
                
            }
        }
        };
    }
}


