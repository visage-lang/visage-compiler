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

package org.visage.runtime;

import java.util.Hashtable;
import java.io.InputStream;

public class  SystemProperties {
   /**
    * Visage System Properties table.
    * First column represents visage property name with "visage" prefix stripped off.
    * Second column represents underlying runtime platform equivalent. 
    * "visage_specific" value in the runtime platform equivalent field indicates the property is Visage specific.
    * Empty string in   the runtime platform equivalent field indicates thete is no equivalent property for given platform.
    */
    private static String[] sysprop_table = {
        /*"visage.*/"application.codebase", "visage_specific",
    };


    /**
     * Visage Specific System Properties table.
     * First column represents visage environment specific property name with "visage" prefix stripped off.
     * Second column represents value of the property 
    */
    private static String[] visageprop_table = {
        /*"visage.*/"application.codebase", "",
    };

    private static Hashtable sysprop_list = new Hashtable();  
    private static Hashtable visageprop_list = new Hashtable();

    private static final String versionResourceName =
            "/org/visage/runtime/resources/version.properties";
    

    static {
        addProperties (sysprop_table, false);
        addProperties (visageprop_table, true);
        setVersions();
    }

    /*
     * Populate our well known version strings
     */
    private static void setVersions() {
        int size;
        InputStream is =
                SystemProperties.class.getResourceAsStream(versionResourceName);
        try  {
            size = is.available();
        
            byte[] b = new byte[size];
            int n = is.read(b);            
            String inStr = new String(b, "utf-8");
            SystemProperties.setFXProperty("visage.version",
                    getValue(inStr, "release="));

            SystemProperties.setFXProperty("visage.runtime.version",
                    getValue(inStr, "full="));

        } catch (Exception ignore) {
        }
    }
    /*
     * Returns a value given a name
     */
    private static String getValue(String toSearch, String name) {
        String s = toSearch;
        int index;
        while ((index = s.indexOf(name)) != -1) {
            s = s.substring(index);
            if ((index = s.indexOf(0x0A))!= -1) {
                return (s.substring(name.length(), index)).trim();
            }
            return (s.substring(name.length(), s.length())).trim();
        }
        return "unknown";
    }
    /**
     * Registers a statically allocated System Properties table 
     * Once registered properties listed in the table are availabe for inquiry through Visage.getProperty().
     * Table is defined as a String array with Visage property name followed by property value or property mapping identifier
     * depending on whether the table contains Visage specific properties or not.
     * Note that Visage property names have "visage" stripped out to optimize table lookup.
     * The following identifiers are available:
     * </p>
     * 1. Underlying runtime platform property name. When listed, Visage.getProperty() will invoke System.getProperty()
     *    method to retrieve property value.
     *    example:
     *    {"version", "java.version"}
     * </p>   
     * 2. "visage_specific". When listed indicates there is no association between the property and underlying runtime
     *    platform. Rather the property is Visage specific. In that case another table needs to be provided with values
     *    for all Visage specific properties. Visage specific properties table is a string array containing property name
     *    and corresponding property value.
     *    example:
     *    {"hw.radio", "none"} 
     * </p>     
     * 3. Empty string. When listed, the meaning there is no association between the property and underlying runtime 
     *    platform nor the property is Visage specific. Visage.getProperty() invoked on that property returns null.
     *    example:
     *    {"supports.mixing", "none"} 
     * @param table System Properties table
     * @param visage_specific Indicates the table contains Visage specific properties
     */      
    public static void addProperties (String[] table, boolean visage_specific) {
        if (table == null)
            return;

        Hashtable props;
                            
        if (visage_specific) {
            props = visageprop_list;
        } else {
            props = sysprop_list;
        }
                                  
        for (int i=0; i<table.length; i+=2) {
            props.put(table[i], table[i+1]);
        }
    } 

    public static String getProperty (String key) {
        Hashtable props = sysprop_list;
        final String prefix = "visage.";

        if (key == null)
                return null;

        if (key.startsWith(prefix.toString())) {
            key = key.substring(prefix.length());
        } else {
            return null;
        }
        final String found = (String)props.get(key);
        if ((found == null) || (found.equals(""))) {
        // No Java Runtime Environment property equivalent is found
            return null;
        }                        

                
        // Now check if the property is Visage specific and has no association with Runtime Environment
        if (found.equals("visage_specific")) {
            props = visageprop_list;
            return (String)props.get(key);
        } else {
            return System.getProperty(found);
        }
    }

   /*
    * Removes the property from Visage System Properties list 
    * @param key Visage System Property name
    */
    public static void clearProperty (String key) {
        if (key == null)
                return;

        Hashtable props = sysprop_list;
        final String prefix = "visage.";
        
        // Remove "visage." prefix from the key
        if (key.startsWith(prefix.toString())) {
            key = key.substring(prefix.length());
        } else {
            return;
        }

        String value = (String)props.get(key);
        if (value == null)
            return;

        props.remove(key);

        // Remove the prop from the Visage specific properties table if applicable
        if (value.equals("visage_specific")) {
           props = visageprop_list;                
            props.remove(key);
        }
    }

    /**
     * Adds a new Visage specific property or modifyies existing property value.
     * Note that there is no method in this class to set underlying platform 
     * property as MIDP doesn't support System.setProperty() method.
     * @param key Visage Property name
     * @param value Property value
     * @throws NullPointerException if key or value is null
     */
    public static void setFXProperty (String key, final String value) {
        
        Hashtable props = sysprop_list;
        final String prefix = "visage."; 
        
        // Remove "visage." prefix from the key
        if (key.startsWith(prefix)) {
            key = key.substring(prefix.length());
       
           String k = (String)props.get(key);
	   // Add new property to the list
           if (k == null) {
               props.put(key, "visage_specific");
               props = visageprop_list;
               props.put(key, value);
	   } else if (k.equals("visage_specific")) {
               // Change existing property value
               props = visageprop_list;
               props.put(key, value);
               if (codebase.equals(prefix+key))
		   codebase_value = value;
	   }
	} 
    }

    public static String getCodebase() {
	return codebase_value;
    }

    public static void setCodebase(String value) {
	 if (value == null)
		value = "";
 	 codebase_value = value;
	 setFXProperty("visage.application.codebase", value);
    }

    private static String codebase_value;

    public static final String codebase = "visage.application.codebase";
}

