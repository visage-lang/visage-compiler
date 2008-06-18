/*
 * JSONRPCCall.fx
 *
 * Created on Jun 18, 2008, 4:40:15 PM
 */

package javafx.json.rpc;

import javafx.async.*;
import javafx.json.*;

/**
 * Supports making a JSON-RPC call to a JSON Web Service.
 * For more information on JSON-RPC, see <a href="http://json-rpc.org/">json-rpc.org</a>.
 * <p>This is a one-time use object. The <code>url</code> and <code>request</code> 
 * attributes must be populated in the object literal. Upon successful completion
 * the response attribute will be set. On communication failure, the failureText 
 * attribute will be set.
 * 
 * @author jclarke
 */

public class JSONRPCCall {
    
    /*
     * Holds the url for the JSON web service. This attribute must be provided in the
     * object literal when creating a JSONRPCCall.
     */
    public attribute url:String;
    
    /**
     * Holds any failure text from invoking the call. This attribute is populated
     * if there is some communication failure talking to the web service.
     */
    public attribute failureText;
    
    /**
     * Holds the JSON-RPC Response, this attribute is populated based on the
     * the results of the JSON-RPC call. 
     */
    public attribute response: JSONRPCResponse;
    
    /**
     * Holds the JSON-RPC Request. This attribute must be provided in the
     * object literal when creating a JSONRPCCall.
     */
    public attribute request: JSONRPCRequest;
    
    init {
        if(request <> null) {
            var call:AsyncJsonCall;
            call = AsyncJsonCall{
                url: url
                method: "POST"
                contentType: "application/json"
                outboundContent: "{request}"
                onDone: function(success : Boolean) : Void {
                    if(success) {
                        response = JSONRPCResponse{ object: call.document };
                    }else {
                        failureText = call.failureText;
                    }
                }
            }  
         } 
    }
    
    
}