package com.halfcut.galaxygarden.util;

import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.level.map.TileMap;

import java.util.ArrayList;

/**
 * @author halfcutdev
 * @since 16/09/2017
 */
public class Raycaster {

    static public ArrayList<Vector2> line(Vector2 v1, Vector2 v2) {
        int x = (int) v1.x;
        int y = (int) v1.y;
        int x2 = (int) v2.x;
        int y2 = (int) v2.y;
        ArrayList<Vector2> result = new ArrayList<Vector2>();

        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            result.add(new Vector2(x, y));
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
        return result;
    }

    static public Vector2 getEndPoint(Vector2 v1, Vector2 v2, TileMap tileMap) {
        ArrayList<Vector2> line = line(v1, v2);
        for(Vector2 point : line) {
            if(tileMap.isBlocked(point.x, point.y)) return point;
        }
        return v2;
    }

}
