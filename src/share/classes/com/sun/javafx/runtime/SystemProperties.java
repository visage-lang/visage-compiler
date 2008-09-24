/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime;

import java.util.Properties;

public class  SystemProperties {
	 
   /**
    * JavaFX System Properties table.
    * First column represents javafx property name with "javafx" prefix stripped off.
    * Second column represents underlying runtime platform equivalent. 
    * "jfx_specific" value in the runtime platform equivalent field indicates the property is JavaFX specific.
    * Empty string in   the runtime platform equivalent field indicates thete is no equivalent property for given platform.
    */
    private static String[] sysprop_table = {
    };


    /**
     * JavaFX Specific System Properties table.
     * First column represents javafx environment specific property name with "javafx" prefix stripped off.
     * Second column represents value of the property 
    */
    private static String[] jfxprop_table = {
    };

    private static Properties sysprop_list = new Properties();  
    private static Properties jfxprop_list = new Properties();


    static {
        addProperties (sysprop_table, false);
        addProperties (jfxprop_table, true);
    }


    /** 
     * Registers a statically allocated System Properties table 
     * Once registered properties listed in the table are availabe for inquiry through FX.getProperty().
     * Table is defined as a String array with JavaFX property name followed by property value or property mapping identifier
     * depending on whether the table contains JavaFX specific properties or not.
     * Note that JavaFX property names have "javafx" stripped out to optimize table lookup.
     * The following identifiers are available:
     * </p>
     * 1. Underlying runtime platform property name. When listed, FX.getProperty() will invoke System.getProperty()
     *    method to retrieve property value.
     *    example:
     *    {"version", "java.version"}
     * </p>   
     * 2. "javafx_specific". When listed indicates there is no association between the property and underlying runtime
     *    platform. Rather the property is JavaFX specific. In that case another table needs to be provided with values
     *    for all JavaFX specific properties. JavaFX specific properties table is a string array containing property name
     *    and corresponding property value.
     *    example:
     *    {"hw.radio", "none"} 
     * </p>     
     * 3. Empty string. When listed, the meaning there is no association between the property and underlying runtime 
     *    platform nor the property is JavaFX specific. FX.getProperty() invoked on that property returns null.
     *    example:
     *    {"supports.mixing", "none"} 
     * @param table System Properties table
     * @param jfx_specific Indicates the table contains JavaFX specific properties
     */      
    public static void addProperties (String[] table, boolean jfx_specific) {
        if (table == null)
            return;
		         
        Properties props;
		            
        if (jfx_specific) {
            props = jfxprop_list;
        } else {
            props = sysprop_list;
        }
			          
        for (int i=0; i<table.length; i+=2) {
            props.setProperty(table[i], table[i+1]);
        }
    } 

    public static String getProperty (String key) {
	Properties props = sysprop_list;
	final String prefix = "javafx.";

	if (key == null)
		return null;

	if (key.startsWith(prefix.toString())) {
            key = key.substring(prefix.length());
        } else {
	    return null;
        }
	final String found = props.getProperty(key);
        if ((found == null) || (found.equals(""))) {
	// No Java Runtime Environment property equivalent is found
	    return null;
	}			

		
	// Now check if the property is JFX specific and has no association with Runtime Environment
	if (found.equals("jfx_specific")) {
            props = jfxprop_list;
            return props.getProperty(key);
	} else {
            String res = java.security.AccessController.doPrivileged(
		                new java.security.PrivilegedAction<String>() {
                                    public String run() {
                                        return System.getProperty(found);
                                    }
	                        });
	    return res;
        }
    }

   /*
    * Removes the property from JavaFX System Properties list 
    * @param key JavaFX System Property name
    */
    public static void clearProperty (String key) {
	if (key == null)
		return;

        Properties props = sysprop_list;
	final String prefix = "javafx.";
        
	// Remove "javafx." prefix from the key
	if (key.startsWith(prefix.toString())) {
            key = key.substring(prefix.length());
        } else {
	    return;
        }

	String value = props.getProperty(key);
        if (value == null)
            return;

        props.remove(key);

	// Remove the prop from the JavaFX specific properties table if applicable
	if (value.equals("jfx_specific")) {
           props = jfxprop_list;		
	    props.remove(key);
	}
    }

    /**
     * Adds a new property or modifyies existing property value.
     * @param key JavaFX Property name
     * @param value Property value
     * @param jfx_specific Speicifies whether the property is JavaFX specific or Runtime Pleatform associated.
     * Runtime platform associated properties are set through System.setProperty() taken access security into an account.
     */
    public static void setProperty (String key, final String value, boolean jfx_specific) {

	Properties props = sysprop_list;
        final String prefix = "javafx.";

	if ((key == null) || (value == null))
		return;           

	// Remove "javafx." prefix from the key
	if (key.startsWith(prefix.toString())) {
            key = key.substring(prefix.length());
        } else {
	    return;
        }
	
	// Change existing property value
	if (jfx_specific) {
            props.setProperty(key, "jfx_specific");
            props = jfxprop_list;
            props.setProperty(key, value); 
	} else {
            final String rt_prop = props.getProperty(key);
            if ((rt_prop == null) || (rt_prop.equals("")))
                return;

            java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction<Void>() {
		    public Void run() {
                        System.setProperty(rt_prop, value);
                        return null;
                    }
                });
	}
    }
}
