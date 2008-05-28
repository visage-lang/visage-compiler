
package assortis.core.ui;


import javafx.gui.*;
import javax.swing.*;
import javax.swing.tree.*;

import javafx.gui.Layout.*;

import java.lang.System;


public class MyTree extends Component{
    
    private attribute tree: JTree;
    private attribute model: DefaultTreeModel;
    
    public attribute selectedValue: java.lang.Object;
    
    public attribute onMouseClicked: function(e:MyMouseEvent);
    
    
    public attribute root: MyTreeCell on replace{
        //System.out.println("[tree cell] {root}");
        if (root <> null){
            //System.out.println("[tree cell] {root.text}");
            //(tree.getModel() as DefaultTreeModel).setRoot(getCell(root));

            model.setRoot(getCell(root));
                
            var obj = model.getRoot();
            var tp = new TreePath(obj);
            expandTree(tp, obj);
        }
    };

    private function expandTree( treePath: TreePath, obj: java.lang.Object ):Void{
        //System.out.println("[tree] expand path: {treePath}");
        tree.expandPath(treePath);
        
        for(i in [0..model.getChildCount(obj) - 1]){
            var child = model.getChild(obj, i);
            //if( 0 < model.getChildCount(child)){
                expandTree( new MyTreePath(treePath, child), child);
            //}
        }
    }

    
    

    protected function createJComponent(): JComponent {
        tree = new JTree();
        model = tree.getModel() as DefaultTreeModel;

        var  mouseListener =  java.awt.event.MouseAdapter {
            public function mousePressed(e: java.awt.event.MouseEvent) {
                var selRow = tree.getRowForLocation(e.getX(), e.getY());
                var selPath = tree.getPathForLocation(e.getX(), e.getY());
                var lastPath = selPath.getPathComponent(selPath.getPathCount() - 1);
                var obj = (lastPath as DefaultMutableTreeNode).getUserObject(); 

                if(onMouseClicked <> null ){
                    onMouseClicked(MyMouseEvent{ source: obj clickCount: e.getClickCount()});
                }
                
                if(selRow <> -1) {
                    if(e.getClickCount() == 1) {
                        selectedValue = obj; 
                        //System.out.println("[tree] Single Click: {selectedValue}");
                    }
                    else if(e.getClickCount() == 2) {
                        //System.out.println("[tree] Double Click");
                }
            }
        }
        };

        tree.addMouseListener(mouseListener);    

        tree.setRootVisible(false);
        //tree.setModel(model);
        var scrollPane = new JScrollPane(tree);
        return scrollPane;

        //return tree;

    }

        private function getCell(cell: MyTreeCell): MutableTreeNode {
        //var node = new DefaultMutableTreeNode(cell.text);
        //var node = new DefaultMutableTreeNode(cell.value);

        //System.out.println("[tree] set node: {cell},  value: {cell.value}");
        var node = new DefaultMutableTreeNode(cell);
        //System.out.println("       user object: {node.getUserObject()}, {node.getUserObject().getClass().getName()}");
        //node.setUserObject(cell);

        //for (c  in  (reverse cell.cells) ){
        for (c  in  cell.cells ){
        node.<<insert>>(getCell(c),node.getChildCount());
    }
        return node;
    }
    
}

