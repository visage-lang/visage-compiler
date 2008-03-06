/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tesla;
import javafx.ui.*;
import javafx.ui.canvas.*;


public class CockpitStyling extends BodyStyling {
    init {
        imageBaseUrl = "http://www.teslamotors.com/design/cockpit/images-cockpit";
        thumbBaseUrl = "http://www.teslamotors.com/design/cockpit/thumbs-cockpit";

         images =
            [10, 
             200,
             40,
             30,
             300,
             20,
             60,
             140,
             220];
         }
}
