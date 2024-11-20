package org.example.javatowerdefensegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends ImageView {
    private double speed;
    private int hp;
    private int goldReward; // Gold reward for defeating this enemy
    private int currentPathIndex;
    private double[][] path;
    private boolean reachedEnd = false;

    // Fields for animation
    private Image zombie;
    private Image zombieWalk;
    private boolean walkingFrame = false;
    private int animationCounter = 0; // Counter to control the animation speed

    // Updated constructor to accept the gold reward
    public Enemy(double startX, double startY, double speed, double[][] path, Image zombie, Image zombieWalk, int hp, int goldReward) {
        super(zombie);
        this.speed = speed;
        this.path = path;
        this.zombie = zombie;
        this.zombieWalk = zombieWalk;
        this.currentPathIndex = 0;
        this.hp = hp;
        this.goldReward = goldReward;
        setFitWidth(64);  // Set the width of the image
        setFitHeight(64); // Set the height of the image
        setPreserveRatio(true);
        setX(startX);
        setY(startY);
    }

    // Get the gold reward
    public int getGoldReward() {
        return goldReward;
    }

    // Method to resize the image
    public void resize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public double getSpeed() {
        return speed;
    }

    // Calculate the X velocity based on current path direction
    public double getVelocityX() {
        if (currentPathIndex < path.length) {
            double targetX = path[currentPathIndex][0];
            double dx = targetX - getX();
            double distance = Math.sqrt(dx * dx + (path[currentPathIndex][1] - getY()) * (path[currentPathIndex][1] - getY()));
            return (dx / distance) * speed;
        }
        return 0;
    }

    // Calculate the Y velocity based on current path direction
    public double getVelocityY() {
        if (currentPathIndex < path.length) {
            double targetY = path[currentPathIndex][1];
            double dy = targetY - getY();
            double distance = Math.sqrt((path[currentPathIndex][0] - getX()) * (path[currentPathIndex][0] - getX()) + dy * dy);
            return (dy / distance) * speed;
        }
        return 0;
    }
}
