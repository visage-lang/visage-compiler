/*
 * BallBase.fx
 *
 * Java port by Richar Bair, JavaFX port by Alexey Gavrilov
 *
 * License: The code is released under Creative Commons Attribution 2.5 License
 * (http://creativecommons.org/licenses/by/2.5/)
 */

package javafxballs;

import java.lang.Math;
import java.awt.Insets;

private class Model {
    attribute walls:Insets = new Insets(0, 0, 300, 500);
    attribute elastity:Number = -.02;
    attribute ballRadius:Number = 26;
    attribute maxSpeed: Number = 3.0;
}

public class BallBase {
    attribute model:Model = new Model();
    attribute _x:Number = 0;
    attribute _y:Number = 0;
    attribute _vx:Number = 0;
    attribute _vy:Number = 0;
    attribute _r:Number = 0;
    attribute _d:Number = 0;
    attribute _d2:Number = 0;
    
    function initialize() {
        //default provisioning
        // default provisioning
        this._x = (model.walls.right - model.walls.left - 2*model.ballRadius)*Math.random();
        this._y = (model.walls.bottom - model.walls.top - 2*model.ballRadius)*Math.random();
        this._vx = 2*model.maxSpeed*Math.random() - model.maxSpeed;
        this._vy = 2*model.maxSpeed*Math.random() - model.maxSpeed;
        this._r = model.ballRadius; // d = 52 px
        this._d = 2*this._r;
        this._d2 = this._d*this._d;
    };
    
    function move() {
        //TODO: this._x += this._vx;
        //TODO: this._y += this._vy;
        this._x = this._x + this._vx;
        this._y = this._y + this._vy;
        // walls collisons

        // left
        if (this._x < model.walls.left and this._vx<0) {
            //this._vx += (this._x - walls.left)*elastity;
            this._vx = -this._vx;
        }
        // top
        if (this._y < model.walls.top and this._vy<0) {
            //this._vy += (this._y - walls.top)*elastity;
            this._vy = -this._vy;
        }
        // left
        if (this._x > model.walls.right - this._d and this._vx>0) {
            //this._vx += (this._x - walls.right + this._d)*elastity;
            this._vx = -this._vx;
        }
        // top
        if (this._y > model.walls.bottom - this._d and this._vy>0) {
            //this._vy += (this._y - walls.bottom + this._d)*elastity;
            this._vy = -this._vy;
        }
    }; 
    
    function doCollide(b:BallBase):Boolean {
        // calculate some vectors
        var dx = this._x - b._x;
        var dy = this._y - b._y;
        var dvx = this._vx - b._vx;
        var dvy = this._vy - b._vy;
        var distance2 = dx*dx + dy*dy;

        if (Math.abs(dx) > this._d or Math.abs(dy) > this._d)
        {
            return false;
        }
        if (distance2 > this._d2)
        {
            return false;
        }

        // make absolutely elastic collision
        var mag = dvx*dx + dvy*dy;

        // test that balls move towards each other
        if (mag > 0)
        {
            return false;
        }

        //TODO: mag /= distance2;
        mag = mag / distance2;

        var delta_vx = dx*mag;
        var delta_vy = dy*mag;

        //TODO: this._vx -= delta_vx;
        //TODO: this._vy -= delta_vy;
        this._vx = this._vx - delta_vx;
        this._vy = this._vy - delta_vy;

        //TODO: b._vx += delta_vx;
        //TODO: b._vy += delta_vy;
        b._vx = b._vx + delta_vx;
        b._vy = b._vy + delta_vy;

        return true;
    }; 
}

