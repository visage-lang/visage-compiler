/* M4.fx
 * Demonstrates direct calls to Java Swing classes -- creates a frame with a text field
 * No interaction
 *
 * @test
 */

import javax.swing.*;
import java.lang.*;

var frame = new JFrame();
var text = new JTextField("I am a text field");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.getContentPane().add(text);
frame.pack();
frame.setVisible(true);
