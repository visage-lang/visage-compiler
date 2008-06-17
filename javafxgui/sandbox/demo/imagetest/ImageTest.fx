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

package demo.imagetest;

import javafx.gui.*;
import javafx.gui.component.*;
import java.lang.System;

var imageURL = "{__DIR__}/sun_logo.png";

var image = Image{ size:450 url:imageURL };

/* Setting the image variable should cause the ImageView's private
 * awtImage attribute to be updated, which will update the corresponding
 * SGImage's image property.
 */

var resetImageSize250 = function():Void {
    image = Image{ size:250 url:imageURL };
};

var resetImageHeight100 = function():Void {
    image = Image{ height:100 url:imageURL };
};

var resetImageWidth450Height450 = function():Void {
    image = Image{ width:450 height:450 url:imageURL };
};

var placeholder = Image{ url:"{__DIR__}/placeholder.jpg" };
var bigImageURL = "http://www.ibiblio.org/wm/paint/auth/durer/large-turf.jpg";
var asyncLoad = function():Void {
    image = Image{ backgroundLoading:true url:bigImageURL };
}
var asyncLoadWithPlaceholder = function():Void {
    image = Image{ backgroundLoading:true placeholder:placeholder url:bigImageURL };
}

var cancel = function():Void {
    image.cancel();
};

var menuItems = [
    MenuItem{ text:"Reset Bound Image Size=250" action:resetImageSize250 },
    MenuItem{ text:"Reset Bound Image Height=100" action:resetImageHeight100 },
    MenuItem{ text:"Reset Bound Image Width=450,Height=450" action:resetImageWidth450Height450 },
    MenuItem{ text:"Async Load" action:asyncLoad },
    MenuItem{ text:"Async Load with placeholder" action:asyncLoadWithPlaceholder },
    MenuItem{ text:"Cancel" action:cancel },
    MenuItem{ text:"Exit" action:function(){System.exit(0)} }
];

var canvas = Canvas {
    content: [ImageView{ translateX:25 translateY:25 image: bind image }]
}

var frame = Frame {
    title: bind "Test Image, ImageView {image.width as Integer} {image.height as Integer}"
    width: 500
    height: 500
    background: Color.WHITE
    menus: [Menu{ text:"Actions" items:menuItems }]
    content: canvas
    visible: true
    attribute finished:Boolean = bind (image.progress == 100) on replace {
        java.lang.System.out.println("Finished {image.getBufferedImage()}");
    }
}

 
