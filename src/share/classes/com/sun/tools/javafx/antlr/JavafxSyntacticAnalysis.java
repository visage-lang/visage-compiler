/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.tools.javafx.antlr;

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

    public JCCompilationUnit parse(CharSequence content, String fileName) {
        JCCompilationUnit unit = null;
        String parserChoice = options.get("parser");
        if (parserChoice == null) {
            parserChoice = "v3"; // default
        }
        {
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
                if (errorCount() == 0) {
                    // Walk resulting tree; create treenode stream first
                    CommonTreeNodeStream nodes = new CommonTreeNodeStream(comTree);
                    // AST nodes have payloads that point into token stream
                    nodes.setTokenStream(tokens);
                    // Create a tree Walker attached to the nodes stream
                    v3Walker walker = new v3Walker(nodes);
                    // Set the context
                    walker.initialize(context);
                    // Invoke the start symbol, rule module
                    unit = walker.module();                   
                }
                String treeChoice = options.get("tree");
                if (treeChoice != null) {
                    printTree(comTree, "---");
                }            
            } catch (Throwable thr) {
                System.err.println("Error in syntactic analysis in " + fileName + ":");
                thr.printStackTrace(System.err);
            }
        }
        return unit;
    }

    /** The number of errors reported so far.
     */
    public int errorCount() {
        return log.nerrors;
    }

    private void printTree(Tree tree, String prefix) {
        CommonToken token = (CommonToken)((CommonTree)tree).getToken();
        System.out.println(prefix + tree + "  (" + token.getStartIndex() + ")" + token.getLine());
        int n = tree.getChildCount();
        String nextPrefix = prefix + "   ";
        for (int i = 0; i < n; ++i) {
            printTree(tree.getChild(i), nextPrefix);
        }
    }
}

