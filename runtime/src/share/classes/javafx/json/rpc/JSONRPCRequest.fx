/*
 * JSONRPCRequest.fx
 *
 * Created on Jun 16, 2008, 8:52:30 PM
 */

package javafx.json.rpc;
import javafx.json.*;
import java.lang.Object;

/**
 * Represents a JSON-RPC Request
 *
 * @author jclarke
 */

public class JSONRPCRequest extends JSONObject {
    /**
     * indicates if updating is from parsing or not
     */
    protected attribute parsing:Boolean;

    /**
     * Convenience attribute for setting this JSONRPCResponse from a plain JSONObject
     */
    public attribute object:JSONObject on replace {
        if(object <> null) {
            parsing = true;
            this.pairs = object.pairs;
            try {
                method = getValue("method") as String;
                params = getValue("params") as JSONArray;
                id = getValue("id");
            }finally {
                parsing = false;
            }
        }
    }
    /*
     * the name of the method to invoke on the server
     */
    public attribute method:String on replace {
        if(not parsing) {
            var old = for(p in pairs where p.name == "method")p;
            for(p in old) { delete p from pairs; }
            insert Pair{name:"method" value: method} into pairs;
        }
    };

    /*
     * An JSONArray  to pass as arguments to the method.
     */
    public attribute params:JSONArray on replace {
        if(not parsing) {
            var old = for(p in pairs where p.name == "params")p;
            for(p in old) { delete p from pairs; }    
            var pair = Pair{name:"params" value:  if(params <> null) then params else JSONNull{}};
            insert pair into pairs;
        }
    };

    /*
     * Unique identifier for the request. 
     * It is used to match the response with the request that it is replying to.
     */
    public attribute id: java.lang.Object on replace {
        if(not parsing) {
            var old = for(p in pairs where p.name == "id")p;
            for(p in old) { delete p from pairs; }           
            insert Pair{name:"id" value: id} into pairs;
        }
    }; 

    override attribute handler = function(type:ElementType, object:Object):Void {
        if(type == ElementType.PAIR) {
            var pair = object as Pair;
            parsing = true;
            try {
                if(pair.name == "method") {
                    method = pair.value as String;
                }else if(pair.name == "params") {
                    params = pair.value as JSONArray;
                }else if(pair.name == "id") {
                    id = pair.value;
                }
            }finally {
                parsing = false;
            }

        }
    }
}