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

package com.sun.javafx.tools.debug.tty;

import com.sun.javafx.jdi.FXBootstrap;
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
import com.sun.javafx.tools.debug.expr.ExpressionParser;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.WatchpointRequest;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * This class is programmable equivalent of TTY.java (which is the main class of
 * the command line tool "jfxdb").
 *
 * @author sundar
 */
public class Debugger {
    private final List<EventNotifier> listeners = Collections.synchronizedList(new LinkedList());
    private Commands evaluator;
    private EventHandler handler;

    // "args" is same as command line options supported by TTY.java except that
    // -listconnectors, -version and -help options are not supported.
    public Debugger(EventNotifier listener, String args) {
        if (listener != null) {
            this.listeners.add(listener);
        }
        this.evaluator = new Commands();
        init((args == null)? null : args.split(" "));
        if (Env.connection().isOpen() && Env.vm().canBeModified()) {
            this.handler = new EventHandler(new EventNotifierImpl(), true);
        }
    }

    public Debugger(String args) {
        this(null, args);
    }

    public Debugger() {
        this(null);
    }

    public VirtualMachine vm() {
        return Env.vm();
    }

    // event requests, queues and listeners.
    public EventRequestManager eventRequestManager() {
        return vm().eventRequestManager();
    }

    public EventQueue eventQueue() {
        return vm().eventQueue();
    }

    public void addListener(EventNotifier notifier) {
        listeners.add(notifier);
    }

    public void removeListener(EventNotifier notifier) {
        listeners.remove(notifier);
    }

    // shutdown the target VM
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

    // queries to target VM
    public ReferenceType findReferenceType(String name) {
        List rts = vm().classesByName(name);
        Iterator iter = rts.iterator();
        while (iter.hasNext()) {
            ReferenceType rt = (ReferenceType)iter.next();
            if (rt.name().equals(name)) {
                return rt;
            }
        }
        return null;
    }

    public Method findMethod(ReferenceType rt, String name, String signature) {
        List methods = rt.methods();
        Iterator iter = methods.iterator();
        while (iter.hasNext()) {
            Method method = (Method)iter.next();
            if (method.name().equals(name) &&
                    method.signature().equals(signature)) {
                return method;
            }
        }
        return null;
    }

    public Location findLocation(ReferenceType rt, int lineNumber)
            throws AbsentInformationException {
        List locs = rt.locationsOfLine(lineNumber);
        if (locs.size() == 0) {
            throw new IllegalArgumentException("Bad line number");
        } else if (locs.size() > 1) {
            throw new IllegalArgumentException("Line number has multiple locations");
        }

        return (Location)locs.get(0);
    }

    // synchronous event-request-and-wait commands - various step, resumeTo 
    // and various waitForXX methods

    private StepEvent doStep(ThreadReference thread, int gran, int depth) {
        final StepRequest sr =
                eventRequestManager().createStepRequest(thread, gran, depth);
        sr.addClassExclusionFilter("java.*");
        sr.addClassExclusionFilter("sun.*");
        sr.addClassExclusionFilter("com.sun.*");
        sr.addCountFilter(1);
        sr.enable();
        StepEvent retEvent = (StepEvent)waitForRequestedEvent(sr);
        eventRequestManager().deleteEventRequest(sr);
        return retEvent;
    }

    public StepEvent stepIntoInstruction(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_MIN, StepRequest.STEP_INTO);
    }

    public StepEvent stepIntoLine(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
    }

    public StepEvent stepOverInstruction(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_MIN, StepRequest.STEP_OVER);
    }

    public StepEvent stepOverLine(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
    }

    public StepEvent stepOut(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_LINE, StepRequest.STEP_OUT);
    }

    public BreakpointEvent resumeTo(Location loc) {
        final BreakpointRequest request = eventRequestManager().createBreakpointRequest(loc);
        request.addCountFilter(1);
        request.enable();
        return (BreakpointEvent)waitForRequestedEvent(request);
    }

    public BreakpointEvent resumeTo(String clsName, String methodName,
            String methodSignature) {
        ReferenceType rt = findReferenceType(clsName);
        if (rt == null) {
            rt = resumeToPrepareOf(clsName).referenceType();
        }

        Method method = findMethod(rt, methodName, methodSignature);
        if (method == null) {
            throw new IllegalArgumentException("Bad method name/signature");
        }

        return resumeTo(method.location());
    }

    public BreakpointEvent resumeTo(String clsName, int lineNumber) throws AbsentInformationException {
        ReferenceType rt = findReferenceType(clsName);
        if (rt == null) {
            rt = resumeToPrepareOf(clsName).referenceType();
        }

        return resumeTo(findLocation(rt, lineNumber));
    }

    public ClassPrepareEvent resumeToPrepareOf(String className) {
        final ClassPrepareRequest request =
                eventRequestManager().createClassPrepareRequest();
        request.addClassFilter(className);
        request.addCountFilter(1);
        request.enable();
        return (ClassPrepareEvent)waitForRequestedEvent(request);
    }

    public interface EventFilter {
        public boolean match(Event evt);
    }

    public Event waitForEvent(final EventFilter filter) {
        class EventNotification {
            Event event;
            boolean disconnected = false;
        }

        final EventNotification en = new EventNotification();
        EventNotifierAdapter adapter = new EventNotifierAdapter() {
            @Override
            public void receivedEvent(Event event) {
                if (filter.match(event)) {
                    synchronized (en) {
                        en.event = event;
                        en.notifyAll();
                    }
                    removeThisListener();
                } else if (event instanceof VMDisconnectEvent) {
                    synchronized (en) {
                        en.disconnected = true;
                        en.notifyAll();
                    }
                    removeThisListener();
                }
            }
        };

        addListener(adapter);

        try {
            synchronized (en) {
                vm().resume();
                while (!en.disconnected && (en.event == null)) {
                    en.wait();
                }
            }
        } catch (InterruptedException e) {
            return null;
        }

        if (en.disconnected) {
            throw new RuntimeException("VM Disconnected before requested event occurred");
        }

        if (en.event.request().suspendPolicy() == EventRequest.SUSPEND_ALL) {
            ThreadInfo.invalidateAll();
            ThreadInfo.setCurrentThread(EventHandler.eventThread(en.event));
        }
        return en.event;
    }

    public Event waitForRequestedEvent(final EventRequest request) {
        return waitForEvent(new EventFilter() {
            public boolean match(Event evt) {
                return request.equals(evt.request());
            }
        });
    }

    public BreakpointEvent waitForBreakpointEvent() {
        return (BreakpointEvent) waitForEvent(new EventFilter() {
            public boolean match(Event evt) {
                return (evt instanceof BreakpointEvent);
            }
        });
    }

    public StepEvent waitForStepEvent() {
        return (StepEvent) waitForEvent(new EventFilter() {
            public boolean match(Event evt) {
                return (evt instanceof StepEvent);
            }
        });
    }

    public WatchpointEvent waitForWatchpointEvent() {
        return (WatchpointEvent) waitForEvent(new EventFilter() {
            public boolean match(Event evt) {
                return (evt instanceof WatchpointEvent);
            }
        });
    }

    public ExceptionEvent waitForExceptionEvent() {
        return (ExceptionEvent) waitForEvent(new EventFilter() {
            public boolean match(Event evt) {
                return (evt instanceof ExceptionEvent);
            }
        });
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

    // get/set source path - used to list source code.
    public void setSourcePath(String path) {
        Env.setSourcePath(path);
    }

    public String getSourcePath() {
        return Env.getSourcePath();
    }

    public void setOutput(PrintStream out) {
        MessageOutput.setOutput(out);
    }

    public void setError(PrintStream err) {
        MessageOutput.setOutput(err);
    }

    public void resetOutputs() {
        MessageOutput.resetOutputs();
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

    // Event request returning commands will return null for all deferred events.
    public ExceptionRequest catchException(String command) {
        return evaluator.commandCatchException(new StringTokenizer(command));
    }

    public void classes() {
        evaluator.commandClasses();
    }

    public boolean classPath() {
        return evaluator.commandClasspath(new StringTokenizer(""));
    }

    public boolean clear() {
        return clear("");
    }

    public boolean clear(String command) {
        return evaluator.commandClear(new StringTokenizer(command));
    }

    public boolean cont() {
        return evaluator.commandCont();
    }

    public void dump(String command) {
        evaluator.commandPrint(new StringTokenizer(command), true);
    }

    public void disableGC(String command) {
        evaluator.commandDisableGC(new StringTokenizer(command));
    }

    public boolean down() {
        return down("");
    }

    public boolean down(String command) {
        return evaluator.commandDown(new StringTokenizer(command));
    }

    public void enableGC(String command) {
        evaluator.commandEnableGC(new StringTokenizer(command));
    }

    public boolean ignoreException() {
        return ignoreException("");
    }

    public boolean ignoreException(String command) {
        return evaluator.commandIgnoreException(new StringTokenizer(command));
    }

    public boolean interrupt() {
        return interrupt("");
    }

    public boolean interrupt(String command) {
        return evaluator.commandInterrupt(new StringTokenizer(command));
    }

    public void kill() {
        kill("");
    }

    public void kill(String command) {
        evaluator.commandKill(new StringTokenizer(command));
    }

    public boolean lines(String command) {
        return evaluator.commandLines(new StringTokenizer(command));
    }

    public void list() {
        list("");
    }

    public void list(String command) {
        evaluator.commandList(new StringTokenizer(command));
    }

    public boolean locals() {
        return evaluator.commandLocals();
    }

    public void lock(String command) {
        evaluator.commandLock(new StringTokenizer(command));
    }

    public StepRequest next() {
        return evaluator.commandNext();
    }

    public boolean pop() {
        return pop("");
    }

    public boolean pop(String command) {
        return evaluator.commandPopFrames(new StringTokenizer(command), false);
    }

    public void print(String command) {
        evaluator.commandPrint(new StringTokenizer(command), false);
    }

    public boolean redefine(String command) {
        return evaluator.commandRedefine(new StringTokenizer(command));
    }

    public boolean reenter() {
        return reenter("");
    }

    public boolean reenter(String command) {
        return evaluator.commandPopFrames(new StringTokenizer(command), true);
    }

    public void run() {
        run("");
    }

    public boolean run(String command) {
        boolean result = evaluator.commandRun(new StringTokenizer(command));
        if ((handler == null) && Env.connection().isOpen()) {
            handler = new EventHandler(new EventNotifierImpl(), false);
        }
        return result;
    }

    public boolean resume() {
        return resume("");
    }

    public boolean resume(String command) {
        return evaluator.commandResume(new StringTokenizer(command));
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

    public StepRequest step() {
        return step("");
    }

    public StepRequest step(String command) {
        return evaluator.commandStep(new StringTokenizer(command));
    }

    public StepRequest stepi() {
        return evaluator.commandStepi();
    }

    public BreakpointRequest stop() {
        return stop("");
    }

    public BreakpointRequest stop(String command) {
        return evaluator.commandStop(new StringTokenizer(command));
    }

    public boolean suspend() {
        return suspend("");
    }

    public boolean suspend(String command) {
        return evaluator.commandSuspend(new StringTokenizer(command));
    }

    public boolean thread(String command) {
        return evaluator.commandThread(new StringTokenizer(command));
    }

    public boolean threadGroup(String command) {
        return evaluator.commandThreadGroup(new StringTokenizer(command));
    }

    public boolean threads() {
        return threads("");
    }

    public boolean threads(String command) {
        return evaluator.commandThreads(new StringTokenizer(command));
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

    public boolean up() {
        return up("");
    }

    public boolean up(String command) {
        return evaluator.commandUp(new StringTokenizer(command));
    }

    public WatchpointRequest watch(String command) {
        return evaluator.commandWatch(new StringTokenizer(command));
    }

    public boolean where() {
        return where("");
    }

    public boolean where(String command) {
        return evaluator.commandWhere(new StringTokenizer(command), false);
    }

    public boolean wherei() {
        return wherei("");
    }

    public boolean wherei(String command) {
        return evaluator.commandWhere(new StringTokenizer(command), true);
    }

    public void quit() {
        shutdown();
    }

    // Internals only below this point
    private static void usageError(String messageKey) {
        throw new IllegalArgumentException(MessageOutput.format(messageKey));
    }

    private static void usageError(String messageKey, String argument) {
        throw new IllegalArgumentException(MessageOutput.format(messageKey, argument));
    }

    private static boolean supportsSharedMemory() {
        for (Connector connector : FXBootstrap.virtualMachineManager().allConnectors()) {
            if (connector.transport() == null) {
                continue;
            }
            if ("dt_shmem".equals(connector.transport().name())) {
                return true;
            }
        }
        return false;
    }

    private static String addressToSocketArgs(String address) {
        int index = address.indexOf(':');
        if (index != -1) {
            String hostString = address.substring(0, index);
            String portString = address.substring(index + 1);
            return "hostname=" + hostString + ",port=" + portString;
        } else {
            return "port=" + address;
        }
    }

    private static boolean hasWhitespace(String string) {
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (Character.isWhitespace(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static String addArgument(String string, String argument) {
        if (hasWhitespace(argument) || argument.indexOf(',') != -1) {
            // Quotes were stripped out for this argument, add 'em back.
            StringBuffer buffer = new StringBuffer(string);
            buffer.append('"');
            for (int i = 0; i < argument.length(); i++) {
                char c = argument.charAt(i);
                if (c == '"') {
                    buffer.append('\\');
                }
                buffer.append(c);
            }
            buffer.append("\" ");
            return buffer.toString();
        } else {
            return string + argument + ' ';
        }
    }

    private static void init(String[] argv) {
        String cmdLine = "";
        String javaArgs = "";
        int traceFlags = VirtualMachine.TRACE_NONE;
        boolean launchImmediately = false;
        String connectSpec = null;

        MessageOutput.textResources = ResourceBundle.getBundle("com.sun.javafx.tools.debug.tty.TTYResources",
                Locale.getDefault());

        // don't exit this VM on Env.shutdown()
        Env.setExitDebuggerVM(false);

        if (argv != null) {
            for (int i = 0; i < argv.length; i++) {
                String token = argv[i];
                if (token.equals("-dbgtrace")) {
                    if ((i == argv.length - 1) ||
                            !Character.isDigit(argv[i + 1].charAt(0))) {
                        traceFlags = VirtualMachine.TRACE_ALL;
                    } else {
                        String flagStr = "";
                        try {
                            flagStr = argv[++i];
                            traceFlags = Integer.decode(flagStr).intValue();
                        } catch (NumberFormatException nfe) {
                            usageError("dbgtrace flag value must be an integer:",
                                    flagStr);
                            return;
                        }
                    }
                } else if (token.equals("-X")) {
                    usageError("Use java minus X to see");
                    return;
                } else if ( // Standard VM options passed on
                        token.equals("-v") || token.startsWith("-v:") || // -v[:...]
                        token.startsWith("-verbose") || // -verbose[:...]
                        token.startsWith("-D") ||
                        // -classpath handled below
                        // NonStandard options passed on
                        token.startsWith("-X") ||
                        // Old-style options (These should remain in place as long as
                        //  the standard VM accepts them)
                        token.equals("-noasyncgc") || token.equals("-prof") ||
                        token.equals("-verify") || token.equals("-noverify") ||
                        token.equals("-verifyremote") ||
                        token.equals("-verbosegc") ||
                        token.startsWith("-ms") || token.startsWith("-mx") ||
                        token.startsWith("-ss") || token.startsWith("-oss")) {

                    javaArgs = addArgument(javaArgs, token);
                } else if (token.equals("-tclassic")) {
                    usageError("Classic VM no longer supported.");
                    return;
                } else if (token.equals("-tclient")) {
                    // -client must be the first one
                    javaArgs = "-client " + javaArgs;
                } else if (token.equals("-tserver")) {
                    // -server must be the first one
                    javaArgs = "-server " + javaArgs;
                } else if (token.equals("-sourcepath")) {
                    if (i == (argv.length - 1)) {
                        usageError("No sourcepath specified.");
                        return;
                    }
                    Env.setSourcePath(argv[++i]);
                } else if (token.equals("-classpath")) {
                    if (i == (argv.length - 1)) {
                        usageError("No classpath specified.");
                        return;
                    }
                    javaArgs = addArgument(javaArgs, token);
                    javaArgs = addArgument(javaArgs, argv[++i]);
                } else if (token.equals("-attach")) {
                    if (connectSpec != null) {
                        usageError("cannot redefine existing connection", token);
                        return;
                    }
                    if (i == (argv.length - 1)) {
                        usageError("No attach address specified.");
                        return;
                    }
                    String address = argv[++i];

                    /*
                     * -attach is shorthand for FX-JDI implementation's attaching
                     * connectors. Use the shared memory attach if it's available;
                     * otherwise, use sockets. Build a connect specification string
                     * based on this decision.
                     */
                    if (supportsSharedMemory()) {
                        connectSpec = "com.sun.javafx.jdi.connect.FXSharedMemoryAttachingConnector:name=" +
                                address;
                    } else {
                        String suboptions = addressToSocketArgs(address);
                        connectSpec = "com.sun.javafx.jdi.connect.FXSocketAttachingConnector:" + suboptions;
                    }
                } else if (token.equals("-listen") || token.equals("-listenany")) {
                    if (connectSpec != null) {
                        usageError("cannot redefine existing connection", token);
                        return;
                    }
                    String address = null;
                    if (token.equals("-listen")) {
                        if (i == (argv.length - 1)) {
                            usageError("No attach address specified.");
                            return;
                        }
                        address = argv[++i];
                    }

                    /*
                     * -listen[any] is shorthand for one of the FX-JDI implementation's
                     * listening connectors. Use the shared memory listen if it's
                     * available; otherwise, use sockets. Build a connect
                     * specification string based on this decision.
                     */
                    if (supportsSharedMemory()) {
                        connectSpec = "com.sun.javafx.jdi.connect.FXSharedMemoryListeningConnector:";
                        if (address != null) {
                            connectSpec += ("name=" + address);
                        }
                    } else {
                        connectSpec = "com.sun.javafx.jdi.connect.FXSocketListeningConnector:";
                        if (address != null) {
                            connectSpec += addressToSocketArgs(address);
                        }
                    }
                } else if (token.equals("-launch")) {
                    launchImmediately = true;
                } else if (token.equals("-connect")) {
                    /*
                     * -connect allows the user to pick the connector
                     * used in bringing up the target VM. This allows
                     * use of connectors other than those in the reference
                     * implementation.
                     */
                    if (connectSpec != null) {
                        usageError("cannot redefine existing connection", token);
                        return;
                    }
                    if (i == (argv.length - 1)) {
                        usageError("No connect specification.");
                        return;
                    }
                    connectSpec = argv[++i];
                } else if (token.startsWith("-")) {
                    usageError("invalid option", token);
                    return;
                } else {
                    // Everything from here is part of the command line
                    cmdLine = addArgument("", token);
                    for (i++; i < argv.length; i++) {
                        cmdLine = addArgument(cmdLine, argv[i]);
                    }
                    break;
                }
            }
        }

        /*
         * Unless otherwise specified, set the default connect spec.
         */
        if (connectSpec == null) {
            connectSpec = "com.sun.javafx.jdi.connect.FXLaunchingConnector:";
        } else if (!connectSpec.endsWith(",") && !connectSpec.endsWith(":")) {
            connectSpec += ","; // (Bug ID 4285874)
        }

        cmdLine = cmdLine.trim();
        javaArgs = javaArgs.trim();

        if (cmdLine.length() > 0) {
            if (!connectSpec.startsWith("com.sun.javafx.jdi.connect.FXLaunchingConnector:") &&
                    !connectSpec.startsWith("com.sun.jdi.CommandLineLaunch:")) {
                usageError("Cannot specify command line with connector:",
                        connectSpec);
                return;
            }
            connectSpec += "main=" + cmdLine + ",";
        }

        if (javaArgs.length() > 0) {
            if (!connectSpec.startsWith("com.sun.javafx.jdi.connect.FXLaunchingConnector:") &&
                    !connectSpec.startsWith("com.sun.jdi.CommandLineLaunch:")) {
                usageError("Cannot specify target vm arguments with connector:",
                        connectSpec);
                return;
            }
            connectSpec += "options=" + javaArgs + ",";
        }

        try {
            if (!connectSpec.endsWith(",")) {
                connectSpec += ","; // (Bug ID 4285874)
            }
            Env.init(connectSpec, launchImmediately, traceFlags);
        } catch (Exception e) {
            MessageOutput.printException("Internal exception:", e);
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    // class implementing EventNotifier interface
    private class EventNotifierImpl implements EventNotifier {
        public boolean shouldRemoveListener() {
            return false;
        }

        public void breakpointEvent(BreakpointEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.breakpointEvent(evt);
                }
            }
        }

        public void classPrepareEvent(ClassPrepareEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.classPrepareEvent(evt);
                }
            }
        }

        public void classUnloadEvent(ClassUnloadEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.classUnloadEvent(evt);
                }
            }
        }

        public void exceptionEvent(ExceptionEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.exceptionEvent(evt);
                }
            }
        }

        public void fieldWatchEvent(WatchpointEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.fieldWatchEvent(evt);
                }
            }
        }

        public void methodEntryEvent(MethodEntryEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.methodEntryEvent(evt);
                }
            }
        }

        public boolean methodExitEvent(MethodExitEvent evt) {
            boolean result = false;
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    result |= en.methodExitEvent(evt);
                }
            }
            return result;
        }

        public void receivedEvent(Event evt) {
            synchronized (listeners) {
                ListIterator<EventNotifier> itr = listeners.listIterator();
                if (itr.hasNext()) {
                    EventNotifier en = itr.next();
                    if (en.shouldRemoveListener()) {
                        itr.remove();
                    } else {
                        en.receivedEvent(evt);
                        if (en.shouldRemoveListener()) {
                            itr.remove();
                        }
                    }
                }
            }
        }

        public void stepEvent(StepEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.stepEvent(evt);
                }
            }
        }

        public void threadDeathEvent(ThreadDeathEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.threadDeathEvent(evt);
                }
            }
        }

        public void threadStartEvent(ThreadStartEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.threadStartEvent(evt);
                }
            }
        }

        public void vmDeathEvent(VMDeathEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.vmDeathEvent(evt);
                }
            }
        }

        public void vmDisconnectEvent(VMDisconnectEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.vmDisconnectEvent(evt);
                }
            }
        }

        public void vmInterrupted() {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.vmInterrupted();
                }
            }
        }

        public void vmStartEvent(VMStartEvent evt) {
            synchronized (listeners) {
                for (EventNotifier en : listeners) {
                    en.vmStartEvent(evt);
                }
            }
        }
    }
}
