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

package javafx.xml;

import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.FactoryConfigurationError; 
import javax.xml.parsers.ParserConfigurationException; 
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException; 
import org.xml.sax.InputSource;
import java.io.File;
import java.io.IOException; 
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import org.w3c.dom.DOMException; 
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 * Provides methods for creating a new document and for parsing xml from 
 * a data stream to create a new document.
 * @see javax.xml.parsers.DocumentBuilder
 * @see javax.xml.parsers.DocumentBuilderFactory
 * @author jclarke
 */

public class DocumentBuilder {
    private attribute factory:DocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    private attribute builder:javax.xml.parsers.DocumentBuilder = factory.newDocumentBuilder();
    
    /**
     * Specify the EntityResolver to be used to resolve entities
     * present in the XML document to be parsed. 
     * Setting this to null will result in the underlying implementation
     * using it's own default implementation and behavior. 
     * 
     * @see org.xml.sax.EntityResolver
     */ 
    public attribute entityResolver:EntityResolver on replace {
        builder.setEntityResolver(entityResolver);
    };
    
    /**
     * Specify the ErrorHandler to be used by the parser. 
     * Setting this to null will result in the underlying implementation
     * using it's own default implementation and behavior.
     * 
     * @see org.xml.sax.ErrorHandler
     */
    public attribute errorHandler:ErrorHandler on replace {
        builder.setErrorHandler(errorHandler);
    };    
    
    
    private function resetBuilder():Void {
        builder = factory.newDocumentBuilder();
        builder.setEntityResolver(entityResolver);
        builder.setErrorHandler(errorHandler);
    }
    
    /**
     * Indicates whether or not this parser is configured to
     * validate XML documents. Set to true to configure 
     * to validate XML documents; false otherwise. Default is false.
     */    
    public attribute validating:Boolean = factory.isValidating() on replace {
        factory.setValidating(validating);
        resetBuilder();
    };
    
    /**
     * Indicates whether or not this parser is configured to
     * understand namespaces. Set to true if this parser is to understand
     * namespaces; false otherwise. Default is <code>false</code>.
     */    
    public attribute namespaceAware:Boolean = factory.isNamespaceAware() on replace {
        factory.setNamespaceAware(namespaceAware);
        resetBuilder();
    };
    
    /**
     * Specifies that the parser produced by this code will
     * expand entity reference nodes. By default the value of this is set to
     * <code>true</code>
     */    
    public attribute expandEntityReferences:Boolean = factory.isExpandEntityReferences() on replace {
        factory.setExpandEntityReferences(expandEntityReferences);
        resetBuilder();
    };
    
    /**
     * <p>Specifies that the parser produced by this code will
     * ignore comments. By default the value of this is set to <code>false
     * </code>.</p>
     */
    public attribute ignoringComments = factory.isIgnoringComments() on replace {
        factory.setIgnoringComments(ignoringComments);
        resetBuilder();
    };
    
    /**
     * Specifies that the parser produced by this code will
     * convert CDATA nodes to Text nodes and append it to the
     * adjacent (if any) text node. By default the value of this is set to
     * <code>false</code>
     */
    public attribute coalescing:Boolean = factory.isCoalescing() on replace {
        factory.setCoalescing(coalescing);
        resetBuilder();
    };
    
    /**
     * Indicates whether or not the factory is configured to produce
     * parsers which ignore ignorable whitespace in element content.
     */
    public attribute ignoringElementContentWhitespace:Boolean = factory.isIgnoringElementContentWhitespace() on replace {
        factory.setIgnoringElementContentWhitespace(ignoringElementContentWhitespace);
        resetBuilder();
    };
    
    /**
     * Obtain a new instance of a DOM {@link Document} object
     * to build a DOM tree with.
     *
     * @return A new instance of a DOM Document object.
     * @see javax.xml.parsers.DocumentBuilder#newDocument()
     */
    public function createDocument():Document {
        var document = builder.newDocument();
        Document { document: document };
    }
    
   /**
     * Parse the content of the given <code>File</code> as an XML
     * document and return a new DOM {@link Document} object.
     * @param file the File containing the XML content
     * @return <code>Document</code> result of parsing 
     * @see javax.xml.parsers.DocumentBuilder#parse(java.io.File)
     */
    public function parseFile(file:File):Document {
        var document = builder.parse(file);
        Document { document: document };
    }
    
   /**
     * Parse the content of the given <code>InputSource</code> as an XML
     * document and return a new DOM {@link Document} object.
     * @param source the InputSource containing the XML content
     * @return <code>Document</code> result of parsing 
     * @see javax.xml.parsers.DocumentBuilder#parse(org.xml.sax.InputSource)
     */    
    public function parseInputSource(source:InputSource):Document {
        var document = builder.parse(source);
        Document { document: document };
    }
    
   /**
     * Parse the content of the given <code>InputStream</code> as an XML
     * document and return a new DOM {@link Document} object.
     * @param stream the InputStream containing the XML content
     * @return <code>Document</code> result of parsing 
     * @see javax.xml.parsers.DocumentBuilder#parse(java.io.InputStream)
     */    
    public function parseInputStream(stream:InputStream):Document {
        var document = builder.parse(stream);
        Document { document: document };
    }
    
   /**
     * Parse the content of the given <code>Reader</code> as an XML
     * document and return a new DOM {@link Document} object.
     * @param reader the Reader containing the XML content
     * @return <code>Document</code> result of parsing 
     * @see javax.xml.parsers.DocumentBuilder#parse(org.xml.sax.InputSource)
     */    
    public function parseReader(reader:Reader):Document {
        var document = builder.parse(new InputSource(reader));
        Document { document: document };
    }
    
   /**
     * Parse the content of the given <code>String</code> as an XML
     * document and return a new DOM {@link Document} object.
     * @param text the String containing the XML content
     * @return <code>Document</code> result of parsing 
     * @see javax.xml.parsers.DocumentBuilder#parse(org.xml.sax.InputSource)
     */    
    public function parseText(text:String):Document {
        var document = builder.parse(new InputSource(new StringReader(text)));
        Document { document: document };
    }
    
   /**
     * Parse the content of the given <code>URI</code> as an XML
     * document and return a new DOM {@link Document} object.
     * @param uri  The location of the content to be parsed.
     * @return <code>Document</code> result of parsing 
     * @see javax.xml.parsers.DocumentBuilder#parse(java.lang.String)
     */    
    public function parseURI(uri:String):Document {
        var document = builder.parse(uri);
        Document { document: document };
    }
    
}