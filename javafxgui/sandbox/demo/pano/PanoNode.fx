/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package demo.pano;

import javafx.gui.*;
import java.lang.*;

public class PanoNode extends CustomNode {
    public attribute pano:Panorama = Panorama{};
    public attribute width:Integer = 800;
    public attribute height:Integer = 550;
    public attribute url:String on replace {
        System.out.println("url="+url);
        pano.baseUrl = url;
        zoomFit();
        updateView();
    };

    private attribute currentZoomLevel:ZoomLevel;
    private attribute fitScale:Number;
    private attribute scale:Number;
    private attribute xBeforeDrag:Number;
    private attribute yBeforeDrag:Number;
    private attribute imageViewGroup:Group = Group{};
    private attribute imageViewGroupOld:Group = Group{};

    init{
        onMouseDragged = function(e:MouseEvent) {
            var newX = xBeforeDrag + e.getDragX();
            var newY = yBeforeDrag + e.getDragY();
            var minX = -Math.max(0,(currentZoomLevel.width*imageViewGroup.scaleX)-width);
            var minY = -Math.max(0,(currentZoomLevel.height*imageViewGroup.scaleY)-height);
            imageViewGroup.translateX = Math.max(Math.min(0,newX),minX);
            imageViewGroup.translateY = Math.max(Math.min(0,newY),minY);
        };
        onMousePressed = function(e:MouseEvent) {
            xBeforeDrag = imageViewGroup.translateX;
            yBeforeDrag = imageViewGroup.translateY;
        };
        onMouseWheelMoved = function(e:MouseEvent) {
            System.out.println("getWheelRotation="+e.getWheelRotation());
            zoom(1-(0.01*e.getWheelRotation()),e.getX(),e.getY());
        };
    }

    private function updateView(){
        System.out.println("updateView currentZoomLevel={currentZoomLevel.index}");
        if (currentZoomLevel <> null){
            // layout image tiles
            delete imageViewGroup.content;
            var offsetX:Integer = 0;
            var offsetY:Integer = 0;
            for (col in [0..(currentZoomLevel.cols-1)]){
                offsetX = col * pano.tileSize;
                for (row in [0..(currentZoomLevel.rows-1)]){
                    offsetY = row * pano.tileSize;
    //                insert Rectangle{
    //                    x: offsetX
    //                    y: offsetY
    //                    width: pano.tileSize
    //                    height: pano.tileSize
    //                    stroke: Color.CRIMSON
    //                    fill: null
    //                } into imageViewGroup.content;
                    insert ImageView{
                        x: offsetX
                        y: offsetY
                        image: pano.getTileImage(currentZoomLevel,col,row);
                    } into imageViewGroup.content;
                }
            }
        }
    }

    private function calculateCurrentZoomLevel(){
        var newZoom:ZoomLevel;
        for (z in reverse pano.zoomLevels){
            if (z.scale < scale) {
                break;
            }
            newZoom = z;
        }
        currentZoomLevel = newZoom;
        System.out.println("calculateCurrentZoomLevel currentZoomLevel={currentZoomLevel.index}");
    }

    private function zoom(amount:Number, centerX:Number, centerY:Number){
        setScale(scale * amount);
    }

    private function setScale(newScale:Number){
        // check bounds
        if (newScale < fitScale) newScale = fitScale;
        if (newScale > 1) newScale = 1;
        // update
        if (newScale <> 0 and newScale <>  scale){
            scale = newScale;
            var oldZoomLevel = currentZoomLevel;
            calculateCurrentZoomLevel();
            // calculate combined scale and apply to imageViewGroup
            var combinedScale = scale/currentZoomLevel.scale;
            imageViewGroup.scaleX = combinedScale;
            imageViewGroup.scaleY = combinedScale;
            System.out.println("combinedScale={combinedScale}");
            // update tiles if zoom level changes
            if (oldZoomLevel <> currentZoomLevel){
                updateView();
            }
        }
    }

    public function zoomIn() {
        zoom(1.2,width/2,height/2);
        System.out.println("zoomIn scale={scale}");
    }

    public function zoomOut() {
        zoom(0.8,width/2,height/2);
        System.out.println("zoomOut scale={scale}");
    }

    public function zoomFit() {
        fitScale = Math.min(
            (width as Number)/ pano.width,
            (height as Number) / pano.height
        );
        setScale(fitScale);
        imageViewGroup.translateX = 0;
        imageViewGroup.translateY = 0;
        System.out.println("zoomFit fitScale={scale}");
    }

    protected function create():Node {
//        Rectangle {
//            width: bind width
//            height: bind height
//            fill: Color.CRIMSON
//        },
        imageViewGroup
    }
}

public class Panorama{
    public attribute width:Integer = 0;
    public attribute height:Integer = 0;
    public attribute tileSize:Integer = 0;
    public attribute zoomLevels:ZoomLevel[];
    public attribute baseUrl:String on replace {
        if (baseUrl <> null){
            // load xml
            // eg. <IMAGE_PROPERTIES WIDTH="9601" HEIGHT="2578" NUMTILES="578" NUMIMAGES="1" VERSION="1.8" TILESIZE="256" />
            var doc:javafx.xml.Document = javafx.xml.DocumentBuilder{}.parseURI(baseUrl+"/ImageProperties.xml");
            width = java.lang.Integer.parseInt(doc.documentElement.getAttribute("WIDTH"));
            height = java.lang.Integer.parseInt(doc.documentElement.getAttribute("HEIGHT"));
            tileSize = java.lang.Integer.parseInt(doc.documentElement.getAttribute("TILESIZE"));
            System.out.println("width={width} height={height} tileSize={tileSize}");
            // calculate zoom levels
            delete zoomLevels;
            var currentMaxDim:Integer = Math.max(width,height);
            var currentWidth:Integer = width;
            var currentHeight:Integer = height;
            // add original size to zoom levels
            insert ZoomLevel{
                width: currentWidth
                height: currentHeight
                scale: currentWidth/width
                cols: Math.ceil((currentWidth as Number) / tileSize) as Integer
                rows: Math.ceil((currentHeight as Number) / tileSize) as Integer
            } into zoomLevels;
            // add other zoom levels (50% zoom each time)
            while (currentMaxDim > tileSize){
                currentWidth = Math.ceil((currentWidth as Number) / 2.0) as Integer;
                currentHeight = Math.ceil((currentHeight as Number) / 2.0) as Integer;
                currentMaxDim = Math.max(currentWidth,currentHeight);
                insert ZoomLevel{
                    width: currentWidth
                    height: currentHeight
                    scale: (currentWidth as Number)/width
                    cols: Math.ceil((currentWidth as Number) / tileSize) as Integer
                    rows: Math.ceil((currentHeight as Number) / tileSize) as Integer
                } into zoomLevels;

            }
            // reverse zoom levels
            zoomLevels = reverse zoomLevels;
            // calculate index & tileCountOffsets (needed to calc tile directory names)
            var tileCountOffset:Integer = 0;
            var index:Integer = 0;
            for (z in zoomLevels){
                z.index = index;
                index ++;
                z.tileCountOffset = tileCountOffset;
                tileCountOffset += z.tileCount;
            }
            // print zoom levels
            for (z in zoomLevels){
                System.out.println("ZoomLevel[{z.index}] {z.width}x{z.height} scale={z.scale} tiles={z.cols}x{z.rows} tileCount={z.tileCount} tileCountOffset={z.tileCountOffset}");
            }
        }
    };

    public function getTileImage(zoomLevel:ZoomLevel, col:Integer, row:Integer):Image{
        var imageIndex:Integer = zoomLevel.tileCountOffset + (row*zoomLevel.cols) + col;
        var directoryNum:Integer = imageIndex/256;
        Image {
            backgroundLoading: true
            url: "{baseUrl}/TileGroup{directoryNum}/{zoomLevel.index}-{col}-{row}.jpg"
        }
    }
}

public class ZoomLevel{
    public attribute index:Integer;
    public attribute width:Integer;
    public attribute height:Integer;
    public attribute cols:Integer;
    public attribute rows:Integer;
    public attribute scale:Number;
    public attribute tileCount:Integer = bind cols * rows;
    public attribute tileCountOffset:Integer;
}
