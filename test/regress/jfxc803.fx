/*
* @test
*/
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

var coord = XY {
     x: 50
     y: 100
};

var li = ActionListener {
      public function actionPerformed(e: ActionEvent): Void {
          System.out.println(coord.y);
          return;
      }
};


var timer = new Timer(100, null);
timer.start(); 
