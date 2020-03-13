package com.pathfinding;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    static final int WIDTH = 100;
    static final int HEIGHT = 100;
    static final int SIZE = 6;
    private static List<List<Point>> points = new ArrayList<>(WIDTH);
    private static List<Point> openSet = new ArrayList<>();
    private static List<Point> closedSet = new ArrayList<>();
    private static GridPane grid;
    private static Point start;
    private static Point end;
    private static List<Point> path = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Path Finding");
        grid = new GridPane();
        createGrid();

        start = points.get(0).get(0);
        end = points.get(WIDTH-1).get(HEIGHT-1);

        openSet.add(start);

        AnimationTimer animationTimer = getAnimator();
        animationTimer.start();

        Scene scene = new Scene(grid, WIDTH*SIZE,HEIGHT*SIZE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static void createGrid() {
        for(int i=0; i<WIDTH; i++) {
            points.add(new ArrayList<>(HEIGHT));
        }

        for(int x=0; x<WIDTH; x++) {
            for(int y=0; y<HEIGHT; y++) {
                Point point = new Point(x, y);
                point.setOnMouseClicked(e -> {
                    point.wall = true;
                    point.setFill(Color.BLACK);
                });
                points.get(x).add(y, point);
                grid.add(point, x, y);
            }
        }

        //Making sure start and end are not wall
        Point start = points.get(0).get(0);
        Point end = points.get(WIDTH-1).get(HEIGHT-1);
        if(start.wall) {
            start.wall = false;
            start.setFill(Color.LIGHTBLUE);
        }
        if(end.wall) {
            end.wall = false;
            end.setFill(Color.LIGHTBLUE);
        }

        //Adding neighbours to Points
        for(List<Point> list : points) {
            for(Point point : list) {
                point.addNeighbours(points);
            }
        }
    }

    private AnimationTimer getAnimator() {
        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(openSet.isEmpty()) {
                    System.err.println("No solution");
                    stop();
                } else {
                    int winner = 0;
                    for(int i=0; i<openSet.size(); i++) {
                        if(openSet.get(i).f < openSet.get(winner).f) {
                            winner = i;
                        }
                    }

                    Point current = openSet.get(winner);

                    if(current == end) {
                        System.out.println("DONE");
                        stop();
                        return;
                    }

                    List<Point> neighbours = current.neighbours;
                    for(Point neighbour : neighbours) {
                        if(!closedSet.contains(neighbour) && !neighbour.wall) {
                            double tempG = current.g + 1;

                            if(openSet.contains(neighbour)) {
                                if(tempG < neighbour.g) {
                                    neighbour.g = tempG;
                                }
                            } else {
                                neighbour.g = tempG;
                                openSet.add(neighbour);
                            }

                            neighbour.h = heuristic(neighbour, end);
                            neighbour.f = neighbour.g + neighbour.h;
                            neighbour.previous = current;
                        }
                    }

                    //DRAW
                    colorPoints();

                    Point temp = current;
                    path.add(temp);
                    while(temp.previous != null) {
                        path.add(temp.previous);
                        temp = temp.previous;
                    }
                    printPath(Color.YELLOW);
                    path.clear();
                    openSet.remove(current);
                    closedSet.add(current);
                }
            }
        };
    }

    private static double heuristic(Point a, Point b) {
        double distX = Math.pow(b.x - a.x, 2);
        double distY = Math.pow(b.y - a.y, 2);
        return Math.sqrt(distX + distY);
    }

    private static void colorPoints() {
        for(Point point : closedSet) {
            if(point.wall){
                continue;
            }
            point.setFill(Color.INDIANRED);
        }

        for(Point point : openSet) {
            if(point.wall) {
                continue;
            }
            point.setFill(Color.SPRINGGREEN);
        }
    }

    private void printPath(Color color) {
        for(Point point : path) {
            if(point.wall){
                continue;
            }
            point.setFill(color);
        }
    }
}
