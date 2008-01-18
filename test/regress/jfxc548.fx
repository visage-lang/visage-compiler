/*
 * @test
 * @run
 */

public class One { 
    init { 
        java.lang.System.out.println("init called on One"); 
    } 
    postinit { 
        java.lang.System.out.println("postinit called on One"); 
    } 
} 

public class Two extends One { 
    init { 
        java.lang.System.out.println("init called on Two"); 
    } 
    postinit { 
        java.lang.System.out.println("postinit called on Two"); 
    } 
} 

public class Three extends Two { 
    init { 
        java.lang.System.out.println("init called on Three"); 
    } 
    postinit { 
        java.lang.System.out.println("postinit called on Three"); 
    } 
} 

public class Four extends Three { 
    init { 
        java.lang.System.out.println("init called on Four"); 
    } 
    postinit { 
        java.lang.System.out.println("postinit called on Four"); 
    } 
} 

var x = Four{} 
