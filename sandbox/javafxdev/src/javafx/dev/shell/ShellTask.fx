/*
 * ShellTask.fx
 *
 * Created on Jun 26, 2008, 4:37:42 PM
 */

package javafx.dev.shell;

/**
 * @author Alexandr Scherbatiy, alexsch@dev.java.net
 */

public abstract class ShellTask {

    abstract function execute():Void; 

    init{
        execute();
    }
}
