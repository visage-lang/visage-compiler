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
    
    public attribute root: STreeNode;
    
    public attribute value: function(object: Object): Object;
    public attribute nodes: function(object: Object): Object[];
    
    public attribute create: function(object: Object): STreeNode = function (object: Object): STreeNode{
        return STreeNode{
            value: value(object)
            nodes: for(node in nodes(object))  create(node)
        };
    }
    

}