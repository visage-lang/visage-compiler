/*
 * Copyright 2010 Sun Microsystems, Inc.  All Rights Reserved.
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
package com.sun.tools.example.debug.tty;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.ClassUnloadEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.event.WatchpointEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.tools.example.debug.expr.ExpressionParser;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * This class is programmable equivalent of TTY.java (which is the main class of
 * the command line tool "jdb"). Note: I've to put this class in this specific
 * package so that we can access package-private implementation classes.
 *
 * @author sundar
 */
public class FXJDB implements EventNotifier {
    private FXJDBListener listener;
    private Commands evaluator;
    private EventHandler handler;

    public FXJDB(FXJDBListener listener, String connectorSpec, boolean openNow, int flags) {
        this.listener = listener;
        this.evaluator = new Commands();
        MessageOutput.textResources = ResourceBundle.getBundle(
                "com.sun.tools.example.debug.tty.TTYResources", Locale.getDefault());
        if (connectorSpec.charAt(connectorSpec.length() - 1) != ',') {
            connectorSpec = connectorSpec.concat(",");
        }
        Env.init(connectorSpec, openNow, flags);
        if (Env.connection().isOpen() && Env.vm().canBeModified()) {
            this.handler = new EventHandler(this, true);
        }
    }

    public FXJDB(FXJDBListener listener, String connectorSpec, boolean openNow) {
        this(listener, connectorSpec, openNow, VirtualMachine.TRACE_NONE);
    }

    public FXJDB(FXJDBListener listener, String connectorSpec) {
        this(listener, connectorSpec, false);
    }

    /***
    public static void main(String[] args) throws Exception {
        final FXJDB fxdb = new FXJDB(null, args[0]) {
            @Override
            public void breakpointEvent(BreakpointEvent evt) {
                setCurrentThread(evt.thread());
                System.out.println("stop at " + evt);
                where();
                resume();
            }
        };

        fxdb.stop("in Main.main");
        fxdb.run("");
    }
     ***/

    public VirtualMachine vm() {
        return Env.vm();
    }

    public void shutdown() {
        if (handler != null) {
            handler.shutdown();
        }
        Env.shutdown();
    }

    public void shutdown(String message) {
        if (handler != null) {
            handler.shutdown();
        }
        Env.shutdown(message);
    }

    // get/set source path
    public void setSourcePath(String path) {
        Env.setSourcePath(path);
    }

    public String getSourcePath() {
        return Env.getSourcePath();
    }

    // evaluate expression
    public Value evaluate(String expr) {
        Value result = null;
        ExpressionParser.GetFrame frameGetter = null;
        try {
            final ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
            if (threadInfo != null && threadInfo.getCurrentFrame() != null) {
                frameGetter = new ExpressionParser.GetFrame() {
                    public StackFrame get() throws IncompatibleThreadStateException {
                        return threadInfo.getCurrentFrame();
                    }
                };
            }
            result = ExpressionParser.evaluate(expr, Env.vm(), frameGetter);
        } catch (RuntimeException rexp) {
            throw rexp;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
        return result;
    }

    // set/get current thread and threadgroup.
    public void setCurrentThread(ThreadReference tref) {
        ThreadInfo.setCurrentThread(tref);
    }

    public ThreadReference getCurrentThread() {
        return ThreadInfo.getCurrentThreadInfo().getThread();
    }

    public void setCurrentThreadGroup(ThreadGroupReference tgref) {
        ThreadInfo.setThreadGroup(tgref);
    }

    public ThreadGroupReference getCurrentThreadGroup() {
        return ThreadInfo.group();
    }

    // return string id for the given ThreadReference object
    public String threadId(ThreadReference tref) {
        return "t@" + tref.uniqueID();
    }

    // return string id for the given ThreadGroupReference object
    public String threadGroupId(ThreadGroupReference tgref) {
        return tgref.name();
    }

    // commands
    public void catchException(String command) {
        evaluator.commandCatchException(new StringTokenizer(command));
    }

    public void classes() {
        evaluator.commandClasses();
    }

    public void classPath() {
        evaluator.commandClasspath(new StringTokenizer(""));
    }
    
    public void clear() {
        clear("");
    }

    public void clear(String command) {
        evaluator.commandClear(new StringTokenizer(command));
    }

    public void cont() {
        evaluator.commandCont();
    }

    public void dump(String command) {
        evaluator.commandPrint(new StringTokenizer(command), true);
    }

    public void disableGC(String command) {
        evaluator.commandDisableGC(new StringTokenizer(command));
    }

    public void down() {
        down("");
    }

    public void down(String command) {
        evaluator.commandDown(new StringTokenizer(command));
    }

    public void enableGC(String command) {
        evaluator.commandEnableGC(new StringTokenizer(command));
    }

    public void ignoreException() {
        ignoreException("");
    }

    public void ignoreException(String command) {
        evaluator.commandIgnoreException(new StringTokenizer(command));
    }

    public void interrupt() {
        interrupt("");
    }

    public void interrupt(String command) {
        evaluator.commandInterrupt(new StringTokenizer(command));
    }

    public void kill() {
        kill("");
    }

    public void kill(String command) {
        evaluator.commandKill(new StringTokenizer(command));
    }

    public void lines(String command) {
        evaluator.commandLines(new StringTokenizer(command));
    }

    public void list() {
        list("");
    }
    
    public void list(String command) {
        evaluator.commandList(new StringTokenizer(command));
    }

    public void locals() {
        evaluator.commandLocals();
    }

    public void lock(String command) {
        evaluator.commandLock(new StringTokenizer(command));
    }

    public void next() {
        evaluator.commandNext();
    }

    public void pop() {
        pop("");
    }
    
    public void pop(String command) {
        evaluator.commandPopFrames(new StringTokenizer(command), false);
    }

    public void print(String command) {
        evaluator.commandPrint(new StringTokenizer(command), false);
    }

    public void redefine(String command) {
        evaluator.commandRedefine(new StringTokenizer(command));
    }

    public void reenter() {
        reenter("");
    }

    public void reenter(String command) {
        evaluator.commandPopFrames(new StringTokenizer(command), true);
    }

    public void run() {
        run("");
    }
    
    public void run(String command) {
        evaluator.commandRun(new StringTokenizer(command));
        if ((handler == null) && Env.connection().isOpen()) {
            handler = new EventHandler(this, false);
        }
    }

    public void resume() {
        resume("");
    }

    public void resume(String command) {
        evaluator.commandResume(new StringTokenizer(command));
    }

    public void set(String command) {
        evaluator.commandSet(new StringTokenizer(command));
    }

    public void sourcePath() {
        sourcePath("");
    }

    public void sourcePath(String command) {
        evaluator.commandUse(new StringTokenizer(command));
    }

    public void step() {
        step("");
    }

    public void step(String command) {
        evaluator.commandStep(new StringTokenizer(command));
    }

    public void stepi() {
        evaluator.commandStepi();
    }

    public void stop() {
        stop("");
    }

    public void stop(String command) {
        evaluator.commandStop(new StringTokenizer(command));
    }

    public void suspend() {
        suspend("");
    }
    
    public void suspend(String command) {
        evaluator.commandSuspend(new StringTokenizer(command));
    }

    public void thread(String command) {
        evaluator.commandThread(new StringTokenizer(command));
    }

    public void threadGroup(String command) {
        evaluator.commandThreadGroup(new StringTokenizer(command));
    }

    public void threads() {
        threads("");
    }

    public void threads(String command) {
        evaluator.commandThreads(new StringTokenizer(command));
    }

    public void threadGroups() {
        evaluator.commandThreadGroups();
    }
    
    public void trace() {
        trace("");
    }

    public void trace(String command) {
        evaluator.commandTrace(new StringTokenizer(command));
    }

    public void untrace() {
        untrace("");
    }
    
    public void untrace(String command) {
        evaluator.commandUntrace(new StringTokenizer(command));
    }

    public void unwatch(String command) {
        evaluator.commandUnwatch(new StringTokenizer(command));
    }

    public void up() {
        up("");
    }

    public void up(String command) {
        evaluator.commandUp(new StringTokenizer(command));
    }

    public void watch(String command) {
        evaluator.commandWatch(new StringTokenizer(command));
    }

    public void where() {
        where("");
    }

    public void where(String command) {
        evaluator.commandWhere(new StringTokenizer(command), false);
    }

    public void wherei() {
        wherei("");
    }

    public void wherei(String command) {
        evaluator.commandWhere(new StringTokenizer(command), true);
    }

    public void quit() {
        shutdown();
    }

    // Internals only below this point - methods implementing EventListener interface
    public void breakpointEvent(BreakpointEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.breakpointEvent(evt);
        }
    }

    public void classPrepareEvent(ClassPrepareEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.classPrepareEvent(evt);
        }
    }

    public void classUnloadEvent(ClassUnloadEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.classUnloadEvent(evt);
        }
    }

    public void exceptionEvent(ExceptionEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.exceptionEvent(evt);
        }
    }

    public void fieldWatchEvent(WatchpointEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.fieldWatchEvent(evt);
        }
    }

    public void methodEntryEvent(MethodEntryEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.methodEntryEvent(evt);
        }
    }

    public boolean methodExitEvent(MethodExitEvent evt) {
        if (listener != null) {
            Thread.yield();
            return listener.methodExitEvent(evt);
        } else {
            return true;
        }
    }

    public void receivedEvent(Event evt) {
        if (listener != null) {
            Thread.yield();
            listener.receivedEvent(evt);
        }
    }

    public void stepEvent(StepEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.stepEvent(evt);
        }
    }

    public void threadDeathEvent(ThreadDeathEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.threadDeathEvent(evt);
        }
    }

    public void threadStartEvent(ThreadStartEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.threadStartEvent(evt);
        }
    }

    public void vmDeathEvent(VMDeathEvent evt) {
        if (listener != null) {
            listener.vmDeathEvent(evt);
        }
    }

    public void vmDisconnectEvent(VMDisconnectEvent evt) {
        if (listener != null) {
            listener.vmDisconnectEvent(evt);
        }
    }

    public void vmInterrupted() {
        if (listener != null) {
            Thread.yield();
            listener.vmInterrupted();
        }
    }

    public void vmStartEvent(VMStartEvent evt) {
        if (listener != null) {
            Thread.yield();
            listener.vmStartEvent(evt);
        }
    }
}