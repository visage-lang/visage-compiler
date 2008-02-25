package javafxpad;

import net.java.javafx.ui.UIContext;
import net.java.javafx.type.expr.ValidationError;
import net.java.javafx.type.expr.VariableDeclarator;
import net.java.javafx.typeImpl.Compilation;
import net.java.javafx.typeImpl.completion.CompletionProcessor;
import net.java.javafx.typeImpl.completion.CompletionParserTokenManager;
import net.java.javafx.typeImpl.completion.CompletionParserConstants;
import net.java.javafx.typeImpl.completion.SimpleCharStream;
import net.java.javafx.typeImpl.completion.Token;
import net.java.javafx.typeImpl.SourceCodeWriter;
import java.io.*;
import java.util.*;

public class JFXCodeFormatter {

    int lineOff;

    public int getLineOffset() {
	return lineOff;
    }

    public String formatCode(String code, int line, int lineOffset, int colOffset, int caretDot) {
	long start = System.currentTimeMillis();
	String textNoCRs = code.replaceAll("\r", "");
	Reader reader = new StringReader(textNoCRs);
	Writer sw = new StringWriter();
	PrintWriter p = new PrintWriter(sw);
	SimpleCharStream charStream = new SimpleCharStream(reader, 1, 0, 4096);
	CompletionParserTokenManager tokenizer = new CompletionParserTokenManager(charStream);
	ArrayList tokens = new ArrayList();
	Token caretToken = null;
	int caretOff = 0;
	int newCaretPos = caretDot;
	Token lastTok = (Token)null;
	while (true) {
	    Token tok = tokenizer.getNextToken();
	    tokens.add(tok);
	    if (caretToken == null && tok.beginLine == line) {
		if (tok.beginColumn >= colOffset) {
		    if (tok.beginColumn == colOffset || lastTok.beginLine < line) {
			caretToken = tok;
			caretOff = tok.beginColumn - colOffset;
		    } else {
			caretToken = lastTok;
			caretOff = tok.beginColumn - colOffset;
		    }
		}
	    }
	    if (tok.kind == 0) {
		break;
	    }
	    lastTok = tok;
	}
	String indent = "    ";
	int level = 0;
	boolean skip = false;
	Stack stack = new Stack();
	int lineNumVal = 1;
	for (int i = 0, size = tokens.size()-1; i < size; i++) {
	    Token tok = (Token)tokens.get(i);
	    Token special = tok.specialToken;
	    String extra = "";
	    stack.clear();
	    boolean skipDec = false;
	    int newLineCount = 0;
	    while (special != null) {
		if (!"\r".equals(special.image)) {
		    if ("\n".equals(special.image)) {
			newLineCount++;
		    }
		    stack.push(special);
		}
		special = special.specialToken;
	    }
	    while (stack.size() > 0) {
		special = (Token)stack.pop();
		String specialImage = special.image;
		if (special.kind == CompletionParserConstants.FORMAL_COMMENT || 
		    special.kind == CompletionParserConstants.MULTI_LINE_COMMENT ||
		    special.kind == CompletionParserConstants.SINGLE_LINE_COMMENT) {
		    // ok
		    p.print(specialImage);
		    if (special.kind == CompletionParserConstants.SINGLE_LINE_COMMENT) {
			for (int j = 0; j < level; j++) {
			    p.print(indent);
			}
			skip = true;
                    }
		} else if ("\n".equals(specialImage)) {
		    lineNumVal++;
		    p.print("\n");
		    if ("}".equals(tok.image)) {
			Token next = special.specialToken;
			if (next != null && "\r".equals(next.image)) {
			    next = next.specialToken;
			}
			if (--newLineCount == 0) {
			    skipDec = true;
			    level--;
			} else {
			    //println("next after newline = '{next.image}'");
			}
		    }        
		    for (int j = 0;  j < level; j++) {
			p.print(indent);
		    }
		    skip = true;
		} else if ("\t".equals(specialImage)) {
		    if (!skip) {
			p.print(indent);
		    }
		} else if ("\r".equals(special.image)) {
		    // nothing
		} else {
		    if (!skip) {
			p.print(specialImage);
		    }
		}
		special = special.specialToken;
	    }
	    p.print(tok.image);
	    if ("{".equals(tok.image)) {
		level++;
	    } else if ("}".equals(tok.image) && !skipDec) {
		level--;
	    } else if ("[".equals(tok.image)) {
	    } else if ("]".equals(tok.image)) {
	    }
	    if (false && caretToken == tok) {
		newCaretPos = sw.toString().length() -tok.image.length() + caretOff;
	    }
	    skip = false;
	}
	//p.print("\n");
	p.flush();
	String newText = sw.toString();
	int lineCount = 0;
	int lineOff = 0;
	char newline = '\n';
	for (int i = 0, len = newText.length()-1; i < len; i++) {
	    if (lineCount == line-1) {
		lineOff = i;
		break;
	    }
	    if (newText.charAt(i) == newline) {
		lineCount++;
	    }
	}
	//var lineOff = editor.getLineStartOffset(line-1);
	char space = ' ';
	char c = newText.charAt(lineOff);
	int newColOff = 0;
	while (c == space) {
	    newColOff++;
	    c = newText.charAt(lineOff+newColOff);
	    if (c != space) {
		break;
	    }
	}
	lineOff +=  newColOff > colOffset-1 ? newColOff : colOffset-1;
	this.lineOff = lineOff;
	long end = System.currentTimeMillis();
	double d1 = (double)start;
	double d2 = (double)end;
	double elapsed = (d2 - d1)/1000000;
	System.out.println("format elapsed: "+ elapsed);
	return newText;
    }
}
