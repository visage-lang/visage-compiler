/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.tools.javafx.antlr;

import com.sun.tools.javafx.tree.JavafxTreeMaker;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import com.sun.tools.javac.util.*;

/**
 *
 * @author Robert Field
 */
public class JavafxSyntacticAnalysis {

    /** The context key for the parser. */
    protected static final Context.Key<JavafxSyntacticAnalysis> syntaxKey =
            new Context.Key<JavafxSyntacticAnalysis>();
    
    protected Context context;
    /** Command line options
     */
    protected Options options;
    /** The name table.
     */
    protected Name.Table names;
    
    /** The log to be used for error reporting.
     */
    public Log log;

    public static JavafxSyntacticAnalysis instance(Context context) {
        JavafxSyntacticAnalysis instance = context.get(syntaxKey);
        if (instance == null)
            instance = new JavafxSyntacticAnalysis(context);
        return instance;
    }
    
    protected JavafxSyntacticAnalysis(final Context context) {
        this.context = context;
        context.put(syntaxKey, this);

        names = Name.Table.instance(context);
        options = Options.instance(context);
        log = Log.instance(context);
    }

    public JCCompilationUnit parse(CharSequence content) {
        JCCompilationUnit unit = null;
        String parserChoice = options.get("parser");
        if (parserChoice == null) {
            parserChoice = "vn"; // default
        }
        if (parserChoice.equals("vn")) {
            // leave this default until the new stuff works
            v2Parser generatedParser;
            generatedParser = new v2Parser(context, content);
            try {
                unit = generatedParser.module();
            } catch (Throwable thr) {
                System.err.println("Got v2 Error:");
                thr.printStackTrace(System.err);
            }
        } else {
            try {
                // Create input stream from standard input
                ANTLRStringStream input = new ANTLRStringStream(content.toString());
                // Create a lexer attached to that input stream
                v3Lexer lexer = new v3Lexer(context, input);
                // Create a stream of tokens pulled from the lexer
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                // Create a parser attached to the token stream
                v3Parser parser = new v3Parser(tokens);
                // Set the context
                parser.initialize(context);
                // Invoke the module rule in get return value
                v3Parser.module_return comReturn = parser.module();
                CommonTree comTree = (CommonTree) comReturn.getTree();
                // Walk resulting tree; create treenode stream first
                CommonTreeNodeStream nodes = new CommonTreeNodeStream(comTree);
                // AST nodes have payloads that point into token stream
                nodes.setTokenStream(tokens);
                // Create a tree Walker attached to the nodes stream
                v3Walker walker = new v3Walker(nodes);
                // Invoke the start symbol, rule module
                unit = walker.module();
                printTree(comTree, "---");
            } catch (Throwable thr) {
                System.err.println("Got Error:");
                thr.printStackTrace(System.err);
            }
        }
        return unit;
    }

    private void printTree(Tree tree, String prefix) {
        System.out.println(prefix + tree);
        int n = tree.getChildCount();
        String nextPrefix = prefix + "   ";
        for (int i = 0; i < n; ++i) {
            printTree(tree.getChild(i), nextPrefix);
        }
    }
}

