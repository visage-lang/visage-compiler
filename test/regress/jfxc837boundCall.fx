/**
 * Regression test JFXC-837 : funcion/method call within bind
 * @test
 */
import java.lang.System; 
import java.util.regex.Pattern;

var enableBindingOverhaul;

var x = bind 1234 .toString();
System.out.println(x);
var rex = 'a*b';
var str = 'aaaab';
var match = bind Pattern.matches(rex, str);
System.out.println("{rex} : {str} -- {if (match) 'MATCH' else 'no'}");
str = 'aacb';
System.out.println("{rex} : {str} -- {if (match) 'MATCH' else 'no'}");
rex = 'a*cb*';
System.out.println("{rex} : {str} -- {if (match) 'MATCH' else 'no'}");
str = 'cbbbb';
System.out.println("{rex} : {str} -- {if (match) 'MATCH' else 'no'}");
str = 'zzz';
System.out.println("{rex} : {str} -- {if (match) 'MATCH' else 'no'}");
rex = 'z*';
System.out.println("{rex} : {str} -- {if (match) 'MATCH' else 'no'}");

