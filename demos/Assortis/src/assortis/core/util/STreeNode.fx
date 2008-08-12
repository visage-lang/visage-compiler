/*
 * TreeNode.fx
 *
 * Created on Jan 28, 2008, 4:19:50 PM
 */

package assortis.core.util;

/**
 * @author Alexandr Scherbatiy
 */

import java.lang.Object;

public class STreeNode {
    
    public var value: Object;
    public var nodes: STreeNode[];
 
    public var handle: function(handler: function(value: Object)) = function(handler: function(value: Object)) {
        handler(value);
        for (node in nodes){
            node.handle(handler);
        }
    }
    
    //public var convert: function(): Object;
}
