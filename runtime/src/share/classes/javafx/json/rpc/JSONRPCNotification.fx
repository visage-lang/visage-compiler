/*
 * JSONRpcNotification.fx
 *
 * Created on Jun 16, 2008, 8:58:36 PM
 */

package javafx.json.rpc;
import javafx.json.*;
import java.lang.Object;

/**
 * Represents a JSON-RPC Notification
 * @author jclarke
 */

public class JSONRPCNotification extends JSONRPCRequest {
    /*
     * Unique identifier for the request. 
     * For Notification this must be JSONNull
     */
    override attribute id on replace {
        if(not parsing) {
            var old = for(p in pairs where p.name == "id")p;
            for(p in old) { delete p from pairs; }
            var pair = Pair{name:"id" value: JSONNull{} };
            insert pair into pairs;
        }
    }; 
}