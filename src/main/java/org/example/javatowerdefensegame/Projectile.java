package org.example.javatowerdefensegame;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile {
    private double speed;
    private double directionX;
    private double directionY;
    private double boardWidth;
    private double boardHeight;
    private ImageView imageView;
    private int damage;  // Add damage property

    // Updated constructor to include size and damage parameters
    public Projectile(double startX, double startY, double targetX, double targetY, double speed, Image img, double boardWidth, double boardHeight, double size, int damage) {
        this.speed = speed;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.damage = damage;  // Initialize damage

        // Calculate direction based on target
        double deltaX = targetX - startX;
        double deltaY = targetY - startY;
        double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        this.directionX = deltaX / length;
        this.directionY = deltaY / length;

        // Create an ImageView for the projectile
        this.imageView = new ImageView(img);
        this.imageView.setX(startX);
        this.imageView.setY(startY);
        this.imageView.setFitWidth(size); // Set projectile width
        this.imageView.setFitHeight(size); // Set projectile height
        this.imageView.setPreserveRatio(true);
    }

    public void move() {
        imageView.setX(imageView.getX() + directionX * speed);
        imageView.setY(imageView.getY() + directionY * speed);

        // Check if projectile is out of bounds
        if (imageView.getX() < 0 || imageView.getX() > boardWidth || imageView.getY() < 0 || imageView.getY() > boardHeight) {
            imageView.setVisible(false);  // Mark as invisible
        }
    }

    public boolean hasHitTarget() {
        return !imageView.isVisible(); // Indicates if projectile is out of bounds or hit a target
    }

    public ImageView getImageView() {
        return imageView;
    }

    // Method to get the bounds of the projectile
    public Bounds getBounds() {
        return imageView.getBoundsInParent();
    }

    // New method to get the damage value
    public int getDamage() {
        return damage;
    }
}
