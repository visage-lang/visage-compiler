package hello;
import java.net.URL;
import java.awt.Toolkit;
import java.lang.Thread;
import java.lang.Runnable;
import javax.swing.SwingUtilities;
import java.lang.System;

public class Image extends Icon {

   var swingIcon = bind lazy if (awtImage == null) null else new javax.swing.ImageIcon(awtImage);
   var awtImage: java.awt.Image = 
       bind lazy if (url == null) null else Toolkit.getDefaultToolkit().createImage(new URL(url));

   public var url: String;

   public function getImage(): java.awt.Image {
        awtImage;
   }

   public function getIcon(): javax.swing.Icon {
        swingIcon;      	
   }
}
