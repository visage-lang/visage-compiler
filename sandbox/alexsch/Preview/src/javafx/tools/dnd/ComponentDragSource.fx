/*
 * ComponentDragSource.fx
 *
 * Created on 01.06.2008, 19:59:45
 */

package javafx.tools.dnd;

import javafx.tools.*;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.JComponent;

import java.lang.System;

public class ComponentDragSource extends DragGestureListener, DragSourceListener {

    public attribute component: ToolComponent on replace{
        var dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(component.getJComponent(), DnDConstants.ACTION_COPY_OR_MOVE, this);
    };

    public function dragGestureRecognized(dge: DragGestureEvent) {
        //System.out.println("[drag] Value");
        //Transferable transferable = new ViewTransferable();
        var drag = component.drag;
        if(drag <> null){
            dge.startDrag(null, new ViewTransferable(drag()), this);
            
        }
    }

    public function dropActionChanged(dsde: DragSourceDragEvent) {
    }

    public function dragEnter(dsde: DragSourceDragEvent) {
    }

    public function dragOver(dsde: DragSourceDragEvent) {
    }

    public function dragExit(dsde: DragSourceEvent) {
    }

    public function dragDropEnd(dsde: DragSourceDropEvent) {
    }

}