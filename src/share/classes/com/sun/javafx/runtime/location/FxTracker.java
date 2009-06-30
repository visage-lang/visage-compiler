/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.FXObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;

/**
 * @author ksrini
 */
public class FxTracker {
    public static boolean tracker = false;
    static final long interval = Long.getLong("FxTracker.interval", 60*1000L);
    static final String CSEP = System.getProperty("FxTracker.csv_separator", ":");

    static final Map<AbstractLocation, Boolean> loc_map =
            Collections.synchronizedMap(new WeakHashMap<AbstractLocation, Boolean>());

    static final Map<FXObject, Boolean> fxo_map =
            Collections.synchronizedMap(new WeakHashMap<FXObject, Boolean>());

    private static Vector<HistoData> hData = new Vector<HistoData>();

    static Timer timer;

    private FxTracker(){}

    private static void toCsv(String filename) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            PrintStream ps = new PrintStream(fos);
            ps.print("INTERVAL" + CSEP);
            ps.print("BINDING_EXPRESSION" + CSEP);
            ps.print("CHANGE_LISTENER" + CSEP);
            ps.print("TRIGGER" + CSEP);
            ps.print("VIEW_LOCATION" + CSEP);
            ps.print("WEAK_LOCATION" + CSEP);
            ps.print("WEAK_ME_HOLDER" + CSEP);
            ps.print("Location MapSize" + CSEP);
            ps.print("Iterator Count: ");
            ps.print("Listener Count" + CSEP);
            ps.print("Location Change");
            ps.print("FXBase Mapsize" + CSEP);
            ps.print("FXBase Change");
            ps.println();

            int i = 0;
            for (HistoData h : hData) {
                ps.println(i + CSEP + h.toString());
                i += interval / 1000;
            }
            ps.println("Location classes Avg.");
            ps.print(getClassTable(true));
            ps.println("FXBase classes Avg.");
            ps.print(getClassTable(false));
        } catch (IOException ignore) {
        } finally {
            try {
                fos.close();
            } catch (IOException ignore) {
            }
        }
    }

    private static String getClassTable(boolean locations) {
        HashMap<String, Long> tmpMap = new HashMap<String, Long>();
        for (HistoData h : hData) {
            Set<Map.Entry<String, Long>> kset = (locations)
                    ? h.loc_class_type_table.entrySet()
                    : h.fxo_class_type_table.entrySet();
            for (Map.Entry<String, Long> x : kset) {
                Long nvalue = tmpMap.get(x.getKey());
                if (nvalue == null) {
                    nvalue = x.getValue();
                } else {
                    nvalue += x.getValue();
                }
                tmpMap.put(x.getKey(), nvalue);
            }
        }
        Set<Map.Entry<String, Long>> kset = tmpMap.entrySet();
        TreeSet<Map.Entry<String, Long>> sortedSet =
                new TreeSet<Map.Entry<String, Long>>(new Comparator<Map.Entry<String, Long>>() {
                public int compare(Map.Entry<String, Long> m1, Map.Entry<String, Long> m2) {
                    return (int) (m2.getValue() - m1.getValue());
                }                   
        });
        sortedSet.addAll(kset);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> x : sortedSet) {
            long avg = x.getValue() / hData.size();
            if (avg > 0) {
                sb.append(x.getKey() + ":" + avg);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static synchronized void engage() {
	if (tracker == true) return;
        if (timer == null) {            
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    collect();
                }
            }, 0L, interval);
            // while we are here we add the shutdown hook as well
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    timer.cancel();
                    timer.purge();
                    toCsv("profile.csv");
                }
            });
        }
    }

    public static void track(Object obj) {
        if (tracker == false)  {
	   engage();
           tracker = true; 
        }
        if (obj instanceof AbstractLocation) {
            loc_map.put((AbstractLocation)obj, tracker);
        } else if (obj instanceof FXObject) {
            fxo_map.put((FXObject)obj, tracker);
        }
    }

    public static void collect() {
        System.err.print("Collection in progress ...");
        HistoData hd = null;
        if (hData.isEmpty()) {
            hd = new HistoData();
        } else {
            HistoData last = hData.lastElement();
            hd = new HistoData(last.loc_mapsize, last.fxo_mapsize);
        }
        System.gc();
        synchronized (loc_map) {
            hd.gatherCounts();
            hData.add(hd);
        }
        System.err.println("done.");
    }
    
    /*
     * a set of convenience getters called from btrace script
     */
    public static long getBindingExpressionCount() {
        return (hData.size() > 0) ? hData.lastElement().binding_expression : 0; 
    }
    public static long getChangeListenerCount() {
        return (hData.size() > 0) ? hData.lastElement().binding_expression : 0;
    }    
    public static long getTriggerCount() {
        return (hData.size() > 0) ? hData.lastElement().trigger : 0;
    }
    public static long getViewLocationCount() {
        return (hData.size() > 0) ? hData.lastElement().view_location : 0;
    }
    public static long getWeakLocationCount() {
        return (hData.size() > 0) ? hData.lastElement().weak_location : 0;
    }
    public static long getWeakMeHolderCount() {
        return (hData.size() > 0) ? hData.lastElement().weak_me_holder : 0;
    }   
    public static long getTotalChildrenCount() {
        return (hData.size() > 0) ? hData.lastElement().total_children : 0;
    }
    public static long getLocationMapSize() {
        return (hData.size() > 0) ? hData.lastElement().loc_mapsize : 0;
    }
    public static long getIteratorCount() {
       return (hData.size() > 0) ? hData.lastElement().iterator_count : 0;
    }
    public static long getListenerCount() {
       return (hData.size() > 0) ? hData.lastElement().listener_count : 0;
    }
    public static long getFxObjectCount() {
       return (hData.size() > 0) ? hData.lastElement().fxo_mapsize : 0;
    }
}

class HistoData {
    // Location statistics
    long  binding_expression     = 0;
    long  change_listener        = 0;
    long  trigger                = 0;
    long  view_location          = 0;
    long  weak_location          = 0;
    long  weak_me_holder         = 0;
    long  total_children         = 0;
    long  loc_mapsize            = 0;
    long  iterator_count         = 0;
    long  listener_count         = 0;
    long  loc_prev_size          = 0;

    // FXObject statistics
    long fxo_mapsize            = 0;
    long fxo_prev_size          = 0;

    final Map<String, Long> loc_class_type_table =
            new HashMap<String, Long>();

    final Map<String, Long> fxo_class_type_table =
            new HashMap<String, Long>();


    public HistoData() {}
    HistoData(long loc_prev_size, long fxo_prev_size) {
        this.loc_prev_size = loc_prev_size;
        this.fxo_prev_size = fxo_prev_size;
    }

    static void keepClassTally(Map<String, Long> m, String key) {
        Long value = m.get(key);
        m.put(key, value == null ? 1 : ++value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(binding_expression);
        sb.append(FxTracker.CSEP);
        sb.append(change_listener);
        sb.append(FxTracker.CSEP);
        sb.append(trigger);
        sb.append(FxTracker.CSEP);
        sb.append(view_location);
        sb.append(FxTracker.CSEP);
        sb.append(weak_location);
        sb.append(FxTracker.CSEP);
        sb.append(weak_me_holder);
        sb.append(FxTracker.CSEP);
        sb.append(total_children);
        sb.append(FxTracker.CSEP);
        sb.append(loc_mapsize);
        sb.append(FxTracker.CSEP);
        sb.append(iterator_count);
        sb.append(FxTracker.CSEP);
        sb.append(listener_count);
        sb.append(FxTracker.CSEP);
        sb.append(loc_mapsize - loc_prev_size);
        sb.append(FxTracker.CSEP);
        sb.append(fxo_mapsize);
        sb.append(FxTracker.CSEP);
        sb.append(fxo_mapsize - fxo_prev_size);
        return sb.toString();
    }
 
    public void gatherCounts() {
        // do the locations work
        loc_mapsize = FxTracker.loc_map.size();

        for (AbstractLocation loc : FxTracker.loc_map.keySet()) {
            keepClassTally(loc_class_type_table, loc.getClass().getName());
            binding_expression +=
                    loc.countChildren(AbstractLocation.CHILD_KIND_BINDING_EXPRESSION);
            change_listener +=
                    loc.countChildren(AbstractLocation.CHILD_KIND_CHANGE_LISTENER);
            trigger +=
                    loc.countChildren(AbstractLocation.CHILD_KIND_TRIGGER);
            view_location +=
                    loc.countChildren(AbstractLocation.CHILD_KIND_VIEW_LOCATION);
            weak_location +=
                    loc.countChildren(AbstractLocation.CHILD_KIND_WEAK_LOCATION);
            weak_me_holder +=
                    loc.countChildren(AbstractLocation.CHILD_KIND_WEAK_ME_HOLDER);
            total_children += binding_expression + change_listener + trigger +
                    view_location + weak_location + weak_me_holder;
            iterator_count +=
                    AbstractLocation.iterationData.get(loc) != null ? 1 : 0;
            listener_count += loc.getListenerCount();
        }

        // now we do the fxobjects
        fxo_mapsize = FxTracker.fxo_map.size();
        for (FXObject fxo : FxTracker.fxo_map.keySet()) {
            keepClassTally(fxo_class_type_table, fxo.getClass().getName());
        }
    }
}
