import javafx.ui.canvas.*;
import javafx.ui.canvas.Line;
import javafx.ui.*;
import java.lang.Math;
import java.lang.System;
import javafx.geom.*;

public struct point2d  {
    public attribute x:Number;
    public attribute y:Number;
    public function dist(p:point2d):Number;
    public operation move(heading:Number, len:Number);
    public operation cast(heading:Number, len:Number):line2d;
    public operation center(heading:Number, len:Number):line2d;
    
}

operation point2d.move(heading:Number, len:Number): point2d {
    heading = Math.toRadians(heading);
    x += Math.cos(heading)*len;
    y += Math.sin(heading)*len;
    return this;
}

operation point2d.cast(heading:Number, len:Number) {
    var p = this;
    p.move(heading, len);
    var res = line2d {p1: this, p2: p };
    return res;
}


operation point2d.center(heading:Number, len:Number):line2d {
    var angle = Math.toRadians(heading);
    var cos = Math.cos(angle);
    var sin = Math.sin(angle);
    var half = len/2;
    return line2d { p1: point2d { x: x - half * cos, y: y - half * sin}, p2: { x: x + half * cos, y: y + half *sin} };
}

function point2d.dist(p:point2d) = select Math.sqrt(x*x + y*y) from x in p.x - x, y in p.y - y;

public struct line2d {
    public attribute p1:point2d;
    public attribute p2:point2d;
    function getX():Number;
    function getY():Number;
    function setX(n:Number);
    function setY(n:Number);
    function setLength(n:Number);
    function length():Number;
    function intersection(line:line2d):point2d;
    function intersect(line2d:line2d):line2d;
    function heading():Number;
    function center():point2d;
    function mul(n:Number):line2d;
    function dot(line:line2d):Number;
    function reflect(line:line2d):line2d;
    operation recast(heading:Number, len:Number):line2d;
    operation invert():line2d;
    
    operation translate(x:Number, y:Number);
    public operation getP1():point2d;
    public operation getP2():point2d;
    public operation getX1():Number;
    public operation getY1():Number;
    public operation getX2():Number;
    public operation getY2():Number;
    
}



function line2d.getP1() { return p1; }
function line2d.getP2() { return p2; }
function line2d.getX1() { return p1.x; }
function line2d.getY1() { return p1.y; }
function line2d.getX2() { return p2.x; }
function line2d.getY2() { return p2.y; }

operation line2d.translate(x:Number, y:Number) {
    p1.x += x;
    p1.y += y;
    p2.x += x;
    p2.y += y;
}


operation line2d.setLength(len:Number) {
    var oldLen = length();
    p2.x = p1.x + getX() * len / oldLen;
    p2.y = p1.y + getY() * len / oldLen;
}

function line2d.invert():line2d {
    return line2d { p1: p2, p2: p1 };
}

operation line2d.setX(x:Number) {
    p2.x = p1.x + x;
}

operation line2d.setY(y:Number) {
    p2.y = p1.y + y;
}

function line2d.getX() {return p2.x - p1.x; }
function line2d.getY() {return p2.y - p1.y;}

operation line2d.mul(n:Number) {
    return line2d { p1: point2d {x: p1.x, y: p1.y}, p2: point2d { x: getX() *n, y: getY()*n}};
}

function line2d.dot(line:line2d) {
    return getX() * line.getX() + getY() * line.getY();
}

operation line2d.reflect(line:line2d) {
    var n = mul(2*dot(line));
    setX(getX() - n.getX());
    setY(getY() - n.getY());
    setX(p1.x - getX());
    setY(p1.y - getY());
}

function line2d.recast(heading:Number, len:Number):line2d {
    var angle = Math.toRadians(heading);
    return line2d {p1: p1, p2: point2d {x: p1.x + Math.cos(angle)*len, y: p1.y + Math.sin(angle)*len}};
}


function line2d.center() = point2d { x: bind p1.x + getX()/2, y: bind p1.y + getY()/2 }; 

function line2d.heading() {
    var dx = getX();
    var dy = getY();
    return Math.toDegrees(Math.atan2(dy, dx));
}

function line2d.length():Number {
    var dx = getX();
    var dy = getY();
    return Math.sqrt(dx*dx + dy*dy);
}

function line2d.intersect(line:line2d):line2d {
    var p = intersection(line);
    var res = line2d { p1: p1, p2: p};
    return res;
}

function line2d.intersection(line:line2d):point2d {
    var x1 = p1.x;
    var y1 = p1.y;
    var x2 = p2.x;
    var y2 = p2.y;
    var x3 = line.p1.x;
    var y3 = line.p1.y;
    var x4 = line.p2.x;
    var y4 = line.p2.y;
    
    var n = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3));
    var d = ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
    return if (d == 0) then null
    else select
    
    point2d {
        x: x1 + ua*(x2-x1),
        y: y1 + ua*(y2-y1)
    } from ua in n / d;
}


public class Book extends CompositeNode {
    
    operation handleDrag(x:Number, y:Number);
    attribute pageHeight: Number;
    attribute pageWidth: Number;
    
    attribute pages:Node[];
    attribute page:Integer;
    
    attribute leftFront:Node?;
    attribute leftBack:Node?;
    attribute rightFront:Node?;
    attribute rightBack:Node?;
    
    
    attribute dragPage:Node?;
    
    attribute top: line2d;
    attribute bottom: line2d;
    attribute left: line2d;
    attribute right: line2d;
    attribute farRight: line2d;
    attribute mouse: line2d;
    
    
    attribute fold: line2d;
    attribute clipTop: line2d;
    attribute clipLeft: line2d;
    attribute clipRight: line2d;
    attribute clipBottom: line2d;
    
    attribute mouseLine: Line;
    attribute topLine: Line;
    attribute botLine: Line;
    attribute leftLine: Line;
    attribute rightLine: Line;
    attribute foldLine: Line;
    
    attribute dragTop: point2d;
    attribute dragBottom: point2d;
    
    attribute contentOrigin: point2d;
    attribute contentRotation: Number;
    
    attribute gradientX: Number;
    attribute gradientY: Number;
    attribute gradientWidth: Number;
    attribute gradientHeight: Number;
    attribute gradientRotation: Number;
    
    attribute flipTarget: point2d;
    
    attribute animator: Timeline;
    
    attribute autoMouseX:Number;
    attribute autoMouseY:Number;
    
    attribute targetPage: Integer;
    function finishDrag();
    function getPage(i:Number):Node;
    function stopAnimation();
}

attribute Book.page = 0;

function Book.getPage(i:Number) = pages[i];

attribute Book.leftFront = bind lazy this.getPage(page-1);
attribute Book.rightFront = bind lazy this.getPage(page);
attribute Book.rightBack = bind lazy this.getPage(page+1);
attribute Book.leftBack = bind lazy this.getPage(page-2);

operation Book.finishDrag() {
    page = targetPage;
    dragPage = null;
    autoMouseX = flipTarget.x;
    autoMouseY = flipTarget.y;
}

operation Book.stopAnimation() {
    animator.stop();
    finishDrag();
    
}


attribute Book.animator = Timeline {
    keyFrames:
    [at (0s) {
        this.autoMouseX => mouse.p2.x;
        this.autoMouseY => mouse.p2.y;
    },
    at (1s) {
        this.autoMouseX => flipTarget.x tween EASEBOTH;
        this.autoMouseY => flipTarget.y tween EASEBOTH;
        trigger { 
            println("targetPage = {targetPage}");
            finishDrag(); 
        }
    }]
};



trigger on Book.autoMouseY = newValue {
    println("drag to {autoMouseX} {autoMouseY}");
    handleDrag(autoMouseX.intValue(), autoMouseY.intValue());
}

attribute Book.flipTarget = point2d {};
attribute Book.mouse = new line2d;
attribute Book.top = new line2d {p1: point2d { x: 0, y: 0 }, p2: point2d {x: bind pageWidth, y : 0}};
attribute Book.bottom = new line2d {p1: {x: 0, y: bind pageHeight}, p2: {x: bind pageWidth, y: bind pageHeight}};
attribute Book.left = new line2d {p1: {x: 0, y: 0}, p2: {x: 0, y: bind pageHeight}};
attribute Book.right = new line2d {p1: {x: bind pageWidth, y: 0}, p2: {x: bind pageWidth, y: bind pageHeight}};
attribute Book.farRight = new line2d {p1: {x: bind pageWidth*2, y: 0}, p2: {x: bind pageWidth*2, y: bind pageHeight}};
attribute Book.fold = new line2d;

attribute Book.clipTop = new line2d;
attribute Book.clipLeft = new line2d;
attribute Book.clipRight = new line2d;
attribute Book.clipBottom = new line2d;

attribute Book.mouseLine = Line {
    x1: bind mouse.p1.x, y1: bind mouse.p1.y, x2: bind mouse.p2.x, y2: bind mouse.p2.y,
    stroke: green, 
    strokeWidth: 2
};
attribute Book.topLine = Line {
    stroke: bisque, strokeWidth: 1,
    x1: bind clipTop.p1.x, y1: bind clipTop.p1.y, x2: bind clipTop.p2.x, y2: bind clipTop.p2.y,
};

attribute Book.botLine = Line {
    stroke: yellow, strokeWidth: 1
    x1: bind clipBottom.p1.x, y1: bind clipBottom.p1.y, x2: bind clipBottom.p2.x, y2: bind clipBottom.p2.y,
    
};


attribute Book.leftLine = Line {
    stroke: black, strokeWidth: 2
    x1: bind clipLeft.p1.x, y1: bind clipLeft.p1.y, x2: bind clipLeft.p2.x, y2: bind clipLeft.p2.y,
    
};


attribute Book.rightLine = Line {
    stroke: purple, strokeWidth: 5
    x1: bind clipRight.p1.x, y1: bind clipRight.p1.y, x2: bind clipRight.p2.x, y2: bind clipRight.p2.y,
    
};

attribute Book.foldLine = Line {
    stroke:blue, strokeWidth: 2,
    x1: bind fold.p1.x, y1: bind fold.p1.y, x2: bind fold.p2.x, y2: bind fold.p2.y,
    
};

operation Book.handleDrag(x:Number, y:Number) {
    this.mouse.p2 = point2d {x: x, y: y };
    
    if (mouse.length().intValue() == 0) {
        clipTop = line2d;
        clipLeft = line2d;
        clipRight = line2d;
        clipBottom = line2d;
        
        return;
    } else {
    }
    // if we're dragging from top left then we're dragging top right corner of page
    // if from bottom left then bottom right corner of page
    var isTop = false;
    var isLeft = false;
    
    // We have to avoid tearing the page
    // To do that we'll limit the mouse drag as follows:
    
    
    if (mouse.getX1() == 0 and mouse.getY1() == 0) {
        // top left
        isTop = true;
        isLeft = true;
        dragTop = point2d { x: pageWidth, y: 0};
        dragBottom = point2d { x: pageWidth, y: pageHeight};
        if (mouse.getX2() > pageWidth) {
            flipTarget = point2d {x: pageWidth*2, y: 0};
            targetPage = page - 2;
        } else {
            targetPage = page;
            flipTarget = point2d {x: 0, y: 0};
        }
    } else if (mouse.getX1() == 0 and mouse.getY1() == pageHeight) {
        // bottom left
        
        isLeft = true;
        dragTop = point2d { x: pageWidth, y: pageHeight };
        dragBottom = point2d { x: pageWidth y: 0 };
        if (mouse.getX2() > pageWidth) {
            flipTarget = point2d {x: pageWidth*2, y: pageHeight};
            targetPage = page - 2;
        } else {
            targetPage = page;
            flipTarget = point2d {x: 0, y: pageHeight};
        }
    } else if (mouse.getX1() == pageWidth * 2 and mouse.getY1() == 0) {
        // top right
        isTop = true;
        
        dragTop = point2d { x: pageWidth, y: 0};
        dragBottom = point2d { x: pageWidth, y: pageHeight};
        if (mouse.getX2() > pageWidth) {
            flipTarget = point2d {x: pageWidth*2, y: 0};
            targetPage = page;
        } else {
            targetPage = page + 2;
            flipTarget = point2d {x: 0, y: 0};
        }
    } else {
        // bottom right
        dragTop = point2d { x: pageWidth, y: pageHeight };
        dragBottom = point2d { x: pageWidth y: 0 };
        if (mouse.getX2() > pageWidth) {
            flipTarget = point2d {x: pageWidth*2, y: pageHeight};
            targetPage = page;
        } else {
            targetPage = page + 2;
            flipTarget = point2d {x: 0, y: pageHeight};
        }
    }
    if (true) {
        var dragCenter = dragBottom;
        var radiusVector = line2d { p1: dragCenter, p2: mouse.getP2() };
        var radius = radiusVector.length();
        // max radius is diagonal of page
        var maxRadius = Math.sqrt(pageWidth*pageWidth + pageHeight*pageHeight);
        radiusVector.setLength(Math.min(radius, maxRadius));
        mouse.p2 = radiusVector.getP2();
    }
    if (true) {
        var dragCenter = dragTop;
        var radiusVector = line2d { p1: dragCenter, p2: mouse.getP2() };
        var radius = radiusVector.length();
        // max radius is width of page
        var maxRadius = pageWidth;
        radiusVector.setLength(Math.min(radius, maxRadius));
        mouse.p2 = radiusVector.getP2();
    }
    var c = mouse.center();
    
    // create the page fold perpendicular to the mouse line at its midpoint
    // the "set" operation takes a center point, length, and direction and yields the corresponding line
    fold = c.center(mouse.heading() + 90, mouse.length());
    println("fold={fold}");
    println("mouse={mouse.length()} c={c}");
    
    // find where the fold intersects the top and bottom and left and right of the page
    var ti = fold.intersection(top);
    var bi = fold.intersection(bottom); 
    
    var li = fold.intersection(left);                
    var ri = fold.intersection(right);
    
    // if the left is intersected before the top we'll use that
    var p1 = if (ti.x < 0) then li else ti;
    
    if (isLeft) {
        
        dragPage = leftBack;
        
        if (isTop) {
            // here we construct the clip region clockwise starting at the top left
            clipTop = line2d { p1: p1, p2: mouse.getP2()};
            // Top can't be wider than the page width
            clipTop.setLength(Math.min(this.clipTop.length(), pageWidth));
            // Cast a ray at a 90 degree angle the height of the page to create the right side
            clipRight = clipTop.p2.cast(clipTop.heading() + 90, pageHeight);
            // if it intersects the left side of the book below the top truncate it to intersection point
            var intersectsLeft = clipRight.intersect(left);
            if (intersectsLeft.getY2() > 0) {
                clipRight.setLength(Math.min(clipRight.length(), intersectsLeft.length()));
            }
            // bottom of the clip goes from the bottom right to the intersection point of left or bottom of the book whichever comes first
            var p2 = if bi.x < 0 then li else bi;
            clipBottom = line2d { p1: clipRight.getP2(), p2: p2};
            // construct the left side of the clip by simply connecting the bottom and top
            clipLeft = line2d { p1: clipBottom.getP2(), p2: clipTop.getP1()};
            // We're dragging the top right corner of the page in this case so position the content
            // -pageWidth from the right along the top
            contentOrigin = clipRight.p1.cast(clipTop.heading(), -pageWidth).getP2();
            contentRotation = clipTop.heading();
            
        } else  { // bottom
            clipBottom = line2d { p1: mouse.p2, p2: bi };
            clipBottom.setLength(Math.min(clipBottom.length(), pageWidth));
            clipLeft = line2d { p1: clipBottom.getP2(), p2: p1};
            
            clipRight = clipBottom.p1.cast(clipBottom.heading()+90, pageHeight);
            var intersectsLeft = clipRight.intersect(left);
            if (intersectsLeft.getY2() < pageHeight) {
                clipRight.setLength(Math.min(clipRight.length(), intersectsLeft.length()));
            }
            clipRight = clipRight.invert();
            clipTop = line2d { p1: clipLeft.getP2(), p2: clipRight.getP1()};
            contentOrigin = clipRight.getP2().cast(clipRight.invert().heading(), pageHeight).getP2().cast(clipBottom.heading(), pageWidth).getP2();
            contentRotation = clipBottom.heading() + 180;
            
        }
        
        gradientX = p1.x;
        gradientY = p1.y - pageHeight/2;
        gradientWidth = pageWidth;
        gradientHeight = pageHeight*3;
        gradientRotation = fold.heading() - 90;
        
    } else {
        
        dragPage = rightBack;
        
        if (isTop) {
            var li = fold.intersection(right);                
            var ri = fold.intersection(farRight);
            p1 = if (ti.y > 0) then ri else ti;
            var p2 = if (bi.x > pageWidth * 2) then li else bi;
            var p3 = if (bi.x > pageWidth *  2) then ri else bi;
            clipTop = line2d { p1: mouse.p2, p2: ti};
            clipLeft = this.clipTop.p1.cast(clipTop.heading()+90, pageHeight);
            var intersectsRight = clipLeft.intersect(farRight);
            if (intersectsRight.p2.y > 0) {
                clipLeft.setLength(Math.min(clipLeft.length(), intersectsRight.length()));
            }
            clipLeft = clipLeft.invert();
            clipRight =  line2d { p1: clipTop.getP2(), p2: p3};
            clipBottom = line2d { p1: p3, p2: clipLeft.getP1() };
            contentOrigin = clipLeft.getP2();
            contentRotation = clipTop.heading();
            
        } else { // bottom
            var li = fold.intersection(right);                
            var ri = fold.intersection(farRight);
            p1 = if (ti.x > pageWidth *2) then ri else ti;
            var p2 = if (ri.y >= 0) then ri else bi;
            clipBottom = line2d { p2: mouse.p2, p1: bi };
            clipBottom.setLength(Math.min(clipBottom.length(), pageWidth));
            clipLeft = clipBottom.p2.cast(clipBottom.heading()+90, pageHeight);
            var intersectsRight = clipLeft.intersect(farRight);
            if (intersectsRight.p2.y < pageHeight) {
                clipLeft.setLength(Math.min(clipLeft.length(), intersectsRight.length()));
            }
            clipTop = line2d { p1: clipLeft.getP2(), p2: p1};
            clipRight = line2d { p1: p1, p2: clipBottom.getP1() }; 
            contentOrigin = clipLeft.getP1().cast(clipLeft.heading(), pageHeight).getP2();
            contentRotation = clipBottom.heading()+180;
        }
        
        gradientX = p1.x - pageWidth*.4;
        gradientY = p1.y- pageHeight/2;
        gradientWidth = pageWidth;
        gradientHeight = pageHeight*3;
        gradientRotation = fold.heading()+90;
    }
}

function Book.composeNode():Node {
    return Group {
        content:
        [Rect {
            height: bind pageHeight
            width: bind pageWidth
            
            fill: LinearGradient {
                stops:
                [Stop {
                    offset: 0
                    color: new Color(0, 0, 0, 1)
                },
                Stop {
                    offset: 0.7
                    color: new Color(0, 0, 0, 1)
                },
                Stop {
                    offset: 0.9
                    color: new Color(1, 1, 1, 1)
                }]
            }
            //stroke: white
            //strokeWidth: 10
            fill: new Color(0, 0, 0, 0)
        },
        Rect {
            //stroke: white
            //strokeWidth: 10
            x: bind pageWidth
            height: bind pageHeight
            width: bind pageWidth
            fill: LinearGradient {
                stops:
                [Stop {
                    offset: 0
                    color: new Color(0, 0, 0, 0)
                },
                Stop {
                    offset: 0.3
                    color: new Color(1, 1, 1, 1)
                },
                Stop {
                    offset: 1
                    color: new Color(1, 1, 1, 1)
                }]
            }
            fill: new Color(0, 0, 0, 0)
        },
        Group {
            
            
            content:
            [Rect {
                
                fill: new Color(0, 0, 0, 0)
                // top left
                width: bind pageWidth
                height: bind pageHeight
                
                onMousePressed: operation(e) {
                    stopAnimation();
                    if (e.localY > pageHeight/2) {
                        this.mouse.p1 = point2d { x:0, y: pageHeight};
                    } else {
                        this.mouse.p1 = point2d {x: 0, y: 0} ;
                    }
                    
                    handleDrag(e.localX, e.localY);
                }
                
                onMouseDragged: operation(e) {
                    handleDrag(e.localX, e.localY);
                }
                
                onMouseReleased: operation(e) {
                    animator.start();
                }
                
                onMouseClicked: operation(e) {
                    mouse.p1 = point2d {x: 0, y: 0} ;
                    mouse.p2 = point2d { x: e.localX, y: e.localY };
                    flipTarget = point2d { x: 0, y: pageWidth * 2 };
                    animator.start();
                }
            },
            
            Rect {
                fill: new Color(0, 0, 0, 0)
                // top left
                width: bind pageWidth
                height: bind pageHeight
                x: pageWidth
                onMousePressed: operation(e) {
                    stopAnimation();
                    if (e.localY > pageHeight/2) {
                        this.mouse.p1 = point2d { x:pageWidth*2, y: pageHeight};
                    } else {
                        this.mouse.p1 = point2d {x: pageWidth*2, y: 0} ;
                    }
                    handleDrag(e.localX, e.localY);
                }
                onMouseDragged: operation(e) {
                    
                    handleDrag(e.localX, e.localY);
                    
                }
                onMouseReleased: operation(e) {
                    animator.start();
                }
            }]
        },
        
        Group {
            
            content: 
            [Group { content: bind pages[p|indexof p <= page-3 and indexof p % 2 != 0]},
            Clip {
                var shape = bind if dragPage == leftBack then
                Path {
                    d: bind
                    [MoveTo { x: bind clipLeft.p2.x, y: bind clipLeft.p2.y},
                    LineTo {x: 0, y: 0},
                    LineTo {x: bind pageWidth, y: 0},
                    LineTo {x: bind pageWidth, y: bind pageHeight},
                    if (clipLeft.p1.y < pageHeight) then LineTo {x: 0, y: pageHeight} else null,
                    LineTo {x: bind clipLeft.p1.x, y: bind clipLeft.p1.y},
                    
                    ClosePath]
                    //fill: green
                    //stroke: yellow
                    strokeWidth: 6
                } else null
                shape: bind shape
                content: Group { content: bind [leftFront] }
                
            },
            Rect {
                opacity: 0.5
                height: bind pageHeight
                width: bind pageWidth
                fill: LinearGradient {
                    stops:
                    
                    [Stop {
                        offset: 0.6
                        color: new Color(1, 1, 1, 0)
                    },
                    Stop {
                        offset: 1
                        color: new Color(0, 0, 0, .3)
                    }]
                }
            }]
        },
        Group {
            
            content: 
            [Group {
                var: g
                id: "rightStack"
                transform: translate(pageWidth, 0)
                var rightStack = bind pages[p|indexof p >= page + 2 and indexof p % 2 == 0]
                
                trigger on (insert v into g.content) {
                    println("insert {indexof v} {v.id}");
                }
                trigger on (delete v from g.content) {
                    println("delete {indexof v} {v.id}");
                    for (i in g.content) {
                        println("{i.id}");
                    }
                }
                trigger on (newValue[oldvalue] = g.content) {
                    println("replace {indexof oldvalue}");
                }
                content: bind rightStack
            },
            Clip {
                var shape = bind if dragPage == rightBack then Polygon {
                    
                    points: bind [pageWidth, 0, pageWidth*2, 0, clipRight.p1.x, clipRight.p1.y,
                    clipRight.p2.x, clipRight.p2.y, 
                    if (clipRight.p2.y < pageHeight) then [pageWidth*2, pageHeight] else [], pageWidth, pageHeight, pageWidth, 0]
                    
                } else null
                
                shape: bind shape
                //content: Group {transform: translate(pageWidth, 0) content: bind rightFront }
                
            },
            Rect {
                
                x: bind pageWidth
                height: bind pageHeight
                width: bind pageWidth
                opacity: 0.5
                fill: LinearGradient {
                    stops:
                    [Stop {
                        offset: 0
                        color: new Color(1, 1, 1, 0.3)
                    },
                    Stop {
                        offset: 0.4
                        color: new Color(0, 0, 0, 0)
                    }]
                }
            }]
            
        },
        Group {
            
            var clip = Clip {
                //antialias: true
                shape: Polygon {
                    points: bind 
                    [clipTop.p1.x, 
                    clipTop.p1.y,
                    clipTop.p2.x,
                    clipTop.p2.y,
                    
                    clipBottom.p1.x, 
                    clipBottom.p1.y,
                    
                    clipLeft.p1.x,
                    clipLeft.p1.y]
                }
            }
            clip:  clip      
            content:
            [Group {     
                content: bind dragPage
                
                transform: bind [translate(contentOrigin.x, contentOrigin.y), rotate(contentRotation, 0, 0)]
            },
            Rect {
                opacity: 0.5
                visible: bind dragPage != null
                width: bind gradientWidth
                height: bind gradientHeight
                transform: bind [rotate(gradientRotation, 0, 0), translate(gradientX, gradientY)]
                fill: LinearGradient {
                    stops:
                    [Stop {
                        offset: 0
                        color: new Color(0, 0, 0, 0)
                    },
                    Stop {
                        offset: 0.4
                        color: new Color(0, 0, 0, .2)
                    }]
                }
            }]
        },
        Path {
            strokeWidth: 2
            stroke: new Color(1, 1, 1, 1)
            d:
            [MoveTo {x: bind clipTop.getX1(), y: bind clipTop.getY1()},
            LineTo {x: bind clipTop.p2.x, y: bind clipTop.p2.y},
            LineTo {x: bind clipRight.p2.x, y: bind clipRight.p2.y},
            LineTo {x: bind clipBottom.p2.x, y: bind clipBottom.p2.y},
            ClosePath]
            
        },
        Group {
            visible: true
            content:
            
            [
            Circle { radius: 10, cx: bind clipTop.p1.x, cy: bind clipTop.p1.y, fill: yellow},
            Circle { radius: 10, cx: bind botLine.x2, cy: bind botLine.y2, fill: pink},
            Circle { radius: 10, cx: bind botLine.x1, cy: bind botLine.y1, fill: cyan},
            Circle { cx: pageWidth, cy: pageHeight, strokeDashArray: [5, 5]
                radius: Math.sqrt(pageWidth*pageWidth + pageHeight*pageHeight) stroke: blue},
            Circle { cx: pageWidth, cy: 0, radius: pageWidth, stroke: green,
                strokeDashArray: [5, 5]
            },
            mouseLine,
            foldLine,
            topLine,
            botLine,
            rightLine,
            leftLine]
        }]
    };
}





Frame {
    visible: true
    height: 650, width: 1050
    content: Canvas {
        background: gray
        content: Book {
            var pageHeight = 500
            var pageWidth = 400
            transform: [translate(100, 100)]
            pageHeight: pageHeight
            pageWidth: pageWidth
            var colors:Color[] = [red, blue, green, black, yellow, orange]
            pages:
            foreach (i in [1..5]) 
            Group {
                id: "{i}"
                content:
                [Subtract {
                    stroke: white
                    strokeWidth: 2
                    //opacity: 0.5
                    fill: colors[(i-1) % sizeof colors]
                    
                    shape1: Rect {
                        height: bind pageHeight
                        width: bind pageWidth                   
                    },
                    shape2: Star {
                        startAngle: i * 10
                        transform: translate(pageWidth/2, pageHeight/2)
                        points: 5
                        rin: 50
                        rout: 100
                        
                    },
                },
                VBox {
                    content:
                    [Text {
                        x: 120
                        y: 360
                        font: Font { size: 50 }
                        content: bind "Page {indexof i+1 format as <<0>>}"
                        fill: white
                    },
                    View {
                        content: Button { text: "Click Me" }
                    }
                    ]
                }]
            }
            
        }
    }
}
