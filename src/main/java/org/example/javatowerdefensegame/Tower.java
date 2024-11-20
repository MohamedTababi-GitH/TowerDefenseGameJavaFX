package org.example.javatowerdefensegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Tower extends Rectangle {
    private String type;
    private double fireRate; // Time between shots in seconds
    private int damage;
    private double timeSinceLastShot = 0;
    private ImageView imageView;
    private Image projectileImage; // Specific image for the projectile

    public Tower(double x, double y, double size, String type, Image img, Image projectileImg, double fireRate, int damage) {
        super(x, y, size, size);
        this.type = type;
        this.fireRate = fireRate;
        this.damage = damage;
        this.projectileImage = projectileImg; // Store the projectile image

        // Create an ImageView for the tower image and set its size and position
        this.imageView = new ImageView(img);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);

        // Set the position of the ImageView
        imageView.setX(x);
        imageView.setY(y);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void update(double elapsedTime, List<Enemy> enemies, List<Projectile> projectiles, double boardWidth, double boardHeight) {
        timeSinceLastShot += elapsedTime;
        if (timeSinceLastShot >= fireRate) {
            Enemy target = findNearestEnemy(enemies);
            if (target != null) {
                Projectile projectile = shoot(target, boardWidth, boardHeight);
                projectiles.add(projectile);  // Add the projectile to the list
                timeSinceLastShot = 0;
            }
        }
    }

    private Enemy findNearestEnemy(List<Enemy> enemies) {
        Enemy nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemies) {
            double distance = Math.hypot(enemy.getX() - getX(), enemy.getY() - getY());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = enemy;
            }
        }

        return nearest;
    }

    private Projectile shoot(Enemy target, double boardWidth, double boardHeight) {
        // Get the tower's center position
        double startX = getX() + getWidth() / 2;
        double startY = getY() + getHeight() / 2;

        // Calculate the distance between the tower and the target's current position
        double distance = Math.hypot(target.getX() - startX, target.getY() - startY);

        // Projectile speed in units per second (e.g., pixels per second)
        double projectileSpeed = 4.0; // You may want to adjust this value if it's too slow or fast

        // Estimate how long the projectile will take to reach the target (time = distance / speed)
        double travelTime = distance / projectileSpeed;

        // Predict the enemy's future position based on its velocity and travel time
        double predictedX = target.getX() + target.getVelocityX() * travelTime;
        double predictedY = target.getY() + target.getVelocityY() * travelTime;

        // Create a projectile aimed at the predicted future position
        return new Projectile(startX-30, startY, predictedX, predictedY, projectileSpeed, projectileImage, boardWidth, boardHeight, 28, damage);
    }


}
