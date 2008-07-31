/*
* ProjectModule.fx
*
* Created on May 8, 2008, 12:47:51 PM
*/

package assortis.core;

/**
* @author Alexandr Scherbatiy
*/

package function createProjectModule(module: Module): ProjectModule{
    ProjectModule{
        name: module.name
        samples: for(sample in module.samples) ProjectSample.createProjectSample(sample)
    }
}

public class ProjectModule {
    
    attribute name: String;
    public attribute samples: ProjectSample[];
    
    override function toString ():String { name } 

}
