/*
 * Main.fx
 *
 * Created on Jan 23, 2008, 2:24:19 PM
 */

package jfx.assortie.samples.physics.planets;

import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.animation.*;

import java.lang.Math;
import java.lang.Thread;
import java.lang.System;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


var solarSystem:PlanetarySystem = PlanetarySystem{
    planets: [
    Planet{
        name: "Sun"
        mass : 2e30
        color: Color.YELLOW
        radius:   14000
        coordinate : [ 0 , 0 ]
        velocity : [ 0, 0 ]
    },Planet{
        name: "Mercury"
        mass : 3.3e23
        color: Color.BLUE
        radius: 2400
        coordinate : [ -57e9 ,  0]
        velocity : [ 0, -47e3 ]
    },
    Planet{
        name: "Venus"
        mass : 4.8e24
        color: Color.PINK
        radius: 6000
        coordinate : [ 108e9 ,  0]
        velocity : [ 0, 35e3 ]
    },
    Planet{
        name: "Earth"
        mass : 6e24
        color: Color.GREEN
        radius: 6300
        coordinate : [ 0 , -150e9 ]
        velocity : [ 30e3, 0 ]
    },Planet{
        name: "Mars"
        mass : 6.4e23
        color: Color.RED 
        radius: 3400
        coordinate : [ 0 , 228e9 ]
        velocity : [ -24e3, 0 ]
    }
    ]
};


var w = 400.0;
var h = 250.0;

solarSystem.start();


Frame{
    width: w //bind w with inverse
    height: h //bind h with inverse
    title: "Planetary System"
    onClose: function(){ System.exit(0);}
    content: Canvas{
        content: Group{
           content: [
            PlanetarySystemShape{
                transform: bind [Transform.translate(w/2, h/2)]
                scale: Scale{ coordinateScale:  100.0/150e9 radiusScale: 20.0/14000.0}
                planetarySystem: solarSystem
            }]
        }
    }
    background: Color.BLACK 
    visible: true
}