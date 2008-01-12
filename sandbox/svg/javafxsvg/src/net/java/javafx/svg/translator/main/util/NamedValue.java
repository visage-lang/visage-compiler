/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.main.util;

public class NamedValue {
    private String name;
    private String value;
    
    public NamedValue() {}
    
    public NamedValue(String name, String value) {
        setName(name);
        setValue(value);
    }
    
    public String toString() {
        return name + ": " + value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
