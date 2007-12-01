/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.filter;
import javafx.ui.*;
import com.sun.javafx.api.ui.OpacityMaskFilter;
import javafx.ui.Paint;
import java.awt.image.BufferedImageOp;

public class OpacityMask extends Filter {
    private attribute awtFill: java.awt.Paint= bind fill.getPaint() on replace {
        opacityMaskFilter.setOpacityMask(awtFill);
    };
    private attribute opacityMaskFilter: OpacityMaskFilter;
    public attribute fill: Paint;
    
    public function createFilter(): BufferedImageOp {
        opacityMaskFilter = new OpacityMaskFilter();
        opacityMaskFilter.setOpacityMask(awtFill);
        return opacityMaskFilter;
    }
}


