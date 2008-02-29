
package jfx.assortie.samples.physics.planets;


import javafx.ui.*;

public class Planet {
    attribute name: String;
    
    attribute mass : Number;
    attribute radius: Number;
    
    attribute color: Color;
    
    attribute coordinate: Number[] = [0.0,0.0];
    attribute velocity : Number[] = [0.0,0.0];
    attribute acceleration : Number[] = [0.0,0.0];
}

