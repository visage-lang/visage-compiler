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

package com.sun.javafx.api.ui.fxkit;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import java.awt.Shape;

import com.sun.tools.javafx.antlr.v3Lexer;
import com.sun.tools.javac.util.Context;
import java.awt.Font;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.Token;

public class FXEditorKit extends DefaultEditorKit {

    public static class FXScanner {

        private static Color KEYWORD_COLOR = 
            new Color(127, 0, 85);
        private static Color STRING_COLOR = 
            new Color(42, 0, 255);
        private static Color COMMENT_COLOR = 
            new Color(63, 127, 95);
        private static Color TEXT_COLOR = 
            Color.black;

        static Set keywords = new HashSet();
        final static String[] keywordArray = new String[]{
            "abstract",
            "after",
            "and",
            "as",
            "assert",
            "at",
            "attribute",
            "before",
            "bind",
            "bound",
            "break",
            "catch",
            "class",
            "continue",
            "delete",
            "else",
            "exclusive",
            "extends",
            "false",
            "finally",
            "first",
            "for",
            "from",
            "function",
            "if",
            "import",
            "in",
            "indexof",
            "init",
            "insert",
            "instanceof",
            "into",
            "inverse",
            "last",
            "lazy",
            "let",
            "new",
            "not",
            "null",
            "on",
            "or",
            "override",
            "package",
            "postinit",
            "private",
            "protected",
            "public",
            "readonly",
            "replace",
            "return",
            "reverse",
            "sizeof",
            "static",
            "step",
            "super",
            "then",
            "this",
            "throw",
            "true",
            "try",
            "tween",
            "typeof",
            "var",
            "where",
            "while",
            "with"
        };
        static {
            for (int i = 0; i < keywordArray.length; i++) {
                keywords.add(keywordArray[i]);
            }
        }
        v3Lexer lexer;
        int[]  lineMap;
        int offset;
        int length;
        
        
        class TokenImpl {
            Token token; // antlr token
            Color foreground = Color.BLACK;
            Color background;
            int fontStyle = -1;
            int off;
            
            TokenImpl(Token token) {
                this.token = token;
                switch(token.getType()) {
                    case v3Lexer.WS:
                        break;
                    case v3Lexer.COMMENT:
                    case v3Lexer.LINE_COMMENT:
                    case v3Lexer.DOC_COMMENT:
                        foreground = COMMENT_COLOR;
                        fontStyle = Font.ITALIC;
                        break;
                    case v3Lexer.STRING_LITERAL:
                    case v3Lexer.EMPTY_FORMAT_STRING:
                    case v3Lexer.FORMAT_STRING_LITERAL:
                    case v3Lexer.RBRACE_QUOTE_STRING_LITERAL:
                    case v3Lexer.QUOTE_LBRACE_STRING_LITERAL:
                    case v3Lexer.RBRACE_LBRACE_STRING_LITERAL:
                        foreground = STRING_COLOR;
                        break;
                    default:
                        if(keywords.contains(token.getText())) {
                            foreground =KEYWORD_COLOR;
                            fontStyle = Font.BOLD;
                        }
                }
                if(token.getLine()  > 0)
                    off = offset + lineMap[token.getLine()-1] + token.getCharPositionInLine();
                else 
                    off = offset + length;
            }

            public Token getToken() {
                return token;
            }

            public // antlr token
            Color getForeground() {
                return foreground;
            }

            public Color getBackground() {
                return background;
            }

            public int getFontStyle() {
                return fontStyle;
            }
            public int getLineNumber() {
                return token.getLine();
            }
            public int getColumnNumber() {
                return token.getCharPositionInLine();
            }
            public int getTokenLength() {
                String text = token.getText();
                if(text != null) {
                    switch (token.getType()) {
                        case v3Lexer.STRING_LITERAL:
                            return text.length() + 2;
                        case v3Lexer.EMPTY_FORMAT_STRING:
                            return 2;
                        case v3Lexer.FORMAT_STRING_LITERAL:
                            return text.length() + 2;
                        case v3Lexer.RBRACE_QUOTE_STRING_LITERAL:
                            return text.length() + 1;
                        case v3Lexer.QUOTE_LBRACE_STRING_LITERAL:
                            return text.length() + 1;
                        case v3Lexer.RBRACE_LBRACE_STRING_LITERAL:
                            return text.length() + 2;
                        default:
                            return text.length();
                    }
                }else {
                    return 0;
                }
            }
            public int getOffset() {
                return off;
            }
            public boolean isEOF() {
                return token.getType() == v3Lexer.EOF;
            }
        }
        
        void tokenize(int offset, int length, String content) {
            Context context = new Context();
            ANTLRStringStream input = new ANTLRStringStream(content.toString());
            lexer = new v3Lexer(context, input);
            ArrayList offsets = new ArrayList();
            offsets.add(new Integer(0));
            char[] chs = content.toCharArray();
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] == '\n') {
                    offsets.add(new Integer(i+1));
                }
            }
            lineMap = new int[offsets.size()];
            for (int i = 0; i < lineMap.length; i++) {
                lineMap[i] = ((Integer)offsets.get(i)).intValue();
            }
            this.offset = offset;
            this.length = length;

        }
        TokenImpl nextToken() {
            try {
            Token t = lexer.nextToken();
            if(t.getType() != v3Lexer.EOF)
                return new TokenImpl(t);
            }catch(Throwable th) {
                
            }
            return null;
        }
        
        
        public void setRange(Document document, int offset, int length) {
            try {
                String range = document.getText(offset, length);
                tokenize(offset, length, range);
            } catch (BadLocationException ex) {
                Logger.getLogger(FXEditorKit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        

    }


    FXScanner mLexer = new FXScanner();

    DocumentListener mDocumentListener = null;

    class FXView extends /*Wrapped*/PlainView {

	FXView(Element elem) {
	    super(elem);
	}

        @Override
        public void paint(Graphics g, Shape a) {
	    super.paint(g, a);
	}

        @Override
        protected int drawUnselectedText(Graphics g, int x, int y, 
					 int p0, int p1) 
            throws BadLocationException {
            //TODO
            Document doc = getDocument();

            
            mLexer.setRange(doc, p0, p1-p0);
            Color last = null;
            int lastFontStyle = -1;
            int startPos = p0;
            int mark = p0;
            FXScanner.TokenImpl tok;
            Font saveFont = g.getFont();
            Color saveColor = g.getColor();
            int defaultFontStyle = saveFont.getStyle();
            boolean first = true;
            while ((tok = mLexer.nextToken()) != null) {
                first = false;
                if (tok.getOffset() + tok.getTokenLength() < startPos) {
                    continue;
                }
                if (tok.getOffset() > p1) {
                    break;
                }
                
                int p = Math.min(tok.getOffset() + tok.getTokenLength(), p1);
                p = (p <= p0) ? p1 : p;
                Color fg = tok.getForeground();
                int fontStyle = tok.getFontStyle();
                if (fontStyle < 0) {
                    fontStyle = defaultFontStyle;
                }
                if ((fg != last || fontStyle != lastFontStyle) && last != null) {
                    // color change, flush what we have
                    g.setColor(last);
                    if (lastFontStyle != defaultFontStyle) {
                        g.setFont(saveFont.deriveFont(lastFontStyle));
                    } else {
                        g.setFont(saveFont);
                    }
                    Segment text = getLineBuffer();
                    //System.out.println("drawing text: " + doc.getText(mark, p0- mark));
                    doc.getText(mark, p0 - mark, text);
                    x = Utilities.drawTabbedText(text, x, y, g, this, mark);
                    mark = p0;
                }
                last = fg;
                lastFontStyle = fontStyle;
                p0 = p;

            }
            if (first) {
                // must have been a lexical error
                last = Color.black; // hack
                lastFontStyle = defaultFontStyle;
            }
            // flush remaining
            g.setColor(last);
            if (lastFontStyle != defaultFontStyle) {
                g.setFont(saveFont.deriveFont(lastFontStyle));
            } else {
                g.setFont(saveFont);
            }
            Segment text = getLineBuffer();
            //System.out.println("drawing text: " + doc.getText(mark, p1- mark));
            doc.getText(mark, p1 - mark, text);
            x = Utilities.drawTabbedText(text, x, y, g, this, mark);
            g.setFont(saveFont);
            g.setColor(saveColor);
            return x;
	}
    }


    @Override
    public String getContentType() {
	return "text/fx";
    }

    @Override
    public final ViewFactory getViewFactory() {
        return new ViewFactory() {
                public View create(Element elem) {
                    return new FXView(elem);
                }
            };
    }
}
