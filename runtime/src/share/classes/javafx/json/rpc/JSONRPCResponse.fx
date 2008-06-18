/*
 * JSONRPCResponse.fx
 *
 * Created on Jun 16, 2008, 8:56:22 PM
 */

package javafx.json.rpc;
import javafx.json.*;
import java.lang.Object;

/**
 * Represents a JSON-RPC Response
 *
 * @author jclarke
 */

public class JSONRPCResponse extends JSONObject {

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
                result = getValue("result");
                error = getValue("error");
                id = getValue("id");
            }finally {
                parsing = false;
            }
        }
    }

    /**
     * The Object that was returned by the invoked method. 
     * This must be null in case there was an error invoking the method.
     */
    public attribute result:Object on replace {
        if(not parsing) {
            var old = for(p in pairs where p.name == "result")p;
            for(p in old) { delete p from pairs; }           
            insert Pair{name:"result" value: if(result <> null) then result else JSONNull{} } into pairs;
        }
    };

    /**
     * An Error object if there was an error invoking the method. 
     * It must be null if there was no error.
     */
    public attribute error:Object on replace {
        if(not parsing) {        
            var old = for(p in pairs where p.name == "error")p;
            for(p in old) { delete p from pairs; }    
            var pair = Pair{name:"error" value: if(error <> null) then error else JSONNull{} };
            insert pair into pairs;;
        }
    };
    /*
     * Unique identifier for the request. 
     * This must be the same id as the request it is responding to.
     */
    public attribute id: Object on replace {
        if(not parsing) {
            var old = for(p in pairs where p.name == "id")p;
            for(p in old) { delete p from pairs; }    
            var pair = Pair{name:"id" value: if(id <> null) then id else JSONNull{} };
            insert pair into pairs;
        }
    }; 

    override attribute handler = function(type:ElementType, object:Object):Void {
        if(type == ElementType.PAIR) {
            var pair = object as Pair;
            parsing = true;
            try {
                if(pair.name == "result") {
                    result = pair.value;
                }else if(pair.name == "error") {
                    error = pair.value;
                }else if(pair.name == "id") {
                    id = pair.value;
                }
            }finally {
                parsing = false;
            }

        }
    }

}