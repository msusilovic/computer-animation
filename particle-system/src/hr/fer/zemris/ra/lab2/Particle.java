package hr.fer.zemris.ra.lab2;

import java.util.Random;

public class Particle {

    private int x;
    private int y;

    private int xVelocity;
    private int yVelocity;

    private float[] color; //RGB
    private float size;

    private double life;

    private Random random = new Random();

    public Particle(int x, int y) {
        this.x = x;
        this.y = y;

        this.xVelocity = random.nextInt(3);
        this.yVelocity = random.nextInt(3);
        this.xVelocity = random.nextBoolean() ? this.xVelocity : -this.xVelocity;
        this.yVelocity = random.nextBoolean() ? this.yVelocity : -this.yVelocity;

        this.color = new float[]{random.nextFloat(), random.nextFloat(), random.nextFloat()};
        this.size = 15f;

        this.life = 50 + random.nextInt(100);
    }


    public void update() {
        this.x += xVelocity;
        this.y += yVelocity;
        this.life -= 1;

        if (life % 10 == 0) {
            float[] newColor = new float[]{this.color[0] - 0.05f, this.color[1] - 0.05f, this.color[2] - 0.05f};
            this.color = newColor;
        }

        if (this.life % 30 == 0) {
            size -= 1;
        }
    }

    public void reset(int x, int y) {
        double radius = random.nextInt(20);
        double angle = random.nextDouble() * Math.PI * 2;
        int xPosition = x + (int) (Math.cos(angle) * radius);
        int yPosition = y + (int) (Math.sin(angle) * radius);
        this.x = xPosition;
        this.y = yPosition;

        this.life = 50 + random.nextInt(100);
        this.color = new float[]{random.nextFloat(), random.nextFloat(), random.nextFloat()};
        this.size = 15f;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getLife() {
        return life;
    }

    public float[] getColor() {
        return color;
    }

    public float getSize() {
        return this.size;
    }
}
