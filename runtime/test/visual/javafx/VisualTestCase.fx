/*
 * VisualTestCase.fx
 *
 * Created on Jul 17, 2008, 10:49:00 AM
 */

package javafx;

import javafx.fxunit.FXTestCase;
import javafx.scene.paint.*;
import javafx.lang.Duration;

/**
 * @author Richard
 */
public class VisualTestCase extends FXTestCase {
    private attribute robot = new java.awt.Robot();
    
    attribute autoWaitForIdle:Boolean on replace {
        robot.setAutoWaitForIdle(autoWaitForIdle);
    }
    
    attribute autoDelay:Integer on replace {
        robot.setAutoDelay(autoDelay);
    }
    
    init {
        robot.setAutoWaitForIdle(autoWaitForIdle);
        robot.setAutoDelay(autoDelay);
    }
    
    public function setUp() : Void  {
        autoWaitForIdle = false;
        autoDelay = 0;
        robot = new java.awt.Robot();
    }
    
    public function moveMouse(x:Number, y:Number) : Void {
        robot.mouseMove(x as Integer, y as Integer);
    }
    
    public function leftPress() : Void {
        
    }
    
    public function rightPress() : Void {
        
    }
    
    public function leftRelease() : Void {
        
    }
    
    public function rightRelease() : Void {
        
    }
    
    public function leftClick() : Void {
        
    }
    
    public function rightClick() : Void {
        
    }
    
    public function leftDoubleClick() : Void {
        
    }
    
    public function rightDoubleClick() : Void {
        
    }
    
    public function pressKey(key:Integer) : Void {
        
    }
    
    public function releaseKey(key:Integer) : Void {
        
    }
    
    public function getPixel(x:Number, y:Number) : Color {
        return Color.fromAWTColor(robot.getPixelColor(x as Integer, y as Integer));
    }
    
    public function delay(dur:Duration) : Void {
        robot.delay(dur.toMillis());
    }
    
    public function waitForIdle() : Void {
        robot.waitForIdle();
    }
}
