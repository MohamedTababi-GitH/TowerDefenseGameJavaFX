package org.example.javatowerdefensegame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Tower extends Rectangle {
    private String type;
    private double fireRate; // Time between shots in seconds
    private double damage;
    private double timeSinceLastShot = 0;

    public Tower(double x, double y, double size, String type, Color color, double fireRate, double damage) {
        super(x, y, size, size);
        this.type = type;
        this.fireRate = fireRate;
        this.damage = damage;
        this.setFill(color);
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
        // Create a new projectile aimed at the target
        double startX = getX() + getWidth() / 2;
        double startY = getY() + getHeight() / 2;
        double targetX = target.getX() +50 + target.getWidth() / 2;
        double targetY = target.getY() +50 + target.getHeight() / 2;

        return new Projectile(startX, startY, targetX, targetY, 4.0, Color.RED, boardWidth, boardHeight);
    }
}
