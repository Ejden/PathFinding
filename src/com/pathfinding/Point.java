package com.pathfinding;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

class Point extends Rectangle {
    double f;
    double g;
    double h;
    int x;
    int y;
    boolean wall;
    List<Point> neighbours = new ArrayList<>();
    Point previous = null;

    Point(int x, int y) {
        this.setWidth(Main.SIZE);
        this.setHeight(Main.SIZE);
        this.x = x;
        this.y = y;
        this.wall = Math.random() < 0.30;
        if (this.wall) {
            this.setFill(Color.BLACK);
        } else {
            this.setFill(Color.LIGHTBLUE);
        }
    }

    void addNeighbours(List<List<Point>> grid) {
        if(this.x > 0) {
            neighbours.add(grid.get(this.x-1).get(this.y));
        }
        if(this.x < Main.WIDTH-1) {
            neighbours.add(grid.get(this.x+1).get(this.y));
        }
        if(this.y > 0) {
            neighbours.add(grid.get(this.x).get(this.y-1));
        }
        if(this.y < Main.HEIGHT-1) {
            neighbours.add(grid.get(this.x).get(this.y+1));
        }
        if(this.x > 0 && this.y > 0) {
            neighbours.add(grid.get(this.x-1).get(this.y-1));
        }
        if(this.x > 0 && this.y < Main.HEIGHT-1) {
            neighbours.add(grid.get(this.x-1).get(this.y+1));
        }
        if(this.x < Main.WIDTH-1 && this.y > 0) {
            neighbours.add(grid.get(this.x+1).get(this.y-1));
        }
        if(this.x < Main.WIDTH-1 && this.y < Main.HEIGHT-1) {
            neighbours.add(grid.get(this.x+1).get(this.y+1));
        }
    }
}
