/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javafx.tools.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

        public class ViewTransferable implements Transferable {
            private Object value;

            DataFlavor[] flavors = new DataFlavor[]{DataFlavor.stringFlavor};

            //public ViewTransferable() {
            //}
            
            public ViewTransferable(Object value){
                this.value = value;
            }


            public DataFlavor[] getTransferDataFlavors() {
                return flavors;
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
//                if (flavor.equals(flavors[0])) {
//                    return true;
//                }
//                return false;
                return true;
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
//                if (!isDataFlavorSupported(flavor)) {
//                    System.out.println("unsuported flavor");
//                    return null;
//                }
//                if (flavor.equals(flavors[0])) {
//                    return (null);
//                }
                return value;
            }
        }