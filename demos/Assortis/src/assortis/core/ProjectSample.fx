/*
* ProjectSample.fx
*
* Created on May 8, 2008, 12:49:51 PM
*/

package assortis.core;


/**
* @author Alexandr Scherbatiy, Naoto Sato
*/

import java.util.Locale;
import java.lang.System;

import assortis.core.ui.*;

package function createProjectSample(sample: Sample): ProjectSample{
    ProjectSample{
        name: sample.name
        className: sample.className
        visible: sample.visible
    }
}

public class ProjectSample {
    
    public var name: String;
    public var className: String;
    public var project: AssortisProject;
    
    public var code: String;

    public var frame: MyInternalFrame;
    
    public var visible: Boolean;
    
    public var selected: Boolean on replace {
        //System.out.println("[sample] '{name}' selected: {selected}");
        if(selected){ 
            for(tab in project.codeTabs){
                if(tab.title == name){
                    project.selectedCodeIndex = indexof tab;
                    break;
                }
            }
        }
    };


    public var propertyIndex: Integer = -1 on replace{
        if( -1 < propertyIndex and propertyIndex < sizeof propertyItems){
            Locale.setDefault(getPropertyItem().locale);
            project.reloadSample(this);
        }
    };

    public var propertyItems: PropertyItem[];
    
    
    public function getPropertyItem(){
        return propertyItems[propertyIndex];
    }

    override function toString ():String { name } 

}
