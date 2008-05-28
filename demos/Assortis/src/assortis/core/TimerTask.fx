/*
* TimerTask.fx
*
* Created on May 8, 2008, 1:07:22 PM
*/

package assortis.core;

/**
* @author andromeda
*/

public class TimerTask {
    attribute name: String;
    attribute time: Number;
    attribute action: function();
    
    public function toString () {  name } 
}
