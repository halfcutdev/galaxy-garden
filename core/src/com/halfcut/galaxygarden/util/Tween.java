package com.halfcut.galaxygarden.util;

/**
 * @author halfcutdev
 * @date 12/09/17
 */
public class Tween {

    static public float cubicIn(float t, float b, float c, float d) {
        return (t==0) ? b : c * (float) Math.pow(2, 10 * (t / d - 1)) + b;
    }

    static public float cubicOut(float t, float b, float c, float d) {
        return (t==d) ? b + c : c * (-(float) Math.pow(2, -10 * t/d) + 1) + b;
    }

}
