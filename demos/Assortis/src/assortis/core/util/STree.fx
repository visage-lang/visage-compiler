/*
 * TreeStructure.fx
 *
 * Created on Jan 28, 2008, 4:19:42 PM
 */

package assortis.core.util;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.Object;

public class STree {
    
    public var root: STreeNode;
    
    public var value: function(object: Object): Object;
    public var nodes: function(object: Object): Object[];
    
    public var create: function(object: Object): STreeNode = function (object: Object): STreeNode{
        return STreeNode{
            value: value(object)
            nodes: for(node in nodes(object))  create(node)
        };
    }
    

}
