package com.sun.javafx.runtime.location;

import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
/*[*/
import java.lang.ref.ReferenceQueue;
/*]*/

import com.sun.javafx.runtime.util.Linkable;

/**
 * WeakLink
 *
 * @author Brian Goetz
 */
class WeakLocation extends WeakReference<Location> implements Linkable<WeakLocation, AbstractLocation> {
    /*[*/ static ReferenceQueue<Location> refQ = new ReferenceQueue<Location>(); /*]*/
    WeakLocation next;
    AbstractLocation host;

    WeakLocation(Location referent) {
        super(referent /*[*/ , refQ /*]*/ );
    }

    public WeakLocation getNext() {
        return next;
    }

    public void setNext(WeakLocation next) {
        this.next = next;
    }

    public AbstractLocation getHost() {
        return host;
    }

    public void setHost(AbstractLocation host) {
        this.host = host;
    }

    static void purgeDeadLocations(AbstractLocation fallback) {
        /*[*/
        Reference<? extends Location> loc;
        AbstractLocation lastHost = null;
        while ((loc = refQ.poll()) != null) {
            WeakLocation wl = (WeakLocation) loc;
            AbstractLocation host = wl.host;
            // Minor optimization -- if we just purged a given host, don't do it again
            if (host != lastHost)
                host.purgeDeadDependencies();
            lastHost = host;
        }
        /*]*/

        /* [
            // Fallback strategy is an aggressive purge
            if (fallback != null)
                fallback.purgeDeadDependencies();
        ] */
    }
}
