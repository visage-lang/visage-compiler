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

package visage.reflect;

/** Helper class for reflectively building sequences.
 *
 * <blockquote><pre>
 * VisageContext rcontext = ...;
 * RefType elementType = rcontext.getAnyType();  // Or whatever
 * VisageSequenceBuilder builder = rcontext.makeSequenceBuilder(elementType);
 * builder.append(...);
 * builder.append(...);
 * VisageValue seq = builder.getSequence();
 * </pre></blockquote>
 *
 * @author Per Bothner
 * @profile desktop
 */

public class VisageSequenceBuilder {
    VisageValue[] values;
    int nvalues;
    VisageValue sequence;
    VisageType elementType;
    VisageContext context;

    protected VisageSequenceBuilder(VisageContext context, VisageType elementType) {
        values = new VisageValue[16];
        this.elementType = elementType;
        this.context = context;
    }

    public void append(VisageValue value) {
        if (sequence != null)
            throw new IllegalStateException("appending to SequenceBuilder after getSequence");
        int nitems = value.getItemCount();
        if (nvalues + nitems > values.length) {
            int newSize = values.length;
            while (nvalues + nitems > newSize)
                newSize = 2 * newSize;
            VisageValue[] newValues = new VisageValue[newSize];
            System.arraycopy(values, 0, newValues, 0, nvalues);
            values = newValues;
        }
        for (int i = 0;  i < nitems;  i++) {
            VisageValue item = value.getItem(i);
            item = elementType.coerceOrNull(item);
            if (item == null)
                throw new ClassCastException("cannot coerce to "+elementType);
            values[nvalues++] = item;
        }
    }

    /** Get the final sequence result. */
    public VisageValue getSequence() {
        if (sequence == null) {
            sequence = context.makeSequenceValue(values, nvalues, elementType);
            values = null;
        }
        return sequence;
    }
}
