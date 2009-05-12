/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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

package javafx.reflect;

/**
 *
 * @author Per Bothner
 * @profile desktop
 */
public class FXSequenceValue implements FXValue {
    FXValue[] values;
    int nvalues;
    FXValue sequence;
    FXSequenceType type;

    public FXSequenceValue(FXValue[] values, int nvalues, FXType elementType) {
        this.values = values;
        this.nvalues = nvalues;
        this.type = new FXSequenceType(elementType);
    }

    protected FXSequenceValue(int nvalues, FXSequenceType type) {
        this.nvalues = nvalues;
        this.type = type;
    }

    public int getItemCount() { return nvalues; }

    public boolean isNull() { return nvalues == 0; }

    public FXValue getItem(int index) {
        return index >= 0 && index < nvalues ? values[index] : null;
    }

    public FXSequenceType getType() { return type; }

    public String getValueString() {
        if (nvalues == 0)
            return "";
        String str0 = getItem(0).getValueString();
        if (nvalues == 1)
            return str0;
        StringBuilder buf = new StringBuilder(str0);
        for (int i = 1;  i < nvalues;  i++) {
            buf.append(' ');
            buf.append(getItem(i).getValueString());
        }
        return buf.toString();
    }
}
