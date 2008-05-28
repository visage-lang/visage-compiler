package assortis.sources.samples.physics.planets;

import javafx.gui.*;

import java.lang.Math;
                                                                                                                                              
public class PlanetarySystemShape extends CustomNode{
    attribute planetarySystem: PlanetarySystem;
    attribute scale: Scale;

function create():Node{
    return Group{
        content: [
         Group{
            content: for(i in  [1..15])
            Circle{
                centerX: Math.random() * 300 - 150
                centerY: Math.random() * 300 - 150
                radius: 2
                fill: Color.WHITE
            }
         }, 
          Group{
            content: bind for(planet in planetarySystem.planets)
            Group{     
                content: [
                Circle{
                    centerX: bind planet.coordinate[0] * scale.coordinateScale
                    centerY: bind planet.coordinate[1] * scale.coordinateScale
                    radius: planet.radius * scale.radiusScale
                    fill: planet.color
                },
                Text{
                    content: planet.name
                    font: Font.font("SERIF", FontStyle.BOLD_ITALIC, 14 )
                    x: bind planet.coordinate[0] * scale.coordinateScale + 10
                    y: bind planet.coordinate[1] * scale.coordinateScale - 10
                    strokeWidth: 3
                    fill: Color.WHITE
                }
                ]
            }
        }]
        
    };
}

}

