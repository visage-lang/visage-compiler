import java.lang.System;

/*
 * @test
 * @run
 */

class Component { 
} 

class Container extends Component { 
    attribute content : Component[]; 
} 

class Label extends Component { 
} 

var labels : Label[] = [Label {}]; 
var container = Container { 
    content: labels 
} 
