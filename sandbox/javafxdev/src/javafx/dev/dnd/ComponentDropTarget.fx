/*
 * ComponentDropTarget.fx
 *
 * Created on 01.06.2008, 21:15:32
 */

package javafx.dev.dnd;

import javafx.dev.*;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JComponent;

import java.lang.System;

public class ComponentDropTarget extends DropTargetListener {

    public attribute component: DevComponent on replace{
        var dropTarget = new DropTarget(component.getJComponent(), DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
    };


    public function dragOver(dtde: DropTargetDragEvent ) {
        acceptOrRejectDrag(dtde);
    }

    public function dropActionChanged(dtde: DropTargetDragEvent ) {
        acceptOrRejectDrag(dtde);
    }

    public function dragExit(dtde: DropTargetEvent ) {
    }

    public function drop(dtde: DropTargetDropEvent ) {        
        var transferable = dtde.getTransferable();
        var obj = transferable.getTransferData(DataFlavor.stringFlavor);
        var drop = component.drop;
        if(drop != null){
            drop(obj);
        }
    }

    public function dragEnter(dtde: DropTargetDragEvent ) {
    }

    function acceptOrRejectDrag(dtde: DropTargetDragEvent ):Boolean {
        return true;
    }

    function checkTransferType(dtde: DropTargetDragEvent ) {
        //acceptableType = dtde.isDataFlavorSupported(DataFlavor.stringFlavor);
    }


}