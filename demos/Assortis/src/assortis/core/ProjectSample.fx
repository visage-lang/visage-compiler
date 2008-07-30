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

public class ProjectSample {
    
    public attribute name: String;
    public attribute className: String;
    public attribute project: AssortisProject;
    
    public attribute code: String;

    public attribute frame: MyInternalFrame;
    
    public attribute visible: Boolean;
    
    public attribute selected: Boolean on replace {
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


    public attribute propertyIndex: Integer = -1 on replace{
        if( -1 < propertyIndex and propertyIndex < sizeof propertyItems){
            Locale.setDefault(getPropertyItem().locale);
            project.reloadSample(this);
        }
    };

    public attribute propertyItems: PropertyItem[];
    
    
    public function getPropertyItem(){
        return propertyItems[propertyIndex];
    }
    
    static function createProjectSample(sample: Sample): ProjectSample{
        ProjectSample{
            name: sample.name
            className: sample.className
            visible: sample.visible
        }
    }
    
    override function toString ():String { name } 

}
