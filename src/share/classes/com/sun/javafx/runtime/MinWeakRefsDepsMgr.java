/*
 * Copyright 2009, 2010 Sun Microsystems, Inc.  All Rights Reserved.
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
package com.sun.javafx.runtime;

import com.sun.javafx.runtime.refq.RefQ;
import com.sun.javafx.runtime.refq.WeakRef;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;



/**
 * A DepenendentsManager implementation that minimizes number of WeakReference
 * objects created.
 *
 * @author Per Bothner
 * @author A. Sundararajan (integrated with DependentsManager interface)
 */
class MinWeakRefsDepsMgr extends DependentsManager {
    private WeakBinderRef thisRef;
    DepChain dependencies;

    WeakBinderRef getThisRef(FXObject self) {
       if (thisRef == null) {
           thisRef = new WeakBinderRef(self);
       }
       return thisRef;
    }

    public void addDependent(FXObject bindee, final int varNum, FXObject binder, final int depNum) {
        Dep dep = Dep.newDependency(binder, depNum);
        dep.linkToBindee(bindee, varNum);
        // FIXME: revisit this - is this a good time to call cleanup?
        WeakBinderRef.checkForCleanups();
    }

    public void removeDependent(FXObject bindee, final int varNum, FXObject binder) {
        DepChain chain = DepChain.find(varNum, dependencies);
        if (chain == null)
            return;
        Dep prev = null;
        MinWeakRefsDepsMgr binderManager = (MinWeakRefsDepsMgr) DependentsManager.get(binder);
        WeakBinderRef binderRef = binderManager.thisRef;
        for (Dep dep = binderRef.bindees; dep != null; ) {
            Dep next = dep.nextInBindees;
            if (dep.chain == chain) {
                if (prev == null)
                    binderRef.bindees = next;
                else
                    prev.nextInBindees = next;
                dep.unlinkFromBindee();
                return;
            }
            prev = dep;
            dep = next;
        }
    }

    public void notifyDependents(FXObject bindee, int varNum, int startPos, int endPos, int newLength, int phase) {
        // TODO - handle phase.
        DepChain chain = DepChain.find(varNum, dependencies);
        if (chain == null)
            return;
        for (Dep dep = chain.dependencies; dep != null;) {
            Dep next = dep.nextInBinders;
            WeakBinderRef binderRef = dep.binderRef;
            if (binderRef != null) {
                FXObject binder = binderRef.get();
                if (binder == null) {
                    binderRef.cleanup();
                } else {
                    boolean handled = true;
                    try {
                        handled = binder.update$(bindee, dep.depNum, startPos, endPos, newLength, phase);
                    } catch (RuntimeException re) {
                        ErrorHandler.bindException(re);
                    }
                    if (!handled) {
                        binderRef.cleanup();
                    }
                }
            }
            dep = next;
        }
    }

    public int getListenerCount(FXObject bindee) {
        return getListenerCount(dependencies);
    }

    private int getListenerCount(DepChain chain) {
        if (chain == null)
            return 0;
        int count = 0;
        for (Dep dep = chain.dependencies; dep != null;) {
            WeakBinderRef binderRef = dep.binderRef;
            if (binderRef != null) {
                if (binderRef.get() != null) {
                    count++;
                }
            }
            dep = dep.nextInBinders;
        }
        return count + getListenerCount(chain.child0) + getListenerCount(chain.child1);
    }

    public List<FXObject> getDependents(FXObject bindee) {
        List<FXObject> res = new ArrayList<FXObject>();
        getDependents(dependencies, res);
        return res;
    }

    void getDependents(DepChain chain, List<FXObject> res) {
        if (chain == null)
            return;
        for (Dep dep = chain.dependencies; dep != null;) {
            WeakBinderRef binderRef = dep.binderRef;
            if (binderRef != null) {
                if (binderRef.get() != null) {
                    res.add(binderRef.get());
                }
            }
            dep = dep.nextInBinders;
        }
        getDependents(chain.child0, res);
        getDependents(chain.child1, res);
    }

}
interface BinderLinkable {
    void setNextBinder(Dep next);
};

class WeakBinderRef extends WeakRef<FXObject> {
    private static RefQ<FXObject> refQ = new RefQ<FXObject>();
    /** Chain of Dep instances whose binderRef point back here. */
    Dep bindees;
    /** Increment this to disable checkForCleanups.
     * (I don't know if/when that is needed ...) */
    static volatile int unsafeToCleanup;

    static void checkForCleanups() {
        if (unsafeToCleanup > 0) {
            return;
        }
        Reference<? extends FXObject> ref;
        while ((ref = refQ.poll()) != null) {
            if (ref instanceof WeakBinderRef) {
                ((WeakBinderRef) ref).cleanup();
            }
        }
    }

    WeakBinderRef(FXObject obj) {
        super(obj, refQ);
    }

    void cleanup() {
        for (Dep dep = bindees; dep != null;) {
            Dep next = dep.nextInBindees;
            dep.unlinkFromBindee();
            dep = next;
        }
        bindees = null;
    }
}

/** Each DepChain is a linked list of Deps for a given bindeeVarNum.
 * In addition, a DepCHain may have two child DepChains that are
 * organized as a binary trie.
 */
class DepChain implements BinderLinkable {
    int bindeeVarNum;

    /** The lowest power of 2 such that {@code nextBit > bindeeVarNum}.
     * If {@code child0 != null}, then {@code child0.nextBit > this.nextBit}.
     * If {@code child1 != null}, then {@code child1.nextBit > this.nextBit}.
     */
    int nextBit;

    /** The chain of dependencies for the given bindeeVarNum. */
    Dep dependencies;

    /** Children that have the same varNum prefix string.
     * We view varNum as a bit-string in little-endian order.
     * For all p such that p is child0 or a descendant of child0,
     * we have that {@code (p.varNum & nextBit) == 0} and {@code p.varNum & (nextBit-1) == varNum}.
     * Likewise, for all p such that p is child1 or a descendant of child1,
     * we have that {@code (p.varNum & nextBit) != 0} and {@code p.varNum & (nextBit-1) == varNum}.
     */
    DepChain child0, child1;

    Object /*union<DepChain,MinWeakRefsDepsMgr>*/ parent;
    
    public void setNextBinder(Dep next) {
        dependencies = next;
    }

    /** Find the DepChain for the given varNum, or return null if not found. */
    public static DepChain find(int varNum, DepChain cur) {
        for (;;) {
            if (cur == null)
                return null;
            int curVarNum = cur.bindeeVarNum;
            if (varNum == curVarNum)
                return cur;
            int curBit = cur.nextBit;
            int mask = curBit-1;
            if ((varNum & mask) != (curVarNum & mask))
                return null;
            cur = (varNum & curBit) == 0 ? cur.child0 : cur.child1;
        }
    }

    /** Find the DepChain for the given varNum, or create it if not found. */
    public static DepChain findForce(int varNum, DepChain cur, Object parent) {
        // If selector==-1 then cur == ((MinWeakRefsDepsMgr) parent).dependencies.
        // If selector==0 then cur == ((DepChain) parent).child0.
        // If selector==1 then cur == ((DepChain) parent).child1.
        int selector = -1;
        for (;;) {
            if (cur == null)
                break;
            int curVarNum = cur.bindeeVarNum;
            if (varNum == curVarNum)
                return cur;
            int curBit = cur.nextBit;
            int mask = curBit-1;
            if ((varNum & mask) != curVarNum) {
                DepChain p = new DepChain();
                int nextBit = 1;
                while ((varNum & nextBit) == (curVarNum & nextBit) &&
                        nextBit <= varNum)
                    nextBit++;
                p.bindeeVarNum = varNum & (nextBit-1);
                p.nextBit = nextBit;
                replace(parent, selector, p);
                if ((curVarNum & nextBit) == 0) {
                    p.child0 = cur;
                    selector = 1;
                } else {
                    p.child1 = cur;
                    selector = 0;
                }
                p.parent = parent;
                cur.parent = p;
                if (p.bindeeVarNum == varNum)
                    return p;
                parent = p;
                break;
            }
            parent = cur;
            if ((varNum & curBit) == 0) {
                selector = 0;
                cur = cur.child0;
            }
            else {
                selector = 1;
                cur = cur.child1;
            }
        }
        cur = new DepChain();
        cur.bindeeVarNum = varNum;
        cur.parent = parent;
        int nextBit = 1;
        while (nextBit <= varNum)
            nextBit <<= 1;
        cur.nextBit = nextBit;
        DepChain old = replace(parent, selector, cur);
        if (old != null) {
             if ((old.bindeeVarNum & nextBit) == 0)
                 cur.child0 = old;
             else
                 cur.child1 = old;
        }
        return cur;
    }

    /** Replace parent.selector by dep, returning old value. */
    private static DepChain replace(Object parent, int selector, DepChain dep) {
        DepChain old;
        if (selector == -1) {
            MinWeakRefsDepsMgr pmgr = (MinWeakRefsDepsMgr) parent;
            old = pmgr.dependencies;
            pmgr.dependencies = dep;
        }
        else {
            DepChain pchain = (DepChain) parent;
            if (selector == 0) {
                old = pchain.child0;
                pchain.child0 = dep;
            } else {
                old = pchain.child1;
                pchain.child1 = dep;
            }
        }
        return old;
    }

    /** Replace this.parent by replacement. */
    void replaceParent(DepChain replacement) {
        if (parent instanceof MinWeakRefsDepsMgr)
            ((MinWeakRefsDepsMgr) parent).dependencies = replacement;
        else {
            DepChain pchain = (DepChain) parent;
            if (pchain.child0 == this)
                pchain.child0 = replacement;
            if (pchain.child1 == this)
                pchain.child1 = replacement;
        }
    }

    /* DEBUGGING:
    static int counter;
    int id = ++counter;
    public String toString() { return "DepChain#"+id+"-vn:"+bindeeVarNum; }
    public static String xtoString(DepChain d) {
        if (d==null) return "null";
        return "DepChain[#"+d.id+"-vn:"+d.bindeeVarNum+" nB:"+d.nextBit+" ch0: "+xtoString(d.child0)+" ch1: "+xtoString(d.child1)
                +" p:"+d.parent+" d:"+xstr(d.dependencies)+"]";
    }
    public static String xstr(Dep d) {
        if (d == null)
            return "";
        if (d.nextInBinders==null) return ""+d;
        return ""+d+" "+xstr(d.nextInBinders);
    }
    */
}

class Dep implements BinderLinkable {
    /* DEBUGGING
    static int counter;
    int id = ++counter;
    public String toString() { return "Dep#"+id; }
    */

    WeakBinderRef binderRef;
    Dep nextInBinders;

    public void setNextBinder(Dep next) {
        nextInBinders = next;
    }

    /** Back-pointer corresponding to nextInBinders.
     * Either the previous Dep such that
     * {@code ((Dep) prevInBinders).nextInBinders==this},
     * or (if this is the first dep) the DepChain list head such that
     * {@code ((DepChain) prevInBinders).dependencies==this}.
     */
    BinderLinkable prevInBinders;

    Dep nextInBindees;
    DepChain chain;
    
    int depNum;

    static Dep newDependency(FXObject binder, int depNum) {
        Dep dep = new Dep();
        dep.depNum = depNum;
        MinWeakRefsDepsMgr binderDepMgr =
                (MinWeakRefsDepsMgr) DependentsManager.get(binder);
        WeakBinderRef binderRef = binderDepMgr.getThisRef(binder);
        dep.binderRef = binderRef;
        // Link into bindee chain of binderRef
        Dep firstBindee = binderRef.bindees;
        dep.nextInBindees = firstBindee;
        binderRef.bindees = dep;
        return dep;
    }

    void linkToBindee(FXObject bindee, int bindeeVarNum) {
        MinWeakRefsDepsMgr depMgr =
                (MinWeakRefsDepsMgr) DependentsManager.get(bindee);
        DepChain chain = DepChain.findForce(bindeeVarNum, depMgr.dependencies, depMgr);
        // Link into binder chain of bindee
        Dep firstBinder = chain.dependencies;
        nextInBinders = firstBinder;
        if (firstBinder != null) {
            firstBinder.prevInBinders = this;
        }
        prevInBinders = chain;
        chain.dependencies = this;
        this.chain = chain;
    }

    /**
     * Unlink from dependency chain of bindee.
     */
    void unlinkFromBindee() {
        BinderLinkable prevBinder = prevInBinders;
        if (prevBinder == null)
            return;
        prevInBinders = null;
        binderRef = null;
        Dep next = nextInBinders;
        if (prevBinder instanceof DepChain) {
            DepChain chain = (DepChain) prevBinder;
            chain.dependencies = next;
            if (next == null) {
                if (chain.child0 == null)
                    chain.replaceParent(chain.child1);
                else if (chain.child1 == null)
                    chain.replaceParent(chain.child0);
            }
        }
        else
            prevBinder.setNextBinder(next);
        if (next != null) {
            next.prevInBinders = prevBinder;
        }
    }
}
