/**
 * Regression test JFXC-2136 : javafx.reflectFXVarMember.setValue fails for elided instance var
 *
 * @test
 * @run
 */

import javafx.reflect.*;
import java.util.*;

class jfxc2136 {
    var x : Integer = 11;
    var y : String = "why";
    public var xp : Integer = 22;
    public var yp : String = "oh";

    function show() {
	println( x );
	println( y );
	println( xp );
	println( yp );
    }  
}

    function getVarValue(object : Object, name : String) : FXValue {
        var objectValue : FXObjectValue = FXLocal.getContext().mirrorOf(object);
        var cls : FXClassType = objectValue.getType();
        var attrs = cls.getMembers( FXMemberFilter.acceptAttributes(name), false );
        var attr : FXVarMember  = attrs.get(0) as FXVarMember;
        return attr.getValue(objectValue);
    }


    function setVarValue(object : Object, name : String, value : Object) {
        var objectValue : FXObjectValue = FXLocal.getContext().mirrorOf(object);
        var cls : FXClassType = objectValue.getType();
        var attrs = cls.getMembers( FXMemberFilter.acceptAttributes(name), false);
        var attr = attrs.get(0) as FXVarMember;
        var mirrored : FXValue;
        if (value instanceof FXValue)
            mirrored = value as FXValue
        else
            mirrored = FXLocal.getContext().mirrorOf(value);
        attr.setValue(objectValue, mirrored);
    }

var a = jfxc2136{};
println( getVarValue(a, "x").getValueString() );
println( getVarValue(a, "y").getValueString() );
println( getVarValue(a, "xp").getValueString() );
println( getVarValue(a, "yp").getValueString() );

setVarValue(a, "x", 1001);
setVarValue(a, "y", "what");
setVarValue(a, "xp", 2002);
setVarValue(a, "yp", "ah");

println( getVarValue(a, "x").getValueString() );
println( getVarValue(a, "y").getValueString() );
println( getVarValue(a, "xp").getValueString() );
println( getVarValue(a, "yp").getValueString() );

a.show();

