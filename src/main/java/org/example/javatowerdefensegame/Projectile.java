package org.example.javatowerdefensegame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile extends Circle {
    private double speed;
    private double directionX;
    private double directionY;
    private double boardWidth;
    private double boardHeight;

    public Projectile(double startX, double startY, double targetX, double targetY, double speed, Color color, double boardWidth, double boardHeight) {
        super(startX, startY, 5); // Radius of 5, adjust as needed
        this.speed = speed;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // Calculate direction based on target
        double deltaX = targetX - startX;
        double deltaY = targetY - startY;
        double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        this.directionX = deltaX / length;
        this.directionY = deltaY / length;

        this.setFill(color);
    }

    public void move() {
        setCenterX(getCenterX() + directionX * speed);
        setCenterY(getCenterY() + directionY * speed);

        // Check if projectile is out of bounds
        if (getCenterX() < 0 || getCenterX() > boardWidth || getCenterY() < 0 || getCenterY() > boardHeight) {
            setVisible(false);  // Mark as invisible, or you can use another method to mark for removal
        }
    }

    public boolean hasHitTarget() {
        return !isVisible(); // Indicates if projectile is out of bounds or hit a target
    }
}
