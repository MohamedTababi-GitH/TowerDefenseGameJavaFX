package org.example.javatowerdefensegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends ImageView {
    private double speed;
    private int currentPathIndex;
    private double[][] path;
    private boolean reachedEnd = false;

    // Add the new fields for animation
    private Image zombie;
    private Image zombieWalk;
    private boolean walkingFrame = false;
    private int animationCounter = 0; // Counter to control the animation speed

    // Updated constructor to accept both images
    public Enemy(double startX, double startY, double speed, double[][] path, Image zombie, Image zombieWalk) {
        super(zombie);
        this.speed = speed;
        this.path = path;
        this.zombie = zombie;
        this.zombieWalk = zombieWalk;
        this.currentPathIndex = 0;

        setFitWidth(64);  // Set the width of the image (optional, depending on your needs)
        setFitHeight(64); // Set the height of the image (optional, depending on your needs)
        setPreserveRatio(true);
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

            // Update the animation frame
            updateAnimation();
        }
    }

    // Method to toggle the images to simulate walking animation
    private void updateAnimation() {
        animationCounter++;

        // Change image every few frames (adjust the value to control animation speed)
        if (animationCounter % 45 == 0) {
            walkingFrame = !walkingFrame; // Toggle the walking frame
            setImage(walkingFrame ? zombieWalk : zombie);
        }
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }
}
