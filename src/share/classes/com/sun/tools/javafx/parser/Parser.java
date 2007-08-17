/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.parser;

import java.util.HashMap;
import java.util.Map;
import java.io.OutputStreamWriter;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javafx.tree.*;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import static com.sun.tools.javac.util.ListBuffer.lb;
import static com.sun.tools.javafx.parser.Token.*;
import com.sun.tools.javafx.code.JavafxBindStatus;
import static com.sun.tools.javafx.code.JavafxBindStatus.*;


/** The parser maps a token sequence into an abstract syntax
 *  tree. It operates by recursive descent, with code derived
 *  systematically from an LL(1) grammar. For efficiency reasons, an
 *  operator precedence scheme is used for parsing binary operation
 *  expressions.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Version("@(#)Parser.java	1.107 07/05/05")
public class Parser {
    
    /** A factory for creating parsers. */
    public static class Factory {
        /** The context key for the parser factory. */
        protected static final Context.Key<Parser.Factory> parserFactoryKey =
                new Context.Key<Parser.Factory>();
        
        /** Get the Factory instance for this context. */
        public static Factory instance(Context context) {
            Factory instance = context.get(parserFactoryKey);
            if (instance == null)
                instance = new Factory(context);
            return instance;
        }
        
        final JavafxTreeMaker F;
        final Log log;
        final Keywords keywords;
        final Source source;
        final Name.Table names;
        final Options options;
        
        /** Create a new parser factory. */
        protected Factory(Context context) {
            context.put(parserFactoryKey, this);
            this.F = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
            this.log = Log.instance(context);
            this.names = Name.Table.instance(context);
            this.keywords = Keywords.instance(context);
            this.source = Source.instance(context);
            this.options = Options.instance(context);
        }
        
        /**
         * Create a new Parser.
         * @param S Lexer for getting tokens while parsing
         * @param keepDocComments true if javadoc comments should be kept
         * @param genEndPos true if end positions should be generated
         */
        public Parser newParser(Lexer S, boolean keepDocComments, boolean genEndPos) {
            if (!genEndPos)
                return new Parser(this, S, keepDocComments);
            else
                return new EndPosParser(this, S, keepDocComments);
        }
    }
    
    /** The number of precedence levels of infix operators.
     */
    private static final int infixPrecedenceLevels = 10;
    
    /** The scanner used for lexical analysis.
     */
    private Lexer S;
    
    /** The factory to be used for abstract syntax tree construction.
     */
    protected JavafxTreeMaker F;
    
    /** The log to be used for error diagnostics.
     */
    private Log log;
    
    /** The keyword table. */
    private Keywords keywords;
    
    /** The Source language setting. */
    private Source source;
    
    /** The name table. */
    private Name.Table names;
    
    /** Construct a parser from a given scanner, tree factory and log.
     */
    protected Parser(Factory fac,
            Lexer S,
            boolean keepDocComments) {
        this.S = S;
        S.nextToken(); // prime the pump
        this.F = fac.F;
        this.log = fac.log;
        this.names = fac.names;
        this.keywords = fac.keywords;
        this.source = fac.source;
        Options options = fac.options;
        this.allowGenerics = source.allowGenerics();
        this.allowVarargs = source.allowVarargs();
        this.allowAsserts = source.allowAsserts();
        this.allowEnums = source.allowEnums();
        this.allowForeach = source.allowForeach();
        this.allowStaticImport = source.allowStaticImport();
        this.allowAnnotations = source.allowAnnotations();
        this.keepDocComments = keepDocComments;
        if (keepDocComments) docComments = new HashMap<JCTree,String>();
        this.errorTree = F.Erroneous();
    }
    
    /** Switch: Should generics be recognized?
     */
    boolean allowGenerics;
    
    /** Switch: Should varargs be recognized?
     */
    boolean allowVarargs;
    
    /** Switch: should we recognize assert statements, or just give a warning?
     */
    boolean allowAsserts;
    
    /** Switch: should we recognize enums, or just give a warning?
     */
    boolean allowEnums;
    
    /** Switch: should we recognize foreach?
     */
    boolean allowForeach;
    
    /** Switch: should we recognize foreach?
     */
    boolean allowStaticImport;
    
    /** Switch: should we recognize annotations?
     */
    boolean allowAnnotations;
    
    /** Switch: should we keep docComments?
     */
    boolean keepDocComments;
    
    /** When terms are parsed, the mode determines which is expected:
     *     mode = EXPR        : an expression
     *     mode = TYPE        : a type
     *     mode = NOPARAMS    : no parameters allowed for type
     *     mode = TYPEARG     : type argument
     */
    static final int EXPR = 1;
    static final int TYPE = 2;
    static final int NOPARAMS = 4;
    static final int TYPEARG = 8;
    
    /** The current mode.
     */
    private int mode = 0;
    
    /** The mode of the term that was parsed last.
     */
    private int lastmode = 0;
    
    /* ---------- error recovery -------------- */
    
    private JCErroneous errorTree;
    
    /** Skip forward until a suitable stop token is found.
     */
    private void skip(boolean stopAtImport, boolean stopAtMemberDecl, boolean stopAtIdentifier, boolean stopAtStatement) {
        while (true) {
            switch (S.token()) {
            case SEMI:
                S.nextToken();
                return;
            case PUBLIC:
            case EOF:
            case CLASS:
                return;
            case IMPORT:
                if (stopAtImport)
                    return;
                break;
            case LBRACE:
            case RBRACE:
            case PRIVATE:
            case PROTECTED:
                if (stopAtMemberDecl)
                    return;
                break;
            case IDENTIFIER:
                if (stopAtIdentifier)
                    return;
                break;
            case IF:
            case FOR:
            case WHILE:
            case DO:
            case TRY:
            case RETURN:
            case THROW:
            case BREAK:
            case CONTINUE:
            case ELSE:
            case FINALLY:
            case CATCH:
                if (stopAtStatement)
                    return;
                break;
            }
            S.nextToken();
        }
    }
    
    private JCErroneous syntaxError(int pos, String key, Object... arg) {
        return syntaxError(pos, null, key, arg);
    }
    
    private JCErroneous syntaxError(int pos, List<JCTree> errs, String key, Object... arg) {
        setErrorEndPos(pos);
        reportSyntaxError(pos, key, arg);
        return toP(F.at(pos).Erroneous(errs));
    }
    
    private int errorPos = Position.NOPOS;
    /**
     * Report a syntax error at given position using the given
     * argument unless one was already reported at the same position.
     */
    private void reportSyntaxError(int pos, String key, Object... arg) {
        if (pos > S.errPos() || pos == Position.NOPOS) {
            if (S.token() == EOF)
                log.error(pos, "premature.eof");
            else
                log.error(pos, key, arg);
        }
        S.errPos(pos);
        if (S.pos() == errorPos)
            S.nextToken(); // guarantee progress
        errorPos = S.pos();
    }
    
    
    /** Generate a syntax error at current position unless one was already
     *  reported at the same position.
     */
    private JCErroneous syntaxError(String key) {
        return syntaxError(S.pos(), key);
    }
    
    /** Generate a syntax error at current position unless one was
     *  already reported at the same position.
     */
    private JCErroneous syntaxError(String key, String arg) {
        return syntaxError(S.pos(), key, arg);
    }
    
    /** If next input token matches given token, skip it, otherwise report
     *  an error.
     */
    public void accept(Token token) {
        if (S.token() == token) {
            S.nextToken();
        } else {
            setErrorEndPos(S.pos());
            reportSyntaxError(S.prevEndPos(), "expected", keywords.token2string(token));
        }
    }
    
    /** Report an illegal start of expression/type error at given position.
     */
    JCExpression illegal(int pos) {
        setErrorEndPos(S.pos());
        if ((mode & EXPR) != 0)
            return syntaxError(pos, "illegal.start.of.expr");
        else
            return syntaxError(pos, "illegal.start.of.type");
        
    }
    
    /** Report an illegal start of expression/type error at current position.
     */
    JCExpression illegal() {
        return illegal(S.pos());
    }
    
    /** Diagnose a modifier flag from the set, if any. */
    void checkNoMods(long mods) {
        if (mods != 0) {
            long lowestMod = mods & -mods;
            log.error(S.pos(), "mod.not.allowed.here",
                    Flags.toString(lowestMod).trim());
        }
    }
    
    /* ---------- doc comments --------- */
    
    /** A hashtable to store all documentation comments
     *  indexed by the tree nodes they refer to.
     *  defined only if option flag keepDocComment is set.
     */
    Map<JCTree, String> docComments;
    
    /** Make an entry into docComments hashtable,
     *  provided flag keepDocComments is set and given doc comment is non-null.
     *  @param tree   The tree to be used as index in the hashtable
     *  @param dc     The doc comment to associate with the tree, or null.
     */
    void attach(JCTree tree, String dc) {
        if (keepDocComments && dc != null) {
            //          System.out.println("doc comment = ");System.out.println(dc);//DEBUG
            docComments.put(tree, dc);
        }
    }
    
    /* -------- source positions ------- */
    
    private int errorEndPos = -1;
    
    private void setErrorEndPos(int errPos) {
        if (errPos > errorEndPos)
            errorEndPos = errPos;
    }
    
    protected int getErrorEndPos() {
        return errorEndPos;
    }
    
    /**
     * Store ending position for a tree.
     * @param tree   The tree.
     * @param endpos The ending position to associate with the tree.
     */
    protected void storeEnd(JCTree tree, int endpos) {}
    
    /**
     * Store ending position for a tree.  The ending position should
     * be the ending position of the current token.
     * @param t The tree.
     */
    protected <T extends JCTree> T to(T t) { return t; }
    
    JCStatement toStatement(int pos, JCExpression t)
    {
        JFXBlockExpression bl;
        if (t instanceof JFXBlockExpression
                && (bl = (JFXBlockExpression) t).value == null) {
            return F.at(pos).Block(bl.flags, bl.stats);
        }
        else
            return F.at(pos).Exec(checkExprStat(t));
    }
 
    /**
     * Store ending position for a tree.  The ending position should
     * be greater of the ending position of the previous token and errorEndPos.
     * @param t The tree.
     */
    protected <T extends JCTree> T toP(T t) { return t; }
    
    /** Get the start position for a tree node.  The start position is
     * defined to be the position of the first character of the first
     * token of the node's source text.
     * @param tree  The tree node
     */
    public int getStartPos(JCTree tree) {
        return JavafxTreeInfo.getStartPos(tree);
    }
    
    /**
     * Get the end position for a tree node.  The end position is
     * defined to be the position of the last character of the last
     * token of the node's source text.  Returns Position.NOPOS if end
     * positions are not generated or the position is otherwise not
     * found.
     * @param tree  The tree node
     */
    public int getEndPos(JCTree tree) {
        return Position.NOPOS;
    }
    
    
    
    /* ---------- parsing -------------- */
    
    ///// BEGIN ////
    
    /**
     * 	module		:=	 packageDecl?  moduleItem* <EOF>
     *  packageDecl	:=	'package' Identifier ( '.' Identifier )* ';'
     */
    public JCTree.JCCompilationUnit compilationUnit() {
        int pos = S.pos();
        JCExpression pid = null;
        String dc = S.docComment();
        if (S.token() == PACKAGE) {
            S.nextToken();
            pid = qualident();
            accept(SEMI);
        }
        List<JCTree> topItems = moduleItems();
        JCTree.JCCompilationUnit toplevel = F.at(pos).TopLevel(List.<JCAnnotation>nil(), pid, topItems);
        attach(toplevel, dc);
        if (topItems.isEmpty())
            storeEnd(toplevel, S.prevEndPos());
        if (keepDocComments) toplevel.docComments = docComments;
        /*
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        JavafxPretty pretty = new JavafxPretty(osw, false);
        try {
            pretty.printExpr(toplevel);
            osw.flush();
        }catch(Exception ex) {
            System.err.println("Pretty print got: " + ex);
        }
         * */
        return toplevel;
    }
    
    
    /**
     * 	moduleItem	:=
     *	                 |	import
     *	                 |	forwardDecl
     *	                 |	classDefinition
     *	                 |	attributeDefinition  //TODO
     *	                 |	classOperationDefinition
     *	                 |	classFunctionDefinition //TODO
     *	                 |	changeRule //TODO
     *	                 |	formatDefinition //TODO
     *	                 |	statement
     */
    @SuppressWarnings("fallthrough")
    List<JCTree> moduleItems() {
        int lastErrPos = -1;
        ListBuffer<JCTree> moduleItems = new ListBuffer<JCTree>();
        
        while (true) {
            int pos = S.pos();
            switch (S.token()) {
            case EOF:
                return moduleItems.toList();
            case IMPORT:
                moduleItems.append(importDeclaration());
                break;
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
                moduleItems.append(classDeclaration(S.docComment()));
                break;
            case ATTRIBUTE: {
                JCTree tree = attributeDefinition(pos, S.docComment());
                moduleItems.append(tree);
                break;
            }
            case FUNCTION: {
                JCTree tree = functionDefinition(pos, false, S.docComment());
                moduleItems.append(tree);
                break;
            }
            case OPERATION: {
                JCTree tree = operationDefinition(pos, false, S.docComment());
                moduleItems.append(tree);
                break;
            }
            case TRIGGER: {
                JCTree tree = triggerOn(false, S.docComment());
                moduleItems.append(tree);
                break;
            }
            default:
                moduleItems.append(statement());
                break;
            }
            
            // error recovery
            if (S.pos() == lastErrPos)
                return moduleItems.toList();
            if (S.pos() <= errorEndPos) {
                skip(false, true, true, true);
                lastErrPos = S.pos();
            }
        }
    }
    
    /** ImportDeclaration = IMPORT Ident { "." Ident } [ "." "*" ] ";"
     *
     * TODO: JavaFX has a complex import structure --
     *   import := 'import' typeAlias ( ',' typeAlias )* [ 'from' Identifier ( '.' Identifier )* ] ';'
     *   typeAlias := Identifier ( '.' ( Identifier | '*' ) )* ['as' Identifier]
     * only the most basic case is parsed below
     */
    JCTree importDeclaration() {
        int pos = S.pos();
        S.nextToken();
        JCExpression pid = toP(F.at(S.pos()).Identifier(ident()));
        do {
            int pos1 = S.pos();
            accept(DOT);
            if (S.token() == STAR) {
                pid = to(F.at(pos1).Select(pid, names.asterisk));
                S.nextToken();
                break;
            } else {
                pid = toP(F.at(pos1).Select(pid, ident()));
            }
        } while (S.token() == DOT);
        accept(SEMI);
        boolean importStatic = false;
        return toP(F.at(pos).Import(pid, importStatic));
    }
    
    /**
     * 	classDefinition  := accessModifier? 'class' Identifier
     *      [ 'supertype' Identifier ( ',' Identifier )* ]
     *      '{' ( attributeDecl | functiontionDecl | operationDecl )* '}'
     *  @param dc       The documentation comment for the class, or null.
     */
    JFXClassDeclaration classDeclaration(String dc) {
        int pos = S.pos();
        JCModifiers mods = modifiersOpt();
        accept(CLASS);
        Name name = ident();
        
        ListBuffer<JCExpression> superclasses = new ListBuffer<JCExpression>();
        if (S.token() == SUPERTYPE) {
            S.nextToken();
            superclasses.append(toP(F.at(S.pos()).Ident(ident())));
            while (S.token() == COMMA) {
                S.nextToken();
                superclasses.append(toP(F.at(S.pos()).Ident(ident())));
            }
        }
        accept(LBRACE);
        
        ListBuffer<JCTree> members = new ListBuffer<JCTree>();
        
        int errorCount = 0;
        loop: while (true) {
            int memPos = S.pos();
            JCModifiers memMods = modifiersOpt();
            switch (S.token()) {
                        case ATTRIBUTE: {
                JFXAbstractMember tree = attributeDeclaration(memPos, memMods, S.docComment());
                members.append(tree);
                break;
            }            case FUNCTION: {
                JFXAbstractMember tree = functionDeclaration(memPos, memMods, S.docComment());
                members.append(tree);
                break;
            }            case OPERATION: {
                JFXAbstractMember tree = operationDeclaration(memPos, memMods, S.docComment());
                members.append(tree);
                break;
            }            case RBRACE:
                S.nextToken();
                if (memMods.flags != 0) illegal();
                break loop;
            case EOF:
                log.error(pos, "premature.eof");
                break loop;
            default:
                ++errorCount;
                if (errorCount == 1) {
                    reportSyntaxError(S.prevEndPos(), "expected", "attribute, function, or operation");
                } else if (errorCount > 2) {
                    break loop;  // give-up and move on
                } else {
                    // TODO: scan ahead instead
                    S.nextToken(); // maybe better luck with the next token?
                }
            }
        }
        
        JFXClassDeclaration result = toP(F.at(pos).ClassDeclaration(
                mods, name, superclasses.toList(), members.toList()));
        
        attach(result, dc);
        return result;
    }
    
    /**
     *	attributeDecl := accessModifier? 'readonly'? 'attribute' Identifier
     *                         typeReference ary?
     *                         [ 'inverse' memberSelector ]
     *                         [ orderBy | indexOn ]  ';'
     */
    JFXRetroAttributeDeclaration attributeDeclaration(int pos,
            JCModifiers mods,
            String dc) {
        accept(ATTRIBUTE);
        Name name = ident();
        JFXMemberSelector inverse = null;
        JCExpression ordering = null;
        JFXType type = type();
        if (S.token() == INVERSE) {
            S.nextToken();
            inverse = memberSelector();
        }
        accept(SEMI);
        // TODO: orderBy / indexOn
        JFXRetroAttributeDeclaration result =
                toP(F.at(pos).RetroAttributeDeclaration(mods, name, type,
                inverse, ordering));
        attach(result, dc);
        return result;
    }
    
    /**
     *	functionDecl := accessModifier? 'function' Identifier
     *                       formalParameters [ ':' typeSpec ary? ]  ';'
     */
    JFXRetroFunctionMemberDeclaration functionDeclaration(int pos,
            JCModifiers mods,
            String dc) {
        accept(FUNCTION);
        Name name = ident();
        List<JCTree> params = formalParameters();
        JFXType type = typeOpt();
        accept(SEMI);
        JFXRetroFunctionMemberDeclaration result =
                toP(F.at(pos).RetroFunctionDeclaration(mods, name, type,
                params));
        attach(result, dc);
        return result;
    }
    
    /**
     *	operationDecl := accessModifier? 'operation' Identifier
     *                       formalParameters [ ':' typeSpec ary? ]  ';'
     */
    JFXRetroOperationMemberDeclaration operationDeclaration(int pos,
            JCModifiers mods,
            String dc) {
        accept(OPERATION);
        Name name = ident();
        List<JCTree> params = formalParameters();
        JFXType type = typeOpt();
        accept(SEMI);
        JFXRetroOperationMemberDeclaration result =
                toP(F.at(pos).RetroOperationDeclaration(mods, name, type,
                params));
        attach(result, dc);
        return result;
    }
    
    /**
     * attributeDefinition := 'attribute' memberSelector '='
     *                           [ 'bind' 'lazy'? ] expression ';'
     */
    JCStatement attributeDefinition(int pos, String dc) {
        accept(ATTRIBUTE);
        JFXMemberSelector selector = memberSelector();
        accept(EQ);
        JavafxBindStatus bindStatus = bindStatusOpt();
        JCExpression init = expression();
        JCStatement result;
        result = toP(F.at(pos).RetroAttributeDefinition(selector, init, bindStatus));
        attach(result, dc);
        return result;
    }
    
    JCBlock functionBody() {
        return functionBody(S.pos(), 0);
    }

    JCBlock functionBody(int pos, long flags) {
        accept(LBRACE);
        ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        JCExpression value = blockStatements(stats);
        if (value != null)
            stats.append(to(F.at(S.pos()).Return(value)));
        JCBlock t = F.at(pos).Block(flags, stats.toList());
        // the Block node has a field "endpos" for first char of last token, which is
        // usually but not necessarily the last char of the last token.
        t.endpos = S.pos();
        accept(RBRACE);
        return toP(t);
    }

    /**
     * memberFunctionDefinition  :=	'function' memberSelector formalParameters
     *                                               typeReference? ary? functionBody
     * localFunctionDefinition	:=	'function'? Identifier formalParameters
     *                                               typeReference? ary? functionBody
     *
     * functionBody := '=' expression whereVarDecls? ';'
     *              |	'{' ( variableDefinition
     *                      | localFunctionDefinition
     *                      | localOperationDefinition )* 'return' expression ';'? '}'
     */
    JFXStatement functionDefinition(int pos, boolean localOnly,
            String dc) {
        accept(FUNCTION);
        Name className = null;
        Name name = ident();
        if (!localOnly && S.token() == DOT) {
            S.nextToken();
            className = name;
            name = ident();
        }
        List<JCTree> params = formalParameters();
        JFXType type = typeOpt();
        JCBlock body = functionBody();
        JFXStatement result;
        if (className != null) {
            JFXMemberSelector selector = toP(F.at(pos).MemberSelector(className, name));
            result = toP(F.at(pos).RetroFunctionDefinition(selector, type, params, body));
        } else {
            result = toP(F.at(pos).RetroFunctionLocalDefinition(name, type, params, body));
        }
        attach(result, dc);
        return result;
    }
    
    /**
     * memberOperationDefinition := 'operation' memberSelector
     *       formalParameters typeReference? ary? block
     *
     * localOperationDefinition	:=  'operation' Identifier
     *       formalParameters typeReference? ary? block
     */
    JFXStatement operationDefinition(int pos, boolean localOnly,
            String dc) {
        accept(OPERATION);
        Name className = null;
        Name name = ident();
        if (!localOnly && S.token() == DOT) {
            S.nextToken();
            className = name;
            name = ident();
        }
        List<JCTree> params = formalParameters();
        JFXType type = typeOpt();
        JCBlock body = functionBody();
        JFXStatement result;
        if (className != null) {
            JFXMemberSelector selector = toP(F.at(pos).MemberSelector(className, name));
            result = toP(F.at(pos).RetroOperationDefinition(selector, type, params, body));
        } else {
            result = toP(F.at(pos).RetroOperationLocalDefinition(name, type, params, body));
        }
        attach(result, dc);
        return result;
    }
    
    /**
     * moduleItem := changeRule | ...
     *
     * changeRule		:=	'trigger' 'on' changeCondition block
     * changeCondition		:=	'insert' Identifier 'into' memberSelector
     * |	'delete' ( Identifier 'from' memberSelector | memberSelector '[' Identifier ']' )
     * |	'not'? 'assert' Identifier
     * |	[ Identifier ':' ] typeSpec '(' ')'
     * |	[ Identifier '=' ] 'new' typeSpec
     * |	memberSelector ( '[' Identifier ']' )? '=' Identifier
     * |	'(' changeCondition ')'
     *
     *
     * statement := localTriggerStatement | ...
     *
     * localTriggerStatement := 'trigger' 'on'  '(' ( localReplaceTrigger | localInsertTrigger | localDeleteTrigger ) ')' block
     * localReplaceTrigger	::=	Identifier [ '[' Identifier ']' ] '=' expression
     * localInsertTrigger	::=	'insert' Identifier 'into' [ Identifier '='] expression
     * localDeleteTrigger	::=	'delete' Identifier 'from' [Identifier '='] expression
     */
    JFXStatement triggerOn(boolean localOnly, String dc) {
        JFXStatement t = null;
        boolean parens = false;
        int tiggerPos = S.pos();
        accept(TRIGGER);
        accept(ON);
        if (S.token() == LPAREN) {
            S.nextToken();
            parens = true;
        }
        switch (S.token()) {
        case INSERT:
        case DELETE:
        case NOT:
        case ASSERT:
            // not yet implemented
            reportSyntaxError(S.prevEndPos(), "sorry", "not yet implemented");
            break;
        case IDENTIFIER:
            int identPos = S.pos();
            Name name = ident();
            switch (S.token()) {
            case COLON:
                if (localOnly) illegal();
                // TODO: [ Identifier ':' ] typeSpec '(' ')' -implement (well,... figure out what it is first)
                break;
            case DOT: {
                if (localOnly) illegal();
                S.nextToken();
                Name name2 = ident();
                JFXMemberSelector selector = toP(F.at(identPos).MemberSelector(name, name2));
                if (S.token() == LBRACKET) {
                    S.nextToken();
                    JCExpression elementId = toP(F.at(S.pos()).Identifier(ident()));
                    accept(RBRACKET);
                    accept(EQ);
                    JCExpression id = toP(F.at(S.pos()).Identifier(ident()));
                    if (parens) accept(RPAREN);
                    JCBlock block = block();
                    t = toP(F.at(tiggerPos).TriggerOnReplaceElement(selector, elementId, id, block));
                } else {
                    accept(EQ);
                    JCExpression id = toP(F.at(S.pos()).Identifier(ident()));
                    if (parens) accept(RPAREN);
                    JCBlock block = block();
                    t = toP(F.at(tiggerPos).TriggerOnReplace(selector, id, block));
                }
                break;
            }
            case EQ: {
                S.nextToken();
                JCExpression id = toP(F.at(identPos).Identifier(name));
                if (S.token() == NEW) {
                    if (localOnly) illegal();
                    t = triggerOnNew(id, parens, tiggerPos, dc);
                } else {
                    // TODO local replace
                    t = null;
                }
                break;
            }
            default:
                illegal();
                t = null;
                break;
            }
            break;
        case NEW:
            if (localOnly) illegal();
            t = triggerOnNew(null, parens, tiggerPos, dc);
            break;
        default:
            reportSyntaxError(S.prevEndPos(), "error", "expected change rule");
            break;
        }
        attach(t, dc);
        return t;
    }
    
    /**
     * changeRule		:=	'trigger' 'on' changeCondition block
     * changeCondition		:=	...
     * |	[ Identifier '=' ] 'new' Identifier
     *
     */
    JFXStatement triggerOnNew(JCExpression id, boolean parens, int tiggerPos, String dc) {
        accept(NEW);
        JCExpression className = toP(F.at(S.pos()).Identifier(ident()));
        if (parens) accept(RPAREN);
        JCBlock block = block();
        return toP(F.at(tiggerPos).TriggerOnNew(className, id, block));
    }
    
    /**
     * := Identifier type?
     */
    JFXVar var() {
        int pos = S.pos();
        Name name = ident();
        JFXType type = typeOpt();
        return toP(F.at(pos).Var(name, type));
    }
    
    /**
     * type := : typeSpec ary?
     *
     * ary := '?' | '*' | '+'
     */
    JFXType type() {
        accept(COLON);
        if (S.pos() <= errorEndPos) {
            // error recovery -- assume they fogot type, and move on
            return null;
        }
        int pos = S.pos();
        switch (S.token()) {
        case FUNCTION:
        case OPERATION:
            S.nextToken();
            // fall through
        case LPAREN: {
            List<JCTree> params = formalParameters();
            JFXType restype = typeOpt();
            int ary = ary();
            return toP(F.at(pos).TypeFunctional(params, restype, ary));
        }
        case IDENTIFIER: {
            Name className = ident();
            int ary = ary();
            return toP(F.at(pos).TypeClass(className, ary));
        }
        case STAR: {
            int ary = ary();
            return toP(F.at(pos).TypeAny(ary));
        }
        default:
            illegal();
            return null;
        }
    }
    
    /**
     * type := : typeSpec ary?
     *
     * ary := '?' | '*' | '+'
     */
    JFXType typeOpt() {
        if (S.token() == COLON) {
            S.nextToken();
            int pos = S.pos();
            switch (S.token()) {
            case FUNCTION:
            case OPERATION:
                S.nextToken();
                // fall through
            case LPAREN: {
                List<JCTree> params = formalParameters();
                JFXType restype = typeOpt();
                int ary = ary();
                return toP(F.at(pos).TypeFunctional(params, restype, ary));
            }
            case IDENTIFIER: {
                Name className = ident();
                int ary = ary();
                return toP(F.at(pos).TypeClass(className, ary));
            }
            case STAR: {
                int ary = ary();
                return toP(F.at(pos).TypeAny(ary));
            }
            default:
                illegal();
                return null;
            }
        }
        int pos = S.pos();
        int ary = ary();
        if (ary == JFXType.CARDINALITY_OPTIONAL) {
            return null;
        }
        return toP(F.at(pos).TypeUnknown(ary));
    }
    
    /**
     * ary := [ '[' ']' ]
     *
     * For backward compatibility allow
     * ary := [ '?' | '*' | '+' ]
     */
    int ary() {
        switch (S.token()) {
        case LBRACKET:
            S.nextToken();
            accept(RBRACKET);
            return JFXType.CARDINALITY_ANY;
        case QUES:
            S.nextToken();
            return JFXType.CARDINALITY_OPTIONAL;
        case PLUS:
            S.nextToken();
            return JFXType.CARDINALITY_ANY;
        case STAR:
            S.nextToken();
            return JFXType.CARDINALITY_ANY;
        default:
            return JFXType.CARDINALITY_OPTIONAL;
        }
    }
    
    List<JCTree> formalParameters() {
        ListBuffer<JCTree> params = new ListBuffer<JCTree>();
        JFXVar lastParam = null;
        accept(LPAREN);
        while (S.token() != RPAREN) {
            if (lastParam != null) {
                accept(COMMA);
            }
            params.append(lastParam = var());
        }
        accept(RPAREN);
        return params.toList();
    }
    
    /** memberSelector := Identifier '.' Identifier
     */
    JFXMemberSelector memberSelector() {
        int pos = S.pos();
        Name className = ident();
        accept(DOT);
        Name name = ident();
        JFXMemberSelector result =
                toP(F.at(pos).MemberSelector(className, name));
        return result;
    }
    
    
    
    /**
     * Ident = IDENTIFIER
     */
    Name ident() {
        if (S.token() == IDENTIFIER) {
            Name name = S.name();
            S.nextToken();
            return name;
        } else {
            accept(IDENTIFIER);
            return names.error;
        }
    }
    
    /**
     * Qualident = Ident { DOT Ident }
     */
    public JCExpression qualident() {
        JCExpression t = toP(F.at(S.pos()).Ident(ident()));
        while (S.token() == DOT) {
            int pos = S.pos();
            S.nextToken();
            t = toP(F.at(pos).Select(t, ident()));
        }
        return t;
    }
    
    /**
     * Literal =
     *     INTLITERAL
     *   | LONGLITERAL
     *   | FLOATLITERAL
     *   | DOUBLELITERAL
     *   | STRINGLITERAL
     *   | TRUE
     *   | FALSE
     *   | NULL
     */
    JCExpression literal(Name prefix) {
        int pos = S.pos();
        JCExpression t = errorTree;
        switch (S.token()) {
        case INTLITERAL:
            try {
                t = F.at(pos).Literal(
                        TypeTags.INT,
                        Convert.string2int(strval(prefix), S.radix()));
            } catch (NumberFormatException ex) {
                log.error(S.pos(), "int.number.too.large", strval(prefix));
            }
            break;
        case LONGLITERAL:
            try {
                t = F.at(pos).Literal(
                        TypeTags.LONG,
                        new Long(Convert.string2long(strval(prefix), S.radix())));
            } catch (NumberFormatException ex) {
                log.error(S.pos(), "int.number.too.large", strval(prefix));
            }
            break;
        case FLOATLITERAL: {
            String proper = (S.radix() == 16 ? ("0x"+ S.stringVal()) : S.stringVal());
            Float n;
            try {
                n = Float.valueOf(proper);
            } catch (NumberFormatException ex) {
                // error already repoted in scanner
                n = Float.NaN;
            }
            if (n.floatValue() == 0.0f && !isZero(proper))
                log.error(S.pos(), "fp.number.too.small");
            else if (n.floatValue() == Float.POSITIVE_INFINITY)
                log.error(S.pos(), "fp.number.too.large");
            else
                t = F.at(pos).Literal(TypeTags.FLOAT, n);
            break;
        }
        case DOUBLELITERAL: {
            String proper = (S.radix() == 16 ? ("0x"+ S.stringVal()) : S.stringVal());
            Double n;
            try {
                n = Double.valueOf(proper);
            } catch (NumberFormatException ex) {
                // error already reported in scanner
                n = Double.NaN;
            }
            if (n.doubleValue() == 0.0d && !isZero(proper))
                log.error(S.pos(), "fp.number.too.small");
            else if (n.doubleValue() == Double.POSITIVE_INFINITY)
                log.error(S.pos(), "fp.number.too.large");
            else
                t = F.at(pos).Literal(TypeTags.DOUBLE, n);
            break;
        }
        case STRINGLITERAL:
        case QUOTE_LBRACE_STRINGLITERAL:
        case RBRACE_QUOTE_STRINGLITERAL:
        case RBRACE_LBRACE_STRINGLITERAL:
        case FORMAT_STRINGLITERAL:
            t = F.at(pos).Literal(
                    TypeTags.CLASS,
                    S.stringVal());
            break;
        case TRUE: case FALSE:
            t = F.at(pos).Literal(
                    TypeTags.BOOLEAN,
                    (S.token() == TRUE ? 1 : 0));
            break;
        case NULL:
            t = F.at(pos).Literal(
                    TypeTags.BOT,
                    null);
            break;
        default:
            assert false;
        }
        if (t == errorTree)
            t = F.at(pos).Erroneous();
        storeEnd(t, S.endPos());
        S.nextToken();
        
        return t;
    }
    //where
    boolean isZero(String s) {
        char[] cs = s.toCharArray();
        int base = ((Character.toLowerCase(s.charAt(1)) == 'x') ? 16 : 10);
        int i = ((base==16) ? 2 : 0);
        while (i < cs.length && (cs[i] == '0' || cs[i] == '.')) i++;
        return !(i < cs.length && (Character.digit(cs[i], base) > 0));
    }
    
    String strval(Name prefix) {
        String s = S.stringVal();
        return (prefix.len == 0) ? s : prefix + s;
    }
    
    /**
     *  expression		:=	foreach                   // TODO
     *  			|	bindExpression           // TODO
     *  			|	functionExpression           // TODO
     *  			|	operationExpression           // TODO
     *  			|	alphaExpression           // TODO
     *  			|	ifExpression
     *  			|	selectExpression           // TODO
     *  			|	[ '(' typeSpec ary? ')' ] logicalExpression
     *                                              [ assignmentOperator expression
     *                                              | indexOn | orderBy            // TODO
     *                                              | durClause | '++' | '--' ]           // TODO
     *
     * assignmentOperator	:=	'=' | '+=' | '-=' | '*=' | '/=' | '%='
     */
    JCExpression expression() {
        if (S.token() == IF) {
            int pos = S.pos();
            S.nextToken();
            JCExpression cond = expression();
            accept(THEN);
            JCExpression t1 = expression();
            accept(ELSE);
            JCExpression t2 = expression();
            return F.at(pos).Conditional(cond, t1, t2);
        } else {
            JCExpression t = logicalExpression();
            switch (S.token()) {
            case EQ: {
                int pos = S.pos();
                S.nextToken();
                JCExpression t1 = expression();
                return toP(F.at(pos).Assign(t, t1));
            }
            case PLUSEQ:
            case SUBEQ:
            case STAREQ:
            case SLASHEQ:
            case PERCENTEQ:
                int pos = S.pos();
                Token token = S.token();
                S.nextToken();
                JCExpression t1 = expression();
                return F.at(pos).Assignop(optag(token), t, t1);
            default:
                return t;
            }
        }
    }
    
    /**
     * logicalExpression	:=	instanceOfExpression ( logicalOperator instanceOfExpression )*
     * instanceOfExpression	:=	relationalExpression [ 'instanceof' Identifier ]
     * relationalExpression	:=	additiveExpression [ relationalOperator additiveExpression ]
     * additiveExpression	:=	multiplicativeExpression ( addOperator multiplicativeExpression )*
     * multiplicativeExpression :=	unaryExpression ( multiplyOperator unaryExpression )*
     *
     * logicalOperator		:=	'and' | 'or' | 'xor'
     * relationalOperator	:=	'==' | '>' | '<' | '>=' | '<=' | 'in'
     * addOperator		:=	'+' | '-'
     * multiplyOperator		:=	'*' | '/' | '%'
     *
     */
    JCExpression logicalExpression() {
        JCExpression t = term3();
        if (prec(S.token()) >= JavafxTreeInfo.orPrec) {
            return term2Rest(t, JavafxTreeInfo.orPrec);
        } else {
            return t;
        }
    }
    
    JCExpression term2Rest(JCExpression t, int minprec) {
        List<JCExpression[]> savedOd = odStackSupply.elems;
        JCExpression[] odStack = newOdStack();
        List<Token[]> savedOp = opStackSupply.elems;
        Token[] opStack = newOpStack();
        // optimization, was odStack = new Tree[...]; opStack = new Tree[...];
        int top = 0;
        odStack[0] = t;
        int startPos = S.pos();
        Token topOp = ERROR;
        while (prec(S.token()) >= minprec) {
            opStack[top] = topOp;
            top++;
            topOp = S.token();
            int pos = S.pos();
            S.nextToken();
            odStack[top] = term3();  // Out 4 now            odStack[top] = topOp == INSTANCEOF ? type() : term3();
            while (top > 0 && prec(topOp) >= prec(S.token())) {
                odStack[top-1] = makeOp(pos, topOp, odStack[top-1],
                        odStack[top]);
                top--;
                topOp = opStack[top];
            }
        }
        assert top == 0;
        t = odStack[0];
        
        odStackSupply.elems = savedOd; // optimization
        opStackSupply.elems = savedOp; // optimization
        return t;
    }
    //where
    /** Construct a binary or type test node.
     */
    private JCExpression makeOp(int pos,
            Token topOp,
            JCExpression od1,
            JCExpression od2) {
        if (topOp == INSTANCEOF) {
            return F.at(pos).TypeTest(od1, od2);
        } else {
            return F.at(pos).Binary(optag(topOp), od1, od2);
        }
    }
    
    /** optimization: To save allocating a new operand/operator stack
     *  for every binary operation, we use supplys.
     */
    ListBuffer<JCExpression[]> odStackSupply = new ListBuffer<JCExpression[]>();
    ListBuffer<Token[]> opStackSupply = new ListBuffer<Token[]>();
    
    private JCExpression[] newOdStack() {
        if (odStackSupply.elems == odStackSupply.last)
            odStackSupply.append(new JCExpression[infixPrecedenceLevels + 1]);
        JCExpression[] odStack = odStackSupply.elems.head;
        odStackSupply.elems = odStackSupply.elems.tail;
        return odStack;
    }
    
    private Token[] newOpStack() {
        if (opStackSupply.elems == opStackSupply.last)
            opStackSupply.append(new Token[infixPrecedenceLevels + 1]);
        Token[] opStack = opStackSupply.elems.head;
        opStackSupply.elems = opStackSupply.elems.tail;
        return opStack;
    }
    
    /**
     * unaryExpression		:=	unaryOperator? postfixExpression
     * unaryOperator		:=	'#' | '?' | '-' | 'not' | 'sizeof' | 'typeof' | 'reverse' | '++' | '--'
     */
    protected JCExpression term3() {
        int pos = S.pos();
        JCExpression t;
        switch (S.token()) {
        case NOT:
        case PLUSPLUS:
        case SUBSUB:
        case SUB:{
            // TODO: # | ? | sizeof | typeof | reverse
            Token token = S.token();
            S.nextToken();
            if (token == SUB &&
                    (S.token() == INTLITERAL || S.token() == LONGLITERAL) &&
                    S.radix() == 10) {
                mode = EXPR;
                t = literal(names.hyphen);
            } else {
                t = term3();
                return F.at(pos).Unary(unoptag(token), t);
            }
            break;
        }
        case LPAREN:
        {
            S.nextToken();
            mode = EXPR | TYPE | NOPARAMS;
            t = expression();
            // TODO: was             t = term3();   t = termRest(term1Rest(term2Rest(t, JavafxTreeInfo.orPrec)));
            accept(RPAREN);
            /** Out 4 now
             * lastmode = mode;
             * mode = EXPR;
             * if ((lastmode & EXPR) == 0) {
             * JCExpression t1 = term3();
             * return F.at(pos).TypeCast(t, t1);
             * } else if ((lastmode & TYPE) != 0) {
             * switch (S.token()) {
             * //case PLUSPLUS: case SUBSUB:
             * // Javafx change                    case BANG:
             * // Javafx change                    case TILDE:
             * case LPAREN:
             * case THIS:
             * case SUPER:
             * case INTLITERAL:
             * case LONGLITERAL:
             * case FLOATLITERAL:
             * case DOUBLELITERAL:
             * case CHARLITERAL:
             * case STRINGLITERAL:
             * case TRUE:
             * case FALSE:
             * case NULL:
             * case NEW:
             * case IDENTIFIER:
             * case ASSERT:
             * // Javafx change                    case ENUM:
             * // Javafx change                    case BYTE:
             * // Javafx change                    case SHORT:
             * // Javafx change                    case CHAR:
             * // Javafx change                    case INT:
             * // Javafx change                    case LONG:
             * // Javafx change                    case FLOAT:
             * // Javafx change                    case DOUBLE:
             * // Javafx change                    case BOOLEAN:
             * // Javafx change                    case VOID:
             * JCExpression t1 = term3();
             * return F.at(pos).TypeCast(t, t1);
             * }
             * }
             * ****/
        }
        t = toP(F.at(pos).Parens(t));
        break;
        case QUOTE_LBRACE_STRINGLITERAL: {
            ListBuffer<JCExpression> strexp = new ListBuffer<JCExpression>();
            do {
                strexp.append(literal(names.empty));
                if (S.token() == FORMAT_STRINGLITERAL) {
                    strexp.append(literal(names.empty));
                } else {
                    strexp.append(null);
                }
                strexp.append(expression());
            } while (S.token() == RBRACE_LBRACE_STRINGLITERAL);
            if (S.token() == RBRACE_QUOTE_STRINGLITERAL) {
                strexp.append(literal(names.empty));
            } else {
                return illegal();
            }
            t = toP(F.at(S.pos()).StringExpression(strexp.toList()));
            break;
        }
        case THIS:
            t = to(F.at(pos).Identifier(names._this));
            S.nextToken();
            t = identifierRest(t);
            break;
        case SUPER:
            if ((mode & EXPR) != 0) {
                mode = EXPR;
                t = to(superSuffix(F.at(pos).Identifier(names._super)));
            } else return illegal();
            break;
        case INTLITERAL:
        case LONGLITERAL:
        case FLOATLITERAL:
        case DOUBLELITERAL:
        case CHARLITERAL:
        case STRINGLITERAL:
        case TRUE: case FALSE: case NULL:
            t = literal(names.empty);
            break;
        case NEW:
            S.nextToken();
            t = creator(pos);
            break;
        case LBRACE:
            t = blockExpression();
            break;
        case IDENTIFIER:
            t = toP(F.at(S.pos()).Identifier(ident()));
            if (S.token() == LBRACE) {
                t = pureObjectLiteral(t);
            } else {
                t = identifierRest(t);
            }
            break;
        default:
            return illegal();
        }
        while (true) {
            int pos1 = S.pos();
            if (S.token() == DOT) {
                S.nextToken();
                if (S.token() == SUPER) {
                    t = to(F.at(pos1).Select(t, names._super));
                    S.nextToken();
                    t = arguments(t);
                } else {
                    t = toP(F.at(pos1).Select(t, ident()));
                }
            } else {
                break;
            }
        }
/* Out 4 now
        while ((S.token() == PLUSPLUS || S.token() == SUBSUB) && (mode & EXPR) != 0) {
            mode = EXPR;
            t = to(F.at(S.pos()).Unary(
                  S.token() == PLUSPLUS ? JCTree.POSTINC : JCTree.POSTDEC, t));
            S.nextToken();
        }
 */
        return toP(t);
    }
    
    private JCExpression identifierRest(JCExpression t) {
        while (true) {
            int pos = S.pos();
            switch (S.token()) {
            case LPAREN:
                t = arguments(t);
                return t;
            case DOT:
                S.nextToken();
                switch (S.token()) {
                case CLASS:
                    mode = EXPR;
                    t = to(F.at(pos).Select(t, names._class));
                    S.nextToken();
                    return t;
                }
                t = toP(F.at(pos).Select(t, ident()));
                break;
            default:
                return t;
            }
        }
    }
    
    /**
     *   primaryExpression	:=	Identifier? pureObjectLiteral
     *                           |      ...
     *
     *   pureObjectLiteral	:=	'{' ( pureObjectLiteralPart ','? )* ','* '}'
     *   pureObjectLiteralPart	:=	'var' ( ':' Identifier | Identifier typeReference? ary? '=' expression )
     *                           |	'attribute' ':' Identifier
     *                           |	localOperationDefinition
     *                           |	localFunctionDefinition
     *                           |	localTriggerStatement
     *                           |	objectLiteralPart
     */
    JCExpression pureObjectLiteral(JCExpression ident) {
        ListBuffer<JFXStatement> parts = new ListBuffer<JFXStatement>();
        int pos = S.pos();
        accept(LBRACE);
        loop: while (true) {
            JFXStatement part;
            int partPos = S.pos();
            switch (S.token()) {
            case RBRACE:
                S.nextToken();
                return to(F.at(partPos).PureObjectLiteral(ident, parts.toList()));
            case VAR:
                S.nextToken();
                if (S.token() == COLON) {
                    S.nextToken();
                    Name name = ident();
                    part = to(F.at(partPos).VarIsObjectBeingInitialized(name));
                } else {
                    Name name = ident();
                    JFXType type = typeOpt();
                    accept(EQ);
                    S.nextToken();
                    JCExpression init = expression();
                    part = toP(F.at(partPos).VarInit(name, type, init, UNBOUND)); // TODO bind here?
                }
                break;
            case ATTRIBUTE: {
                S.nextToken();
                accept(COLON);
                Name name = ident();
                part = to(F.at(partPos).SetAttributeToObjectBeingInitialized(name));
                break;
            }
            case FUNCTION:
                part = functionDefinition(partPos, true, null);
                break;
            case OPERATION:
                part = functionDefinition(partPos, true, null);
                break;
            case TRIGGER:
                part = triggerOn(true, null);
                break;
            case IDENTIFIER:
                part = objectLiteralPart();
                break;
            default:
                return illegal();
            }
            if (S.token() == COMMA) {
                S.nextToken();
            }
            parts.append(part);
        }
    }
    
    /**
     *   objectLiteralPart	:=	Identifier ':' [ 'bind' 'lazy'?] expression
     */
    JFXStatement objectLiteralPart() {
        int pos = S.pos();
        int bindPos = 0;
        Name name = ident();
        accept(COLON);
        JavafxBindStatus bindStatus = bindStatusOpt();
        JCExpression exp = expression();
        return toP(F.at(pos).ObjectLiteralPart(name, exp, bindStatus));
    }
    
    /** SuperSuffix = Arguments | "." Ident [Arguments]
     */
    JCExpression superSuffix(JCExpression t) {
        S.nextToken();
        if (S.token() == LPAREN) {
            t = arguments(t);
        } else {
            int pos = S.pos();
            accept(DOT);
            t = toP(F.at(pos).Select(t, ident()));
            t = argumentsOpt(t);
        }
        return t;
    }
    
    /** BasicType = BYTE | SHORT | CHAR | INT | LONG | FLOAT | DOUBLE | BOOLEAN
     */
    JCPrimitiveTypeTree basicType() {
        JCPrimitiveTypeTree t = to(F.at(S.pos()).TypeIdent(typetag(S.token())));
        S.nextToken();
        return t;
    }
    
    /** ArgumentsOpt = [ Arguments ]
     */
    JCExpression argumentsOpt(JCExpression t) {
        if ((mode & EXPR) != 0 && S.token() == LPAREN) {
            mode = EXPR;
            return arguments(t);
        } else {
            return t;
        }
    }
    
    /** Arguments = "(" [Expression { COMMA Expression }] ")"
     */
    List<JCExpression> arguments() {
        ListBuffer<JCExpression> args = lb();
        if (S.token() == LPAREN) {
            S.nextToken();
            if (S.token() != RPAREN) {
                args.append(expression());
                while (S.token() == COMMA) {
                    S.nextToken();
                    args.append(expression());
                }
            }
            accept(RPAREN);
        } else {
            syntaxError(S.pos(), "expected", keywords.token2string(LPAREN));
        }
        return args.toList();
    }
    
    JCMethodInvocation arguments(JCExpression t) {
        int pos = S.pos();
        List<JCExpression> args = arguments();
        return toP(F.at(pos).Apply(null, t, args));
    }
    
    /** Creator = Qualident [TypeArguments]  ClassCreatorRest
     */
    JCExpression creator(int newpos) {
        JCExpression t = toP(F.at(S.pos()).Identifier(ident()));
        List<JCExpression> args = arguments();
        return toP(F.at(newpos).NewClass(null, null, t, args, null));
    }
    
    /** ParExpression = "(" Expression ")"
     */
    JCExpression parExpression() {
        accept(LPAREN);
        JCExpression t = expression();
        accept(RPAREN);
        return t;
    }
    
    /** Block = "{" BlockStatements "}"
     */
    JCBlock block(int pos, long flags) {
        accept(LBRACE);
        List<JCStatement> stats = blockStatements();
        JCBlock t = F.at(pos).Block(flags, stats);
        // the Block node has a field "endpos" for first char of last token, which is
        // usually but not necessarily the last char of the last token.
        t.endpos = S.pos();
        accept(RBRACE);
        return toP(t);
    }

    public JCBlock block() {
        return block(S.pos(), 0);
    }
    
    public JFXBlockExpression blockExpression(int pos, long flags) {
        accept(LBRACE);
        ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        JCExpression value = blockStatements(stats);
        JFXBlockExpression t = F.at(pos).BlockExpression(flags, stats.toList(), value);
        // the Block node has a field "endpos" for first char of last token, which is
        // usually but not necessarily the last char of the last token.
        t.endpos = S.pos();
        accept(RBRACE);
        return toP(t);
    }
    
     public JFXBlockExpression blockExpression() {
        return blockExpression(S.pos(), 0);
     }
     
     /** BlockStatements = { BlockStatement }
     *  BlockStatement  = LocalVariableDeclarationStatement
     *                  | ClassOrInterfaceOrEnumDeclaration
     *                  | [Ident ":"] Statement
     *  LocalVariableDeclarationStatement
     *                  = { FINAL | '@' Annotation } Type VariableDeclarators ";"
     */
    @SuppressWarnings("fallthrough")
    List<JCStatement> blockStatements() {
        //todo: skip to anchor on error(?)
        int lastErrPos = -1;
        ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        while (true) {
            int pos = S.pos();
            switch (S.token()) {
            case RBRACE:
            case EOF:
                return stats.toList();
            case LBRACE:
            case IF:
            case FOR:
            case WHILE:
            case DO:
            case TRY:
            case RETURN:
            case THROW:
            case BREAK:
            case CONTINUE:
            case SEMI:
            case ELSE:
            case FINALLY:
            case CATCH:
            case VAR:
            case FUNCTION:
            case OPERATION:
            case TRIGGER:
                stats.append(statement());
                break;
            case ASSERT:
                if (allowAsserts && S.token() == ASSERT) {
                    stats.append(statement());
                    break;
                }
                /* fall through to default */
            default:
                Name name = S.name();
                JCExpression t = expression();
                
                // This Exec is an "ExpressionStatement"; it subsumes the terminating semicolon
                stats.append(to(toStatement(pos, t)));
                accept(SEMI);
            }
            
            // error recovery
            if (S.pos() == lastErrPos)
                return stats.toList();
            if (S.pos() <= errorEndPos) {
                skip(false, true, true, true);
                lastErrPos = S.pos();
            }
            
            // ensure no dangling /** @deprecated */ active
            S.resetDeprecatedFlag();
        }
    }
    
    @SuppressWarnings("fallthrough")
    JCExpression blockStatements(ListBuffer<JCStatement> stats) {
        //todo: skip to anchor on error(?)
        int lastErrPos = -1;
        while (true) {
            int pos = S.pos();
            switch (S.token()) {
                        case RBRACE:
            case EOF:
                System.err.println("exit blockStatements");
                return null;
            case FOR:
            case WHILE:
            case DO:
            case TRY:
            case RETURN:
            case THROW:
            case BREAK:
            case CONTINUE:
            case SEMI:
            case ELSE:
            case FINALLY:
            case CATCH:
            case VAR:
            case FUNCTION:
            case OPERATION:
            case TRIGGER:
                stats.append(statement());
                break;
            case ASSERT:
                if (allowAsserts && S.token() == ASSERT) {
                    stats.append(statement());
                    break;
                }
                /* fall through to default */
            default:
                Name name = S.name();
                JCExpression t = expression();
                if (S.token() == RBRACE)
                    return t;
                // This Exec is an "ExpressionStatement"; it subsumes the terminating semicolon
                stats.append(to(toStatement(pos, t)));
                accept(SEMI);
            }
            
            // error recovery
            if (S.pos() == lastErrPos)
                return null;
            if (S.pos() <= errorEndPos) {
                skip(false, true, true, true);
                lastErrPos = S.pos();
            }
            
            // ensure no dangling /** @deprecated */ active
            S.resetDeprecatedFlag();
        }
    }
    
    /** Statement =
     *       Block
     *     | IF ParExpression Statement [ELSE Statement]
     *     | FOR "(" ForInitOpt ";" [Expression] ";" ForUpdateOpt ")" Statement
     *     | FOR "(" FormalParameter : Expression ")" Statement
     *     | WHILE ParExpression Statement
     *     | DO Statement WHILE ParExpression ";"
     *     | TRY Block ( Catches | [Catches] FinallyPart )
     *     | SWITCH ParExpression "{" SwitchBlockStatementGroups "}"
     *     | SYNCHRONIZED ParExpression Block
     *     | RETURN [Expression] ";"
     *     | THROW Expression ";"
     *     | BREAK [Ident] ";"
     *     | CONTINUE [Ident] ";"
     *     | ASSERT Expression [ ":" Expression ] ";"
     *     | ";"
     *     | ExpressionStatement
     *     | Ident ":" Statement
     */
    @SuppressWarnings("fallthrough")
    public JCStatement statement() {
        int pos = S.pos();
        switch (S.token()) {
        case FUNCTION: {
            return functionDefinition(pos, true, S.docComment());
        }
        case OPERATION: {
            return operationDefinition(pos, true, S.docComment());
        }
        case TRIGGER: {
            return triggerOn(true, S.docComment());
        }
        case WHILE: {
            S.nextToken();
            JCExpression cond = parExpression();
            JCStatement body = statement();
            return F.at(pos).WhileLoop(cond, body);
        }
        case TRY: {
            S.nextToken();
            JCBlock body = block();
            ListBuffer<JCCatch> catchers = new ListBuffer<JCCatch>();
            JCBlock finalizer = null;
            if (S.token() == CATCH || S.token() == FINALLY) {
                // Out 4 now                while (S.token() == CATCH) catchers.append(catchClause());
                if (S.token() == FINALLY) {
                    S.nextToken();
                    finalizer = block();
                }
            } else {
                log.error(pos, "try.without.catch.or.finally");
            }
            return F.at(pos).Try(body, catchers.toList(), finalizer);
        }
        case RETURN: {
            S.nextToken();
            JCExpression result = S.token() == SEMI ? null : expression();
            JCReturn t = to(F.at(pos).Return(result));
            accept(SEMI);
            return t;
        }
        case THROW: {
            S.nextToken();
            JCExpression exc = expression();
            JCThrow t = to(F.at(pos).Throw(exc));
            accept(SEMI);
            return t;
        }
        case BREAK: {
            S.nextToken();
            JCBreak t = to(F.at(pos).Break(null));
            accept(SEMI);
            return t;
        }
        case CONTINUE: {
            S.nextToken();
            JCContinue t =  to(F.at(pos).Continue(null));
            accept(SEMI);
            return t;
        }
        case SEMI:
            S.nextToken();
            return toP(F.at(pos).Skip());
        case ELSE:
            return toP(F.Exec(syntaxError("else.without.if")));
        case FINALLY:
            return toP(F.Exec(syntaxError("finally.without.try")));
        case CATCH:
            return toP(F.Exec(syntaxError("catch.without.try")));
        case VAR: {
            String dc = S.docComment();
            S.nextToken();
            
            Name name = ident();
            JFXType type = typeOpt();
            JCStatement tree;
            if (S.token() == EQ) {
                accept(EQ);
                JavafxBindStatus bindStatus = bindStatusOpt();
                JCExpression init = expression();
                tree = toP(F.at(pos).VarInit(name, type, init, bindStatus));
            } else {
                tree =  toP(F.at(pos).VarStatement(name, type));
            }
            accept(SEMI);
            attach(tree, dc);
            return tree;
        }
        case ASSERT: {
            if (allowAsserts && S.token() == ASSERT) {
                S.nextToken();
                JCExpression assertion = expression();
                JCExpression message = null;
                if (S.token() == COLON) {
                    S.nextToken();
                    message = expression();
                }
                JCAssert t = to(F.at(pos).Assert(assertion, message));
                accept(SEMI);
                return t;
            }
            // else fall through to default case
        }
        default:
            Name name = S.name();
            JCExpression expr = expression();
            {
                // This Exec is an "ExpressionStatement"; it subsumes the terminating semicolon
                JCStatement stat = to(toStatement(pos, expr));
                accept(SEMI);
                return stat;
            }
        }
    }
    
    /** CatchClause     = CATCH "(" FormalParameter ")" Block
     */
    /** Out 4 now
     * JCCatch catchClause() {
     * int pos = S.pos();
     * accept(CATCH);
     * accept(LPAREN);
     * JCVariableDecl formal =
     * variableDeclaratorId(optFinal(Flags.PARAMETER),
     * qualident());
     * accept(RPAREN);
     * JCBlock body = block();
     * return F.at(pos).Catch(formal, body);
     * }
     * **/
    
    /** MoreStatementExpressions = { COMMA StatementExpression }
     */
    <T extends ListBuffer<? super JCStatement>> T moreStatementExpressions(int pos,
            JCExpression first,
            T stats) {
        // This Exec is a "StatementExpression"; it subsumes no terminating token
        stats.append(toP(toStatement(pos, first)));
        while (S.token() == COMMA) {
            S.nextToken();
            pos = S.pos();
            JCExpression t = expression();
            // This Exec is a "StatementExpression"; it subsumes no terminating token
            stats.append(toP(toStatement(pos, t)));
        }
        return stats;
    }
    
    /** ForInit = StatementExpression MoreStatementExpressions
     *           |  { FINAL | '@' Annotation } Type VariableDeclarators
     */
    List<JCStatement> forInit() {
        ListBuffer<JCStatement> stats = lb();
        int pos = S.pos();
/* Out 4 now
        if (S.token() == FINAL || S.token() == MONKEYS_AT) {
            return variableDeclarators(optFinal(0), type(), stats).toList();
        } else {
            JCExpression t = term(EXPR | TYPE);
            if ((lastmode & TYPE) != 0 &&
                (S.token() == IDENTIFIER || S.token() == ASSERT || S.token() == ENUM))
                return variableDeclarators(modifiersOpt(), t, stats).toList();
            else
                return moreStatementExpressions(pos, t, stats).toList();
        }
 */
        return null; // Javafx change TODO:
    }
    
    /** ModifiersOpt = { Modifier }
     *  Modifier = ABSTRACT | PUBLIC | PROTECTED | PRIVATE
     */
    JCModifiers modifiersOpt() {
        long flags = 0;
        int pos = S.pos();
        int lastPos = Position.NOPOS;
        loop:
        while (true) {
            long flag;
            switch (S.token()) {
            case ABSTRACT     : flag = Flags.ABSTRACT; break;
            case PRIVATE     : flag = Flags.PRIVATE; break;
            case PROTECTED   : flag = Flags.PROTECTED; break;
            case PUBLIC      : flag = Flags.PUBLIC; break;
            case READONLY      : flag = Flags.FINAL; break;
            default: break loop;
            }
            if ((flags & flag) != 0) log.error(S.pos(), "repeated.modifier");
            lastPos = S.pos();
            S.nextToken();
        }
        
        /* A modifiers tree with no modifier tokens or annotations
         * has no text position. */
        if (flags == 0)
            pos = Position.NOPOS;
        
        JCModifiers mods = F.at(pos).Modifiers(flags);
        if (pos != Position.NOPOS)
            storeEnd(mods, S.prevEndPos());
        return mods;
    }
    
    JavafxBindStatus bindStatusOpt() {
        JavafxBindStatus bindStatus = UNBOUND;
        if (S.token() == BIND || S.token() == STAYS) {
            S.nextToken();
            if (S.token() == LAZY) {
                S.nextToken();
                bindStatus = LAZY_UNIDIBIND;
            } else {
                bindStatus = UNIDIBIND;
            }
        }
        if (S.token() == BIBIND || S.token() == TIE) {
            S.nextToken();
            if (S.token() == LAZY) {
                S.nextToken();
                bindStatus = LAZY_BIDIBIND;
            } else {
                bindStatus = BIDIBIND;
            }
        }
        return bindStatus;
    }
    
    /** QualidentList = Qualident {"," Qualident}
     */
    List<JCExpression> qualidentList() {
        ListBuffer<JCExpression> ts = new ListBuffer<JCExpression>();
        ts.append(qualident());
        while (S.token() == COMMA) {
            S.nextToken();
            ts.append(qualident());
        }
        return ts.toList();
    }
    
    /* ---------- auxiliary methods -------------- */
    
    /** Check that given tree is a legal expression statement.
     */
    protected JCExpression checkExprStat(JCExpression t) {
        switch(t.getTag()) {
        case JCTree.PREINC: case JCTree.PREDEC:
        case JCTree.POSTINC: case JCTree.POSTDEC:
        case JCTree.ASSIGN:
        case JCTree.BITOR_ASG: case JCTree.BITXOR_ASG: case JCTree.BITAND_ASG:
        case JCTree.SL_ASG: case JCTree.SR_ASG: case JCTree.USR_ASG:
        case JCTree.PLUS_ASG: case JCTree.MINUS_ASG:
        case JCTree.MUL_ASG: case JCTree.DIV_ASG: case JCTree.MOD_ASG:
        case JCTree.APPLY: case JCTree.NEWCLASS:
        case JCTree.ERRONEOUS:
        case JCTree.CONDEXPR:
        case JavafxTag.BLOCK_EXPRESSION:
            return t;
        default:
            log.error(t.pos, "not.stmt");
            return F.at(t.pos).Erroneous(List.<JCTree>of(t));
        }
    }
    
    /** Return precedence of operator represented by token,
     *  -1 if token is not a binary operator. @see JavafxTreeInfo.opPrec
     */
    static int prec(Token token) {
        int oc = optag(token);
        return (oc >= 0) ? JavafxTreeInfo.opPrec(oc) : -1;
    }
    
    /** Return operation tag of binary operator represented by token,
     *  -1 if token is not a binary operator.
     */
    static int optag(Token token) {
        switch (token) {
        case OR:
            return JCTree.OR;
        case XOR:
            return JavafxTag.XOR;
        case AND:
            return JCTree.AND;
        case EQEQ:
            return JCTree.EQ;
        case LTGT:
            return JCTree.NE;
        case NOT:
            return JCTree.NOT;
        case LT:
            return JCTree.LT;
        case GT:
            return JCTree.GT;
        case LTEQ:
            return JCTree.LE;
        case GTEQ:
            return JCTree.GE;
            // Javafx change        case LTLT:
            // Javafx change            return JCTree.SL;
            // Javafx change        case LTLTEQ:
            // Javafx change            return JCTree.SL_ASG;
            // Javafx change        case GTGT:
            // Javafx change            return JCTree.SR;
            // Javafx change        case GTGTEQ:
            // Javafx change            return JCTree.SR_ASG;
            // Javafx change        case GTGTGT:
            // Javafx change            return JCTree.USR;
            // Javafx change        case GTGTGTEQ:
            // Javafx change            return JCTree.USR_ASG;
            
            // Javafx change
        case PLUS:
            return JCTree.PLUS;
        case PLUSEQ:
            return JCTree.PLUS_ASG;
        case SUB:
            return JCTree.MINUS;
        case SUBEQ:
            return JCTree.MINUS_ASG;
        case STAR:
            return JCTree.MUL;
        case STAREQ:
            return JCTree.MUL_ASG;
        case SLASH:
            return JCTree.DIV;
        case SLASHEQ:
            return JCTree.DIV_ASG;
        case PERCENT:
            return JCTree.MOD;
        case PERCENTEQ:
            return JCTree.MOD_ASG;
        case INSTANCEOF:
            return JCTree.TYPETEST;
        default:
            return -1;
        }
    }
    
    /** Return operation tag of unary operator represented by token,
     *  -1 if token is not a binary operator.
     */
    static int unoptag(Token token) {
        switch (token) {
        case PLUS:
            return JCTree.POS;
        case SUB:
            return JCTree.NEG;
/* Out 4 now
 
 
        case BANG:
            return JCTree.NOT;
        case TILDE:
            return JCTree.COMPL;
        case PLUSPLUS:
            return JCTree.PREINC;
        case SUBSUB:
            return JCTree.PREDEC;
 */
        default:
            return -1;
        }
    }
    
    /** Return type tag of basic type represented by token,
     *  -1 if token is not a basic type identifier.
     */
    static int typetag(Token token) {
        switch (token) {
/* Out 4 now
 
        case BYTE:
            return TypeTags.BYTE;
        case CHAR:
            return TypeTags.CHAR;
        case SHORT:
            return TypeTags.SHORT;
        case INT:
            return TypeTags.INT;
        case LONG:
            return TypeTags.LONG;
        case FLOAT:
            return TypeTags.FLOAT;
        case DOUBLE:
            return TypeTags.DOUBLE;
        case BOOLEAN:
            return TypeTags.BOOLEAN;
 */
        default:
            return -1;
        }
    }
    
    void checkGenerics() {
        if (!allowGenerics) {
            log.error(S.pos(), "generics.not.supported.in.source", source.name);
            allowGenerics = true;
        }
    }
    void checkVarargs() {
        if (!allowVarargs) {
            log.error(S.pos(), "varargs.not.supported.in.source", source.name);
            allowVarargs = true;
        }
    }
    void checkForeach() {
        if (!allowForeach) {
            log.error(S.pos(), "foreach.not.supported.in.source", source.name);
            allowForeach = true;
        }
    }
    void checkStaticImports() {
        if (!allowStaticImport) {
            log.error(S.pos(), "static.import.not.supported.in.source", source.name);
            allowStaticImport = true;
        }
    }
    void checkAnnotations() {
        if (!allowAnnotations) {
            log.error(S.pos(), "annotations.not.supported.in.source", source.name);
            allowAnnotations = true;
        }
    }
}
