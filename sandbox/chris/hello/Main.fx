package hello;
import javax.swing.*;
import java.lang.System;
import java.lang.Math;
import java.lang.Runnable;
import java.awt.Insets;
import java.awt.event.*;

class Ball {
    static attribute elasticity = -0.2;
    static attribute maxSpeed = 3.0;
    static attribute ballRadius = 26;
    static attribute walls = new Insets(0, 0, 300, 500);
 
    attribute x: Number;
    attribute y: Number;
    attribute vx: Number;
    attribute vy: Number;
    attribute r: Number;
    attribute d: Number;
    attribute d2: Number;
 
    // What order are attributes initialized in? Not knowing, I do it in init {}

    init {
	x = walls.right - walls.left * 2* ballRadius * Math.random();;
	y = walls.bottom - walls.top * 2* ballRadius * Math.random();
	vx = 2 * maxSpeed * Math.random() - maxSpeed;;
	vy =  2 * maxSpeed * Math.random() - maxSpeed;;
	r = ballRadius;
	d = 2 * r;
	d2 = d * d;
    }

    public function move(): Void {
	x = x + vx;
	y = y + vy;
	if (x < walls.left and vx < 0) {
	    vx = -vx;
	}
	if (y < walls.top and vy < 0) {
	    vy = -vy;
	}
	if (x > walls.right - d and  vx > 0) {
	    vx = -vx;
	}
	if (y > walls.bottom - d and vy > 0) {
	    vy = -vy;
	}
    }

    function doCollide(ball:Ball):Boolean {
	var dx = x - ball.x;
	var dy = y - ball.y;
	var dvx = vx - ball.vx;
	var dvy = vy - ball.vy;
	var distance2 = dx*dx + dy*dy;
	if (Math.abs(dx) > d or Math.abs(dy) > d) {
	    return false;
	}
	if (distance2 > d2) {
	    return false;
	}
	var mag = dvx*dx + dvy*dy;
	if (mag > 0) {
	    return false;
	}
	mag = mag / distance2;
	var delta_vx = dx*mag;
	var delta_vy = dy*mag;
	vx = vx - delta_vx;
	vy = vy - delta_vy;
	ball.vx = ball.vx + delta_vx;
	ball.vy = ball.vy + delta_vy;
	return true;
    }
}

class BallModel {
    attribute F: Number;
    attribute lastF: Number;
    attribute lastTime: Number;

    attribute ballImage =
	Image { url: "http://bubblemark.com/assets/ball.png"};
    attribute ballCount = 16;
    attribute balls = bind foreach (i in [1..ballCount]) new Ball;
    attribute timer:Timer;
    attribute labels: SimpleLabel[] = 
	bind foreach (ball in balls) SimpleLabel {
		x: bind ball.x
		y: bind ball.y
                icon: ballImage
	};
    attribute listener:ActionListener = ActionListener {
       public function actionPerformed(event:ActionEvent):Void {
	   timerEvent();
       }
    }

    public attribute fps: Number;

    function updateFPS():Void {
	var currTime:Number = System.currentTimeMillis();
	var delta_t = currTime - lastTime;
	if (delta_t >= 2000) {
	    fps = ((F - lastF)/delta_t) * 1000.0;
	    System.out.println("fps={fps}");
	    lastF = F;
	    lastTime = currTime;
	}
    }


    function timerEvent(): Void {
	var n = ballCount -1;
	foreach (i in [0..n]) {
	    balls[i].move();
	}
	foreach (i in [0..n-1], j in [i+1..n]) {
	    balls[i].doCollide(balls[j]);
	}
	F = F + 1;
	updateFPS();
    }


    function start() {
	F = 0;
	lastTime = System.currentTimeMillis();
	timer = new Timer(5, listener);	
	timer.setRepeats(true);
	timer.start();
    }

    function stop() {
	if (timer <> null) {
	    timer.stop();
	}
    }

}

SwingUtilities.invokeLater(Runnable {
	public function run() {
	    var model = new BallModel;
	    model.start();
	    var p = Panel {
		height: 500
		width: 800
		content: // bind 
                [//model.labels, // <- doesn't compile
	        foreach (label in model.labels) label as Widget,
		SimpleLabel {
		    width: 100
		    text: bind "FPS: {%f model.fps}" 
                }]
	    }
	    var frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setTitle("Compiled JavaFX Balls");
	    frame.getContentPane().add(p.getComponent());
	    frame.setSize(525, 325);
	    frame.setVisible(true);
	}
});

