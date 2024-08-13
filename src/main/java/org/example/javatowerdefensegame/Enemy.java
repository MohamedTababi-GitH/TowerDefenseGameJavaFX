package org.example.javatowerdefensegame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Enemy extends Rectangle {
    private double health;
    private double speed;
    private int currentPathIndex;
    private double[][] path;
    private boolean reachedEnd = false;

    public double getSpeed() {
        return speed;
    }

    public Enemy(double startX, double startY, double speed, double[][] path) {
        super(20, 20, Color.RED); // Example size and color
        this.speed = speed;
        this.path = path;
        this.currentPathIndex = 0;
        setX(startX);
        setY(startY);
    }

    public void move() {
        if (currentPathIndex < path.length) {
            double targetX = path[currentPathIndex][0];
            double targetY = path[currentPathIndex][1];

            double dx = targetX - getX();
            double dy = targetY - getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < speed) {
                // Reached the target position
                setX(targetX);
                setY(targetY);
                currentPathIndex++;

                if (currentPathIndex >= path.length) {
                    reachedEnd = true; // Mark as reached the end of the path
                }
            } else {
                // Move towards the target position
                setX(getX() + dx / distance * speed);
                setY(getY() + dy / distance * speed);
            }
        }
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }
}
