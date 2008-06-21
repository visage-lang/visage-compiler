/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
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

package javafx.xml;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.lang.IllegalArgumentException;

/**
 * A simple implementation of a NamespaceContext
 * 
 * @author jclarke
 */
public class FXNamespaceContext extends  NamespaceContext {
    /**
     * map holding prefix to URI mappings
     */
    private attribute prefixMap:Map = new HashMap();
    /**
     * A map containing URI to prefixes mappings
     */
    private attribute uriMap:Map = new HashMap();
    
    /**
     * for a given prefix get its assocationed URI
     * @param prefix the prefix
     * @return the URI
     * @see javax.xml.namespace.NamespaceContext#getNamespaceURI
     */
    public function getNamespaceURI(prefix:String):String {
        if(prefix == null) {
            throw new IllegalArgumentException("prefix cannot be null");
        }
        var uri = prefixMap.get(prefix) as String;
        return if(uri != null) then uri else XMLConstants.NULL_NS_URI;
    }
    /**
     * for a given uri get its associated prefix
     * @param uri the uri
     * @return the prefix
     * @see javax.xml.namespace.NamespaceContext#getPrefix(String)
     */
    public function getPrefix( uri:String ):String  {
        if(uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        var prefixes = uriMap.get(uri) as Set;
        return if(prefixes != null) then prefixes.iterator().next() as String else null;        
    }
    /**
     * for a given uri get its associated prefixes
     * @param uri the uri
     * @return an iterator for the prefixes
     * @see javax.xml.namespace.NamespaceContext#getPrefixes(String)
     */
    public function getPrefixes(uri:String):Iterator {
        if(uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        var prefixes = uriMap.get(uri) as Set;
        return if(prefixes != null) then prefixes.iterator() 
            else Collections.EMPTY_SET.iterator();   
    }
    
    /**
     * add a namespace mapping
     * @param prefix the prefix
     * @param uri the uri
     */
    public function addNamespace(prefix:String, uri:String) {
        prefixMap.put(prefix, uri);
        var uris = uriMap.get(uri) as Set;
        if(uris == null) {
            uris = new HashSet();
            uriMap.put(uri, uris);
        }
        uris.add(prefix);
    }
    
    /**
     * initialize the map with standard mappings
     */
    init {
        addNamespace(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        addNamespace(XMLConstants.XMLNS_ATTRIBUTE,
                XMLConstants.XMLNS_ATTRIBUTE_NS_URI);        
    }
}
