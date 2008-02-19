/*
 * TreeStructure.fx
 *
 * Created on Jan 28, 2008, 4:19:42 PM
 */

package jfx.assortie.system.structure;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.Object;

public class TreeStructure {
    
    public attribute root: TreeNode;
    
    public attribute value: function(object: Object): Object;
    public attribute nodes: function(object: Object): Object[];
    
    public attribute create: function(object: Object): TreeNode = function (object: Object): TreeNode{
        return TreeNode{
            value: value(object)
            nodes: for(node in nodes(object))  create(node)
        };
    }
    

}