/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.ui;

import java.awt.GridBagConstraints;


public class Anchor {
    public attribute name: String;
    public attribute id:Number;

    public static attribute CENTER           = Anchor{
        name:"CENTER" id:GridBagConstraints.CENTER};
    public static attribute NORTH            = Anchor{
        name:"NORTH"  id:GridBagConstraints.NORTH};
    public static attribute SOUTH            = Anchor{
        name:"SOUTH"  id:GridBagConstraints.SOUTH};
    public static attribute EAST             = Anchor{
        name:"EAST"   id:GridBagConstraints.EAST};
    public static attribute WEST             = Anchor{
        name:"WEST"   id:GridBagConstraints.WEST};
    public static attribute NORTHEAST        = Anchor{
        name:"NORTHEAST" id:GridBagConstraints.NORTHEAST};
    public static attribute NORTHWEST        = Anchor{
        name:"NORTHWEST" id:GridBagConstraints.NORTHWEST};
    public static attribute SOUTHEAST        = Anchor{
        name:"SOUTHEAST" id:GridBagConstraints.SOUTHEAST};
    public static attribute SOUTHWEST        = Anchor{
        name:"SOUTHWEST" id:GridBagConstraints.SOUTHWEST};
    public static attribute PAGE_START       = Anchor{
        name:"PAGE_START" id:GridBagConstraints.PAGE_START};
    public static attribute PAGE_END         = Anchor{
        name:"PAGE_END" id:GridBagConstraints.PAGE_END};
    public static attribute LINE_START       = Anchor{
        name:"LINE_START" id:GridBagConstraints.LINE_START};
    public static attribute FIRST_LINE_START = Anchor{
        name:"FIRST_LINE_START" id:GridBagConstraints.FIRST_LINE_START};
    public static attribute FIRST_LINE_END   = Anchor{
        name:"FIRST_LINE_END" id:GridBagConstraints.FIRST_LINE_END};
    public static attribute LAST_LINE_START  = Anchor{
        name:"LAST_LINE_START" id:GridBagConstraints.LAST_LINE_START};
    public static attribute LAST_LINE_END    = Anchor{
        name:"LAST_LINE_END" id:GridBagConstraints.LAST_LINE_END};
}        
    
