package hr.fer.zemris.ra.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem {

    private int x;
    private int y;
    private int maxY;

    private int maxWidth;
    private int maxHeight;

    private List<Particle> particles;

    private int life;

    Random random = new Random();

    public ParticleSystem(int w, int h) {
        this.maxWidth = w;
        this.maxHeight = h;
        this.x = 50 + random.nextInt(w - 100);
        this.maxY = 50 + random.nextInt(h - 100);
        this.y = 0;

        setParticles(200 + random.nextInt(200));
        this.life = 200 + random.nextInt(100);

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public List<Particle> getParticles() {
        return particles;
    }

    public void update() {
        if (!exploded()) {
            this.y += 2;
            this.x = this.x +( random.nextInt(6) - 3);
        } else {
            this.life -= 1;
        }
        if (this.life <= 0 && particles.stream().filter(particle -> particle.getLife() > 0).count() == 0) {
            reset();
        }
        if (this.life > 0) {
            resetParticles();
        }

    }

    private void reset() {
        this.x = 50 + random.nextInt(this.maxWidth - 100);
        this.maxY = 50 + random.nextInt(this.maxHeight - 100);
        this.y = 0;
        // setParticles(200 + random.nextInt(200));

        this.life = 200 + random.nextInt(100);
    }

    private void setParticles(int numParticles) {
        this.particles = new ArrayList<>();

        for (int i = 0; i < numParticles; i++) {
            int radius = random.nextInt(50);
            double angle = random.nextDouble() * Math.PI * 2;

            int xPosition = this.x + (int) (Math.cos(angle) * radius);
            int yPosition = this.maxY + (int) (Math.sin(angle) * radius);

            this.particles.add(new Particle(xPosition, yPosition));
        }
    }

    private void resetParticles() {
        for (Particle p : particles) {
            if (p.getLife() <= 0) {
                p.reset(this.x, this.maxY);
            }
        }
    }

    public boolean exploded() {
        return this.maxY <= this.y;
    }
}
