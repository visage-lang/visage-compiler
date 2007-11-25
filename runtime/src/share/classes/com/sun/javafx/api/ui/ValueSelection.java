/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.api.ui;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.util.*;

/**
 *  Class that supports java File List, String, and Object flavors
 */
public class ValueSelection implements Transferable, ClipboardOwner {

    private Object data;
    static final DataFlavor OBJECT_FLAVOR = makeObjectFlavor();
    static DataFlavor makeObjectFlavor() {
        try {
            return new DataFlavor(Object.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private DataFlavor[] flavors = {
        DataFlavor.stringFlavor,
        DataFlavor.javaFileListFlavor,
        OBJECT_FLAVOR
    };
						   
    public ValueSelection(Object data) {
        this.data = data;
    }

    public DataFlavor[] getTransferDataFlavors() {
        List<DataFlavor> list = new LinkedList<DataFlavor>();
        for (int i = 0; i < flavors.length; i++) {
            if (isDataFlavorSupported(flavors[i])) {
                list.add(flavors[i]);
            }
        }
        DataFlavor[] result = new DataFlavor[list.size()];
        list.toArray(result);
        return result;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor.equals(DataFlavor.javaFileListFlavor)) {
            if (data != null  && data instanceof List) {
                List dataList = (List)data;  
                for (int i = 0, count = dataList.size(); i < count; i++) {
                    if (dataList.get(i) instanceof java.io.File) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        } else if (flavor.equals(DataFlavor.stringFlavor)) {
            if (data != null) {
                if (data instanceof String) {
                    return true;
                }
            } else {
                return true;
            }
        } else if (flavor.equals(OBJECT_FLAVOR)) {
            return true;
        }
        return false;
    }

    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException
    {
        if (flavor == DataFlavor.javaFileListFlavor) {
            if (data != null) {
                List<File> result = new LinkedList<File>();
                if(data instanceof List) {
                    List listData =  (List)data;
                    for (int i = 0, count = listData.size(); i < count; i++) {
                        Object obj = listData.get(i);
                        if (obj instanceof File) {
                            result.add((File)obj);
                        }
                    }
                }else if( data.getClass().isArray()) {
                    Object[] objList = (Object[]) data;
                    for (int i = 0, count = objList.length; i < count; i++) {
                        Object obj = objList[i];
                        if (obj instanceof File) {
                            result.add((File)obj);
                        }
                    }
                    
                }
                return result;
            }
        } else if (flavor == DataFlavor.stringFlavor) {
            if(data instanceof String) {
                return (String)data;
            }
        } else if (flavor.equals(OBJECT_FLAVOR)) {
            if (data != null) {
                return data;
            }
        }
        throw new UnsupportedFlavorException(flavor);
    }

    public Object getValue() {
        return data;
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
