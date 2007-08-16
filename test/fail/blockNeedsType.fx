// Does not compile -- NPE in code.Types during Attr -- Issue 23
import java.lang.System;
import java.lang.Exception;

var bool = true;
if bool then {
    System.out.println("Oh, joy!");
} else {
    throw new Exception("Oops...");
};
