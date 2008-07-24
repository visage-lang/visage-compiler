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

/**
 *    Copyright 2007 Pieter-Jan Savat
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.sun.javafx.api.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.MouseInputListener;

/**
 * The BookPanel uses the page turn effect to navigate between pages. The images used
 * as pages are set by specifying their location, the basename and the extension.
 * Each image should have the same basename followed by its index, ranging 1 to x.
 * If a 0 index image exists at the location then it is used (even though it is not
 * counted in the nrOfPages for the setPages() method).
 * 
 * Navigating through the book can be done by clicking in the bottomright or 
 * bottomleft corner, by using the 4 arrow keys or by dragging and dropping
 * a page.
 * To turn pages programmatically either call nextPage() or previousPage(). Jumping
 * to a page can be done by calling setLeftPageIndex().
 *  
 * By default soft clipping is off and the borders at the edges of the papers are visible.
 * 
 * TODO fade drop shadow on percentage turned (see passedThreshold for calculations)
 * 
 * @author Pieter-Jan Savat
 */
public class JBookPanel extends JPanel 
    implements MouseInputListener, ActionListener {

    public interface BookPanelViewModel {
        public int getPageCount();
        public JComponent getPage(int index);
    }


    BookPanelViewModel model;
    Set<ChangeListener> selectionListeners = new HashSet<ChangeListener>();

    public void addSelectionListener(ChangeListener listener) {
        selectionListeners.add(listener);
    }

    public void removeSelectionListener(ChangeListener listener) {
        selectionListeners.remove(listener);
    }

    void fireSelectionChange() {
        ChangeListener[] arr = new ChangeListener[selectionListeners.size()];
        selectionListeners.toArray(arr);
        ChangeEvent e = new ChangeEvent(this);
        for (int i = 0; i < arr.length; i++) {
            arr[i].stateChanged(e);
        }
    }
    

    protected static final double PI_DIV_2 = Math.PI / 2.0;
    protected enum AutomatedAction {AUTO_TURN, AUTO_DROP_GO_BACK, AUTO_DROP_BUT_TURN}
	
    protected int rotationX;
    protected double nextPageAngle;
    protected double backPageAngle;
	
    protected Timer timer;
    protected AutomatedAction action;
    protected Point autoPoint;
    protected Point tmpPoint;
	
    protected int leftPageIndex;
	
    protected JComponent currentLeftImage;
    protected JComponent currentRightImage;
    protected JComponent nextLeftImage;
    protected JComponent nextRightImage;
    protected JComponent previousLeftImage;
    protected JComponent previousRightImage;
	
    protected String pageLocation;
    protected String pageName;
    protected String pageExtension;
    protected int nrOfPages;
    protected boolean leftPageTurn;
    protected boolean rightPageTurn;
    protected int refreshSpeed;
	
    // used to store the bounds of the book
    protected Rectangle bookBounds;
    protected int pageWidth;
    int animationDuration = 1000;
    long startTime;
	
    protected int shadowWidth;
    protected int cornerRegionSize;
    protected boolean softClipping;
    protected boolean borderLinesVisible;
	
    // vars for optimization and reuse
    protected Color shadowDarkColor;
    protected Color shadowNeutralColor;
    protected GeneralPath clipPath;
	
    /**
     * Constructor
     */
    public JBookPanel() {
        super();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextPage");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "nextPage");
        this.getActionMap().put("nextPage", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    leftPageTurn = false;
                    rightPageTurn = true;
                    nextPage();
                }});
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "previousPage");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "previousPage");
        this.getActionMap().put("previousPage", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    leftPageTurn = true;
                    rightPageTurn = false;
                    previousPage();
                }});
                
        refreshSpeed = 25;
        shadowWidth = 100;
        cornerRegionSize = 20;
        bookBounds = new Rectangle();
        softClipping = false;
        borderLinesVisible = true;
        clipPath = new GeneralPath();
        shadowDarkColor = new Color(0,0,0,130);
        shadowNeutralColor = new Color(255,255,255,0);
        this.setBackground(Color.LIGHT_GRAY);
		
        timer = new Timer(refreshSpeed, this);
        timer.stop();
        leftPageIndex = -1;
	/*	
        setPages(null, null, null, 
                 13, 210, 342);
        setMargins(70, 80);
        */
    }
	
    ///////////////////
    // PAINT METHODS //
    ///////////////////
	
	public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            setGraphicsHints(g2);
		
            // background
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
            // page 1
            paintPage(g2, currentLeftImage, bookBounds.x, bookBounds.y, pageWidth, bookBounds.height, this, false);
		
            // page 2
            paintPage(g2, currentRightImage, bookBounds.x + pageWidth, bookBounds.y, pageWidth, bookBounds.height, this, true);

            if (leftPageTurn) {
                if (softClipping) {
                    paintLeftPageSoftClipped(g2);
                } else {
                    paintLeftPage(g2);
                }
            } else if (rightPageTurn) {
                if (softClipping) {
                    paintRightPageSoftClipped(g2);
                } else {
                    paintRightPage(g2);
                }
            }
	}
	
    protected void paintLeftPage(Graphics2D g2) {
        // translate to rotation point
        g2.translate(bookBounds.width + bookBounds.x - rotationX + bookBounds.x, 
                     bookBounds.y + bookBounds.height);
		
        // rotate over back of page A angle
        g2.rotate(-backPageAngle);
		
        // translate the amount already done
        int done = bookBounds.width + bookBounds.x - rotationX;
        g2.translate(done - pageWidth, 0);
		
        // page 3 (= back of page 1)
        clipPath.reset();
        clipPath.moveTo(pageWidth, 0);
        clipPath.lineTo(pageWidth-done, 0);
        clipPath.lineTo(pageWidth,
                        -1 * (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
        clipPath.closePath();
        Shape s = g2.getClip();
        g2.clip(clipPath);
        paintPage(g2, previousRightImage, 0, 0 - bookBounds.height, pageWidth, bookBounds.height, this, false);
        g2.setClip(s);
		
        // translate back
        g2.translate(pageWidth - done, 0);
		
        // rotate back
        g2.rotate(backPageAngle);

        // translate back
        g2.translate(rotationX - bookBounds.width - bookBounds.x - bookBounds.x, 
                     -bookBounds.y - bookBounds.height);
		
        // page 4
        clipPath.reset();
        clipPath.moveTo(bookBounds.x,
                        bookBounds.height + bookBounds.y);
        clipPath.lineTo(bookBounds.x + done, 
                        bookBounds.height + bookBounds.y);
        clipPath.lineTo(bookBounds.x,
                        bookBounds.height + bookBounds.y - (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
        clipPath.closePath();
        g2.clip(clipPath);
        paintPage(g2, previousLeftImage, bookBounds.x, bookBounds.y, 
                  pageWidth, bookBounds.height, this, true);
        g2.draw(clipPath);
		
        // add drop shadow
        GradientPaint grad = new GradientPaint(bookBounds.x + done, 
                                               bookBounds.height + bookBounds.y, 
                                               shadowDarkColor, 
                                               bookBounds.x + done - shadowWidth, 
                                               bookBounds.height + bookBounds.y + (int)(Math.cos(PI_DIV_2 - nextPageAngle) * shadowWidth), 
                                               shadowNeutralColor, 
                                               false);
        g2.setPaint(grad);
        g2.fillRect(bookBounds.x, bookBounds.y, 
                    pageWidth, bookBounds.height);
    }

    BufferedImage painter = null;
	
    protected void paintLeftPageSoftClipped(Graphics2D g2) {

        // calculate amount done (traveled)
        int done = bookBounds.width + bookBounds.x - rotationX;

        ///////////////////////////////
            // page 3 (= back of page 1) //
            ///////////////////////////////
		
            // init clip path
            clipPath.reset();
            clipPath.moveTo(pageWidth, 0);
            clipPath.lineTo(pageWidth-done, 0);
            clipPath.lineTo(pageWidth,
                            -1 * (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
            clipPath.closePath();
		
            // init soft clip image
            BufferedImage img;
            Graphics2D gImg;
            if (painter == null || painter.getWidth() != this.getWidth() || painter.getHeight() != this.getHeight()) {
                GraphicsConfiguration gc = g2.getDeviceConfiguration();
                painter =gc.createCompatibleImage(this.getWidth(), this.getHeight(), Transparency.TRANSLUCENT);
            }
            img = painter;
            gImg = img.createGraphics();
		
            // translate to rotation point
            gImg.translate(bookBounds.width + bookBounds.x - rotationX + bookBounds.x, 
                           bookBounds.y + bookBounds.height);
		
            // rotate over back of page A angle
            gImg.rotate(-backPageAngle);
		
            // translate the amount already done
            gImg.translate(done - pageWidth, 0);
		
            // init area on which may be painted
            gImg.setComposite(AlphaComposite.Clear);
            gImg.fillRect(0, 0, this.getWidth(), this.getHeight());
            gImg.setComposite(AlphaComposite.Src);
            gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gImg.setColor(Color.WHITE);
            gImg.fill(clipPath);								 
            gImg.setColor(new Color(255,255,255,0));
            gImg.fillRect(0, 0 - bookBounds.height - bookBounds.height, 
                          pageWidth+10, bookBounds.height); 	// remove the top margin from allowed area
            gImg.setComposite(AlphaComposite.SrcAtop);
		
            // paint page
            paintPage(gImg, previousRightImage, 0, 0 - bookBounds.height, pageWidth, bookBounds.height, this, false);

            // translate back
            gImg.translate(pageWidth - done, 0);
		
            // rotate back
            gImg.rotate(backPageAngle);

            // translate back
            gImg.translate(rotationX - bookBounds.width - bookBounds.x - bookBounds.x, 
                           -bookBounds.y - bookBounds.height);
				
            gImg.dispose();
            g2.drawImage(img, 0, 0, null);
		
            ////////////
            // page 4 //
            ////////////
		
            // init clip path
            clipPath.reset();
            clipPath.moveTo(bookBounds.x,
                            bookBounds.height + bookBounds.y);
            clipPath.lineTo(bookBounds.x + done, 
                            bookBounds.height + bookBounds.y);
            clipPath.lineTo(bookBounds.x,
                            bookBounds.height + bookBounds.y - (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
            clipPath.closePath();
		
            // init soft clip image
            if (painter == null || painter.getWidth() != this.getWidth() || painter.getHeight() != this.getHeight()) {
                GraphicsConfiguration gc = g2.getDeviceConfiguration();
                painter =gc.createCompatibleImage(this.getWidth(), this.getHeight(), Transparency.TRANSLUCENT);
            }
            img = painter;
            gImg = img.createGraphics();
            gImg.setComposite(AlphaComposite.Clear);
            gImg.fillRect(0, 0, this.getWidth(), this.getHeight());
            gImg.setComposite(AlphaComposite.Src);
            gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gImg.setColor(Color.WHITE);
            gImg.fill(clipPath);								// init area on which may be painted
            gImg.setColor(new Color(255,255,255,0));
            gImg.fillRect(0,0,this.getWidth(), bookBounds.y);	// remove the top margin from allowed area
            gImg.setComposite(AlphaComposite.SrcAtop);
		
            // paint page
            paintPage(gImg, previousLeftImage, bookBounds.x, bookBounds.y, 
                      pageWidth, bookBounds.height, this, true);
		
            // paint shadow
            GradientPaint grad = new GradientPaint(bookBounds.x + done, 
                                                   bookBounds.height + bookBounds.y, 
                                                   shadowDarkColor, 
                                                   bookBounds.x + done - shadowWidth, 
                                                   bookBounds.height + bookBounds.y + (int)(Math.cos(PI_DIV_2 - nextPageAngle) * shadowWidth), 
                                                   shadowNeutralColor, 
                                                   false);
            gImg.setPaint(grad);
            gImg.fillRect(bookBounds.x, bookBounds.y, 
                          pageWidth, bookBounds.height);
		
            gImg.dispose();
            g2.drawImage(img, 0, 0, null);
    }
	
    protected void paintRightPage(Graphics2D g2) {

        // translate to rotation point
        g2.translate(rotationX, bookBounds.y + bookBounds.height);
		
        // rotate over back of page A angle
        g2.rotate(backPageAngle);
		
        // translate the amount already done
        int done = bookBounds.width + bookBounds.x - rotationX;
        g2.translate(-done, 0);
		
        // page 3 (= back of page 1)
        clipPath.reset();
        clipPath.moveTo(0, 0);
        clipPath.lineTo(done, 0);
        clipPath.lineTo(0,
                        -1 * (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
        clipPath.closePath();
        Shape s = g2.getClip();
        g2.clip(clipPath);
        paintPage(g2, nextLeftImage, 0, 0 - bookBounds.height, pageWidth, bookBounds.height, this, false);
        g2.setClip(s);
		
        // translate back
        g2.translate(done, 0);
		
        // rotate back
        g2.rotate(-backPageAngle);
		
        // translate back
        g2.translate(-rotationX, -bookBounds.y - bookBounds.height);
		
        // page 4
        clipPath.reset();
        clipPath.moveTo(bookBounds.width + bookBounds.x,
                        bookBounds.height + bookBounds.y);
        clipPath.lineTo(rotationX, bookBounds.height + bookBounds.y);
        clipPath.lineTo(bookBounds.width + bookBounds.x,
                        bookBounds.height + bookBounds.y - (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
        clipPath.closePath();
        g2.clip(clipPath);
        paintPage(g2, nextRightImage, pageWidth + bookBounds.x, bookBounds.y, 
                  pageWidth, bookBounds.height, this, true);
		
        // add drop shadow
        GradientPaint grad = new GradientPaint(rotationX, 
                                               bookBounds.height + bookBounds.y, 
                                               shadowDarkColor, 
                                               rotationX + shadowWidth, 
                                               bookBounds.height + bookBounds.y + (int)(Math.cos(PI_DIV_2 - nextPageAngle) * shadowWidth), 
                                               shadowNeutralColor, 
                                               false);
        g2.setPaint(grad);
        g2.fillRect(pageWidth + bookBounds.x, bookBounds.y, 
                    pageWidth, bookBounds.height);
    }
	
    protected void paintRightPageSoftClipped(Graphics2D g2) {

        int done = bookBounds.width + bookBounds.x - rotationX;
		
        ///////////////////////////////
            // page 3 (= back of page 1) //
            ///////////////////////////////

            // init clip path
            clipPath.reset();
            clipPath.moveTo(0, 0);
            clipPath.lineTo(done, 0);
            clipPath.lineTo(0,
                            -1 * (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
            clipPath.closePath();
		
            // init soft clip image
            if (painter == null || painter.getWidth() != this.getWidth() || painter.getHeight() != this.getHeight()) {
                GraphicsConfiguration gc = g2.getDeviceConfiguration();
                painter =gc.createCompatibleImage(this.getWidth(), this.getHeight(), Transparency.TRANSLUCENT);
            }
            BufferedImage img = painter;
            Graphics2D gImg = img.createGraphics();
		
            // translate to rotation point
            gImg.translate(rotationX, bookBounds.y + bookBounds.height);
		
            // rotate over back of page A angle
            gImg.rotate(backPageAngle);
		
            // translate the amount already done
            gImg.translate(-done, 0);
		
            // init area on which may be painted
            gImg.setComposite(AlphaComposite.Clear);
            gImg.fillRect(0, 0, this.getWidth(), this.getHeight());
            gImg.setComposite(AlphaComposite.Src);
            gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gImg.setColor(Color.WHITE);
            gImg.fill(clipPath);								 
            gImg.setColor(new Color(255,255,255,0));
            gImg.fillRect(-10, 0 - bookBounds.height - bookBounds.height, 
                          pageWidth+10, bookBounds.height); 	// remove the top margin from allowed area
            gImg.setComposite(AlphaComposite.SrcAtop);
		
            // paint page
            paintPage(gImg, nextLeftImage, 0, 0 - bookBounds.height, pageWidth, bookBounds.height, this, false);

            // translate back
            gImg.translate(done, 0);
		
            // rotate back
            gImg.rotate(-backPageAngle);
		
            // translate back
            gImg.translate(-rotationX, -bookBounds.y - bookBounds.height);
		
            gImg.dispose();
            g2.drawImage(img, 0, 0, null);
		
            ////////////
            // page 4 //
            ////////////
		
            // init clip path
            clipPath.reset();
            clipPath.moveTo(bookBounds.width + bookBounds.x,
                            bookBounds.height + bookBounds.y);
            clipPath.lineTo(rotationX, bookBounds.height + bookBounds.y);
            clipPath.lineTo(bookBounds.width + bookBounds.x,
                            bookBounds.height + bookBounds.y - (int)(Math.tan(PI_DIV_2 - nextPageAngle) * done));
            clipPath.closePath();

            // init soft clip image
            GraphicsConfiguration gc = g2.getDeviceConfiguration();
            img = gc.createCompatibleImage(this.getWidth(), this.getHeight(), Transparency.TRANSLUCENT);
            gImg = img.createGraphics();
            gImg.setComposite(AlphaComposite.Clear);
            gImg.fillRect(0, 0, this.getWidth(), this.getHeight());
            gImg.setComposite(AlphaComposite.Src);
            gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gImg.setColor(Color.WHITE);
            gImg.fill(clipPath);								// init area on which may be painted
            gImg.setColor(new Color(255,255,255,0));
            gImg.fillRect(0,0,this.getWidth(), bookBounds.y);	// remove the top margin from allowed area
            gImg.setComposite(AlphaComposite.SrcAtop);
		
            // paint page
            paintPage(gImg, nextRightImage, pageWidth + bookBounds.x, bookBounds.y, 
                      pageWidth, bookBounds.height, this, true);
		
            // add drop shadow
            GradientPaint grad = new GradientPaint(rotationX, 
                                                   bookBounds.height + bookBounds.y, 
                                                   shadowDarkColor, 
                                                   rotationX + shadowWidth, 
                                                   bookBounds.height + bookBounds.y + (int)(Math.cos(PI_DIV_2 - nextPageAngle) * shadowWidth), 
                                                   shadowNeutralColor, 
                                                   false);
            gImg.setPaint(grad);
            gImg.fillRect(pageWidth + bookBounds.x, bookBounds.y, 
                          pageWidth, bookBounds.height);
		
            gImg.dispose();
            g2.drawImage(img, 0, 0, null);
    }
	
    protected void paintPage(Graphics2D g, JComponent img, int x, int y, int w, int h, ImageObserver o,
                             boolean rightPage) {
        if (img == null) {
            Color oldColor = g.getColor();
            g.setColor(this.getBackground());
            g.fillRect(x, y, w+10, h+10);
            g.setColor(oldColor);
            return;
        }
        Color oldColor = g.getColor();
        //
        //g.drawImage(img, x, y, w, h, o);
        /*
        CellRendererPane r = new CellRendererPane();
        r.paintComponent(g, img, this, 
                         x, y, w, h);
        */
        img.setDoubleBuffered(false);
        Graphics cg = g.create(x, y, w, h);
        img.setBounds(x, y, w, h);
        img.paint(cg);
        cg.dispose();

        //img.paint(g);
        //
        // stop if no borders are needed
        if (!borderLinesVisible) {
            return;
        }
		
        if (rightPage) {
            g.setColor(Color.GRAY);
            g.drawLine(x + w, y, 
                       x + w, y + h);
            g.drawLine(x, y, 
                       x + w, y);
            g.drawLine(x, y + h, 
                       x + w, y + h);
			
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x + w - 1, y + 1, 
                       x + w - 1, y + h - 1);
            g.drawLine(x, y + 1, 
                       x + w - 1, y + 1);
            g.drawLine(x, y + h - 1, 
                       x + w - 1, y + h - 1);
			
            g.drawLine(x, y + 2, x, y + h - 2);
			
            g.setColor(oldColor);
			
        } else {
            g.setColor(Color.GRAY);
            g.drawLine(x, y, 
                       x, y + h);
            g.drawLine(x, y, 
                       x + w, y);
            g.drawLine(x, y + h, 
                       x + w, y + h);
			
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x + 1, y + 1, 
                       x + 1, y + h - 1);
            g.drawLine(x + 1, y + 1, 
                       x + w - 1, y + 1);
            g.drawLine(x + 1, y + h - 1, 
                       x + w - 1, y + h - 1);

            g.drawLine(x + w - 1, y + 2, x + w - 1, y + h - 2);
			
            g.setColor(oldColor);
        }
    }
	
    protected void paintPageNumber(Graphics g, int index, int width, int height) {
        g.setFont(new Font("Arial", Font.BOLD, 11));
        int w = (int) g.getFontMetrics().getStringBounds(String.valueOf(index), g).getWidth();
        int x = (index % 2 == 0)? width - 8 - w : 8;
        int y = height - 10;
        g.setColor(Color.GRAY);
        g.drawString(String.valueOf(index), x, y);
    }
	
    ///////////////////
    // EVENT METHODS //
    ///////////////////
	
	public void mouseEntered(MouseEvent e) {}	
	
    public void mousePressed(MouseEvent e) {}
	
    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        if (isMouseInRegion(e)) {
            nextPage();
        }
    }
	
    public void mouseDragged(MouseEvent e) {

        // if action busy then dont intervene
        if (action != null) {
            return;
        }
        // decide whether to grab left page or right page
        if (this.rotationX == bookBounds.x + bookBounds.width) {
            if (e.getPoint().x < bookBounds.x + pageWidth) {
                this.leftPageTurn = true;
                this.rightPageTurn = false;
            } else {
                this.leftPageTurn = false;
                this.rightPageTurn = true;
            }
        }
		
        if (isMouseInBook(e)) {
            if (this.getCursor().getType() != Cursor.HAND_CURSOR) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            calculate(e.getPoint());
        }
        this.repaint();
    }
	
    public void mouseMoved(MouseEvent e) {
		
        // if action busy then dont intervene
        if (action != null) {
            return;
        }
		
        if (isMouseInRegion(e)) {
            int xOffset = (leftPageTurn)? 10 : -10;
            Point p = new Point(e.getX() + xOffset, e.getY() - 10);
            calculate(p);
        } else if (rotationX != bookBounds.width + bookBounds.x) {
            rotationX = bookBounds.width + bookBounds.x;
            this.repaint();
        }
    }
	
    public void mouseReleased(MouseEvent e) {
        if (this.getCursor().getType() != Cursor.DEFAULT_CURSOR) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
		
        // drop page back or forward depending on...
        if (rotationX != (bookBounds.width + bookBounds.x)
            && rotationX != bookBounds.x) {
			
            // base decision on amount of surface showing
            if (passedThreshold()) {
                //System.out.println("drop turn");
                startTime = System.currentTimeMillis();
                action = AutomatedAction.AUTO_DROP_BUT_TURN;
                autoPoint = (Point) e.getPoint().clone();
				
                if (leftPageTurn) {
                    autoPoint.x = this.transformIndex(autoPoint.x);
                }
				
                tmpPoint = (Point) autoPoint.clone();
                this.timer.restart();
				
            } else {
                //System.out.println("drop go back");
                startTime = System.currentTimeMillis();
                action = AutomatedAction.AUTO_DROP_GO_BACK;
                autoPoint = (Point) e.getPoint().clone();
				
                if (leftPageTurn) {
                    autoPoint.x = this.transformIndex(autoPoint.x);
                }
				
                tmpPoint = (Point) autoPoint.clone();
                this.timer.restart();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

        int D = 50;

        switch (action) {
		
        case AUTO_TURN:
			
            // update autoPoint
            double nextX = autoPoint.getX() - D;
            double x = nextX;
            x = x - bookBounds.x - pageWidth;
            double y = -1.0 * Math.pow(x / pageWidth, 2);
            y += 1;
            y *= 50;
			
            if (leftPageTurn) {
                nextX = this.transformIndex(nextX);
            } 
			
            autoPoint.setLocation(nextX, bookBounds.y + bookBounds.height - y);
			
            if (nextX <= bookBounds.x || nextX >= bookBounds.x + bookBounds.width) {
                timer.stop();
                action = null;
                switchImages();
                autoPoint.x = bookBounds.x;
                calculate(autoPoint);
                initRotationX();
                this.repaint();
                return;
            }
			
            // calculate using new point
            calculate(autoPoint);
			
            break;
			
        case AUTO_DROP_GO_BACK:
			
            int xDiff = bookBounds.width + bookBounds.x - autoPoint.x;
            int stepsNeeded = 1 + (xDiff / D);	
            if (stepsNeeded == 0) {
                stepsNeeded = 1;
            }
            int yStep = (bookBounds.y + bookBounds.height - autoPoint.y) / stepsNeeded; 
			
            tmpPoint.x = tmpPoint.x + D;
            tmpPoint.y = tmpPoint.y + yStep;
            if (tmpPoint.x >= bookBounds.width + bookBounds.x) {
                timer.stop();
                action = null;
            }
			
            Point newPoint = (Point) tmpPoint.clone();
			
            if (leftPageTurn) {
                newPoint.x = this.transformIndex(tmpPoint.x);
            }
			
            // calculate using new point
            calculate(newPoint);
			
            break;
			
        case AUTO_DROP_BUT_TURN:
			
            int xDiff2 = autoPoint.x - bookBounds.x;
            int stepsNeeded2 = 1 + (xDiff2 / D);
            if (stepsNeeded2 == 0) {
                stepsNeeded2 = 1;
            }
            int yStep2 = (bookBounds.y + bookBounds.height - autoPoint.y) / stepsNeeded2; 
			
            tmpPoint.x = tmpPoint.x - D;
            tmpPoint.y = tmpPoint.y + yStep2;
            if (tmpPoint.x <= bookBounds.x) {
                timer.stop();
                action = null;
                switchImages();
                if (leftPageTurn) {
                    tmpPoint.x = this.transformIndex(bookBounds.x);
                } else {
                    tmpPoint.x = bookBounds.x;
                }
                tmpPoint.y = bookBounds.y + bookBounds.height;
                calculate(tmpPoint);
                initRotationX();
                this.repaint();
                return;
            }
			
            Point newPoint2 = (Point) tmpPoint.clone();
			
            if (leftPageTurn) {
                newPoint2.x = this.transformIndex(tmpPoint.x);
            }
			
            // calculate using new point
            calculate(newPoint2);
			
            break;
        }
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////
	
    protected boolean isMouseInRegion(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
		
        int maxX = bookBounds.x + bookBounds.width;
        int minX = maxX - cornerRegionSize;
        int maxY = bookBounds.y + bookBounds.height;
        int minY = maxY - cornerRegionSize;
		
        int minX2 = bookBounds.x;
        int maxX2 = minX2 + cornerRegionSize;
		
        leftPageTurn = ((x < maxX2) && (x > minX2));
        rightPageTurn = !leftPageTurn;

        return ( (y < maxY)
                 && (y > minY)
                 && ( ((x < maxX) && (x > minX))
                      || ((x < maxX2) && (x > minX2))
                      ) );
    }
	
    protected boolean isMouseInBook(MouseEvent e) {
        return bookBounds.contains(e.getX(), e.getY());
    }

    protected double transformIndex(double x) {
        return bookBounds.width + bookBounds.x - x + bookBounds.x;
    }
	
    protected int transformIndex(int x) {
        return bookBounds.width + bookBounds.x - x + bookBounds.x;
    }
	
    protected void calculate(Point p) {

        if (leftPageTurn) {
            p.x = transformIndex(p.x);
        }
		
        // if no page to turn available then dont 
        // allow turn effect
        if (currentRightImage == null && !leftPageTurn) {
            rotationX = bookBounds.width + bookBounds.x;
            nextPageAngle = 0;
            backPageAngle = 0;
            return;
        } else if (currentLeftImage == null && leftPageTurn) {
            rotationX = bookBounds.width + bookBounds.x;
            nextPageAngle = 0;
            backPageAngle = 0;
            return;
        }
		
        Point cp = new Point(bookBounds.width + bookBounds.x, bookBounds.y + bookBounds.height);               
        double bRico = 1.0 * (cp.x - p.getX()) / (cp.y - p.getY());
        bRico = -bRico;
        Point mid = new Point(0,0);
        mid.x = (int)((cp.x + p.getX()) / 2.0);
        mid.y = (int)((cp.y + p.getY()) / 2.0);
        double c = mid.y - bRico * mid.x;
        rotationX = Math.max((int)((cp.y - c)/ bRico), pageWidth + bookBounds.x);

        nextPageAngle = PI_DIV_2 - Math.abs(Math.atan(bRico));
        backPageAngle = 2.0 * nextPageAngle;
        this.repaint();
    }

    public void nextPage() {
        action = AutomatedAction.AUTO_TURN;
        autoPoint = new Point(bookBounds.x + bookBounds.width,
                              bookBounds.y + bookBounds.height);
        this.timer.restart();
    }
	
    public void previousPage() {
        leftPageTurn = true;
        rightPageTurn = false;
        nextPage();
    }
		
    protected void initRotationX() {
        rotationX = bookBounds.width + bookBounds.x;
    }
		
    protected boolean passedThreshold() {
        int done = bookBounds.width + bookBounds.x - rotationX;
        double X = Math.min(Math.tan(PI_DIV_2 - nextPageAngle) * done, bookBounds.height);
        X *= done * 2;
        double threshold = bookBounds.height * pageWidth;
        return X > threshold;
    }
	
    protected void switchImages() {
        if (leftPageTurn) {
            leftPageIndex -= 2;
            currentLeftImage = previousLeftImage;
            currentRightImage = previousRightImage;
			
        } else {
            leftPageIndex += 2;
            currentLeftImage = nextLeftImage;
            currentRightImage = nextRightImage;
        }
        leftPageTurn = rightPageTurn = false;
        nextLeftImage = getPage(leftPageIndex + 2);
        nextRightImage = getPage(leftPageIndex + 3);
        previousLeftImage = getPage(leftPageIndex - 2);
        previousRightImage = getPage(leftPageIndex - 1);
        fireSelectionChange();
        revalidate();
        repaint();
    }
	
    protected JComponent getBlankPage(int index) {
        /*
        BufferedImage img = new BufferedImage(pageWidth, bookBounds.height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics gfx = img.getGraphics();
        gfx.setColor(Color.WHITE);
        gfx.fillRect(0, 0, img.getWidth(), img.getHeight());
        */
        JLabel label = new JLabel();
        //label.setIcon(new ImageIcon(img));
        label.setSize(pageWidth, bookBounds.height);
        return label;
    }
		
    protected void setGraphicsHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                            RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
    }

    public void setModel(BookPanelViewModel model) {
        this.model = model;
    }

    class ComponentHolder extends JComponent {
    }

    ComponentHolder wrapper = new ComponentHolder() {
            public void repaint(long tm, int x, int y, int w, int h) {
                //System.out.println("repainting: "+ new Rectangle(x, y, w, h));
                super.repaint(tm , x, y, w, h);
            }
            public void paint(Graphics g) {
            }
        };
    {
        add(wrapper);
        wrapper.setBounds(0, 0, 0, 0);
    }

    protected JComponent loadPage(int index) {
        JComponent comp = model.getPage(index);
        if (comp == null) {
            return getBlankPage(index);
        }
        if (comp.getParent() != wrapper) {
            wrapper.add(comp);
        }
        return comp;
    }
	
    protected JComponent getPage(int index) {
        //TODO this var hides the Object field with the same name???
        int nrOfPages = model.getPageCount();
        // if request goes beyond available pages return null
        if (index > nrOfPages) {
			
            // if back of existing page then return a blank
            if ( (index - 1) % 2 == 0) {
                return getBlankPage(index);
            } else {
                return null;
            }
        }
        if (index < 1) {
            if (index == 0) {
                try {
                    return loadPage(index);
                } catch (Exception e) {
                    return getBlankPage(index);
                }
            } else {
                return null;
            }
        }
		
        // if no images specified return blank ones
        if (false && (pageLocation == null 
                      || pageName == null 
                      ||  pageExtension == null )) {
			
            // create the blank page
            JComponent comp = getBlankPage(index);
			
            // draw page number
            //Graphics gfx = img.getGraphics();
            //Graphics2D g2 = (Graphics2D) gfx;
            //setGraphicsHints(g2);
            //paintPageNumber(gfx, index, comp.getWidth(), comp.getHeight());
			
            //return img;
            return comp;
			
        } else {
            return loadPage(index);
        }
    }
	
    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////
	
     public void setMargins(int left, int top, int w, int h) {
         bookBounds.x = left;
         bookBounds.y = top;
         bookBounds.width = 2 * w;
         bookBounds.height = h;
         pageWidth = w;
         initRotationX();
    }
	
    public void setPages(String pageLocation, String pageName, String pageExtension, int nrOfPages,
                         int pageWidth, int pageHeight) {
		
        this.pageLocation = pageLocation;
        this.pageName = pageName;
        this.pageExtension = pageExtension;
        this.nrOfPages = nrOfPages;

        this.pageWidth = pageWidth;
        bookBounds.width = 2 * pageWidth;
        bookBounds.height = pageHeight;
		
        initRotationX();
		
        currentLeftImage = getPage(leftPageIndex);
        currentRightImage = getPage(leftPageIndex + 1);
        nextLeftImage = getPage(leftPageIndex + 2);
        nextRightImage = getPage(leftPageIndex + 3);
        previousLeftImage = getPage(leftPageIndex - 2);
        previousRightImage = getPage(leftPageIndex - 1);
    }
	
    public int getRefreshSpeed() {
        return refreshSpeed;
    }

    public void setRefreshSpeed(int refreshSpeed) {
        this.refreshSpeed = refreshSpeed;
    }

    public Color getShadowDarkColor() {
        return shadowDarkColor;
    }

    public void setShadowDarkColor(Color shadowDarkColor) {
        this.shadowDarkColor = shadowDarkColor;
    }

    public Color getShadowNeutralColor() {
        return shadowNeutralColor;
    }

    public void setShadowNeutralColor(Color shadowNeutralColor) {
        this.shadowNeutralColor = shadowNeutralColor;
    }

    public int getShadowWidth() {
        return shadowWidth;
    }

    public void setShadowWidth(int shadowWidth) {
        this.shadowWidth = shadowWidth;
    }

    public boolean isSoftClipping() {
        return softClipping;
    }

    public void setSoftClipping(boolean softClipping) {
        this.softClipping = softClipping;
    }

    public boolean isBorderLinesVisible() {
        return borderLinesVisible;
    }

    public void setBorderLinesVisible(boolean borderLinesVisible) {
        this.borderLinesVisible = borderLinesVisible;
    }

    public int getLeftPageIndex() {
        return leftPageIndex;
    }

    public void setLeftPageIndex(int leftPageIndex) {
        if (leftPageIndex <= -1) {
            leftPageIndex = -1;
        } else {
            if (this.leftPageIndex == leftPageIndex) {
                return;
            }
        }
        this.leftPageIndex = leftPageIndex;
        previousLeftImage = getPage(leftPageIndex - 2);
        previousRightImage = getPage(leftPageIndex - 1);
        currentLeftImage = getPage(leftPageIndex);;
        currentRightImage = getPage(leftPageIndex + 1);;
        nextLeftImage = getPage(leftPageIndex + 2);
        nextRightImage = getPage(leftPageIndex + 3);
        revalidate();
        fireSelectionChange();
    }

    public Dimension getPreferredSize() {
        return new Dimension((bookBounds.x + pageWidth)*2,
                             bookBounds.y*2 + bookBounds.height);
    }

    public void doLayout() {
        if (currentLeftImage != null) {
            currentLeftImage.setBounds(bookBounds.x, 
                                       bookBounds.y, 
                                       pageWidth, 
                                       bookBounds.height);
        }
        if (currentRightImage != null) {
            currentRightImage.setBounds(bookBounds.x + pageWidth,
                                        bookBounds.y, 
                                        pageWidth, 
                                        bookBounds.height);
            
        }
        if (previousLeftImage != null) {
            previousLeftImage.setBounds(bookBounds.x, 
                                        bookBounds.y, 
                                        pageWidth, 
                                        bookBounds.height);
        }
        if (previousRightImage != null) {
            previousRightImage.setBounds(bookBounds.x + pageWidth, 
                                         bookBounds.y, 
                                         pageWidth, 
                                         bookBounds.height);
        }
        if (nextLeftImage != null) {
            nextLeftImage.setBounds(bookBounds.x + pageWidth, 
                                         bookBounds.y, 
                                         pageWidth, 
                                         bookBounds.height);
        }
        if (nextRightImage != null) {
            nextRightImage.setBounds(bookBounds.x + pageWidth, 
                                         bookBounds.y, 
                                         pageWidth, 
                                         bookBounds.height);
        }
    }
}
