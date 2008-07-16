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

package javafx.application;

/**
 * The JavaFX {@code Application} class provides application lifecycle
 * support methods and attributes. The content of the application is
 * specified on its {@code stage} attribute.
 * 
 * @profile common
 */
public class Application {
    
    /**
     * Called when Application is being started and before it
     * is displayed.
     * 
     * @profile common
     */ 
    public attribute onStart:function():Void;
    
    /**
     * Called when the Application is resumed - i.e. it goes from suspended state. 
     * Typically when the application window is made active or when the document
     * containing the Application is made active in the browser or in the case of 
     * a mobile device when the user switches to the application.
     * 
     * @profile common
     */ 
    public attribute onResume:function():Void;
    
    /**
     * 
     * Called when the Application is being put into - i.e. when the window with the application
     * looses focus, or the document containing the Application is no longer the active
     * document in the browser or the application is running on background in the 
     * mobile device.
     * 
     * @profile common
     */ 
    public attribute onSuspend:function():Void;
    

    /**
     * Called when the application is about to exit. The UI components of
     * the application might no longer be visible.
     * 
     * @profile common
     */
    public attribute onExit:function():Void;
    
    /**
     * Specifies if the Application is in suspended or running 
     * state.
     * 
     * @profile common
     * @readonly
     */ 
    public attribute /*read-only*/ suspended:Boolean;       
    
    /**
     * Requests the application to exit. The onExit callback is called after this
     * method is executed.
     * 
     * @profile common
     */ 
    public function exit() {};
    
    /**
     * Requests the application to go to the suspend() mode. In the case 
     * application is in suspended mode, the function has no effect.
     * 
     * The behavior of this funciton is platform specific. On desktop it
     * would typically mean minimize. On other platforms this function might 
     * have no effect.
     * 
     * @profile common
     */
    public function suspend() {};
    
    /**
     * Requests the application to go to back from the suspend mode to normal 
     * (resumed) operation. In the case the application is already running (i.e. 
     * it is no longer suspended), the function has no effect.
     * 
     * The behavior of this funciton is platform specific. On desktop it
     * would typically mean restore from the minimized state. On other platforms
     * this function might  have no effect.
     * 
     * @profile common
     */
    public function resume() {};

    /**
     * The stage for the application content.
     * 
     * @profile common
     */
    public attribute stage: Stage;

}
