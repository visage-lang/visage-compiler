/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javafx.ui.filter;
import java.awt.image.BufferedImageOp;

public abstract class Filter {
    protected attribute bufferedImageOp: BufferedImageOp;
    public function getFilter(): BufferedImageOp {
        if (bufferedImageOp == null) {
            bufferedImageOp = this.createFilter();
        }
        return bufferedImageOp;
    }
    protected abstract function createFilter(): BufferedImageOp;
}

