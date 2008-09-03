/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

/** Helper class for reflectively building sequences.
 *
 * <blockquote><pre>
 * ReflectionContext rcontext = ...;
 * RefType elementType = rcontext.getAnyType();  // Or whatever
 * SequenceBuilder builder = rcontext.makeSequenceBuilder(elementType);
 * builder.append(...);
 * builder.append(...);
 * ValueRef seq = builder.getSequence();
 * </pre></blockquote>
 */

public class SequenceBuilder {
    ValueRef[] values;
    int nvalues;
    ValueRef sequence;
    TypeRef elementType;
    ReflectionContext context;

    protected SequenceBuilder(ReflectionContext context, TypeRef elementType) {
        values = new ValueRef[16];
        this.elementType = elementType;
        this.context = context;
    }

    public void append(ValueRef value) {
        if (sequence != null)
            throw new IllegalStateException("appending to SequenceBuilder after getSequence");
        int nitems = value.getItemCount();
        if (nvalues + nitems > values.length) {
            int newSize = values.length;
            while (nvalues + nitems > newSize)
                newSize = 2 * newSize;
            ValueRef[] newValues = new ValueRef[newSize];
            System.arraycopy(values, 0, newValues, 0, nvalues);
            values = newValues;
        }
        for (int i = 0;  i < nitems;  i++) {
            ValueRef item = value.getItem(i);
            item = elementType.coerceOrNull(item);
            if (item == null)
                throw new ClassCastException("cannot coerce to "+elementType);
            values[nvalues++] = item;
        }
    }

    /** Get the final sequence result. */
    public ValueRef getSequence() {
        if (sequence == null) {
            sequence = context.makeSequenceValue(values, nvalues, elementType);
            values = null;
        }
        return sequence;
    }

    static class SequenceValue extends ValueRef {
        ValueRef[] values;
        int nvalues;
        ValueRef sequence;
        SequenceTypeRef type;
        public SequenceValue(ValueRef[] values, int nvalues, TypeRef elementType) {
            this.values = values;
            this.nvalues = nvalues;
            this.type = new SequenceTypeRef(elementType);
        }
        public int getItemCount() { return nvalues; }
        public boolean isNull() { return nvalues == 0; }
        public ValueRef getItem(int index) {
            return index >= 0 && index < nvalues ? values[index] : null;
        }
        public SequenceTypeRef getType() { return type; }

        public String getValueString() {
            if (nvalues == 0)
                return "";
            String str0 = values[0].getValueString();
            if (nvalues == 1)
                return str0;
            StringBuilder buf = new StringBuilder(str0);
            for (int i = 1;  i < nvalues;  i++) {
                buf.append(' ');
                buf.append(values[i].getValueString());
            }
            return buf.toString();
        }
    }
}

