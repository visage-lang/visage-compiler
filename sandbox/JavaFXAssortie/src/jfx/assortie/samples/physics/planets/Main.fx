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

var solarSystem = PlanetarySystem{
    planets: [
    Planet{
        name: "Sun"
        mass : 2e30
        color: Color.YELLOW//DARKRED //darkred
        radius:   14000
        coordinate : [ 0 , 0 ]
        velocity : [ 0, 0 ]
    },Planet{
        name: "Mercury"
        mass : 3.3e23
        color: Color.BLUE//WHITE //white
        radius: 2400
        coordinate : [ -57e9 ,  0]
        velocity : [ 0, -47e3 ]
    },
    Planet{
        name: "Venus"
        mass : 4.8e24
        color: Color.PINK//YELLOW //yellow
        radius: 6000
        coordinate : [ 108e9 ,  0]
        velocity : [ 0, 35e3 ]
    },
    Planet{
        name: "Earth"
        mass : 6e24
        color: Color.GREEN//BLUE //blue
        radius: 6300
        coordinate : [ 0 , -150e9 ]
        velocity : [ 30e3, 0 ]
    },Planet{
        name: "Mars"
        mass : 6.4e23
        color: Color.RED //red
        radius: 3400
        coordinate : [ 0 , 228e9 ]
        velocity : [ -24e3, 0 ]
    }
    ]
};


//solarSystem.ticks = [1..2000] dur 1000 linear fps 1000 continue if true;

var w = 400;
var h = 250;



//new Thread( new Runnable(){  public function run(){ System.out.println("Thread Start"); }}).start();
(new Thread(solarSystem)).start();

Frame{
    width: w //bind w
    height: h //bind h
    title: "Planetary System"
    onClose: function(){ System.exit(0);}
    content: Canvas{
        content: Group{
           content: [
		//Rect{
                //width: w
                //height: h
                //fill: Color.BLACK //black
            //},
            PlanetarySystemShape{
                transform:  [Transform.translate(w/2, h/2)]
                scale: Scale{ coordinateScale:  100.0/150e9 radiusScale: 20.0/14000.0}
                planetarySystem: solarSystem
            }]
        }
    }
    background: Color.BLACK //black
    visible: true
}