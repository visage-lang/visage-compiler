/*
 * TreeNode.fx
 *
 * Created on Jan 28, 2008, 4:19:50 PM
 */

package jfx.assortis.system.structure;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.Object;

public class TreeNode {
    public attribute value: Object;
    public attribute nodes: TreeNode[];
 
    public attribute handle: function(handler: function(value: Object)) = function(handler: function(value: Object)) {
        handler(value);
        for (node in nodes){
            node.handle(handler);
        }
    }
    
    //public attribute convert: function(): Object;
}
