/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
    
