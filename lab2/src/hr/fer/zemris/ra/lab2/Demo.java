package hr.fer.zemris.ra.lab2;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    private static int WIDTH = 1400;
    private static int HEIGHT = 700;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

            Animator animator = new Animator();
            animator.add(glCanvas);
            animator.start();

            List<ParticleSystem> particleSystems = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                particleSystems.add(new ParticleSystem(WIDTH, HEIGHT));
            }

            glCanvas.addGLEventListener(new GLEventListener() {
                @Override
                public void init(GLAutoDrawable glAutoDrawable) {
                }

                @Override
                public void dispose(GLAutoDrawable glAutoDrawable) {
                }

                @Override
                public void display(GLAutoDrawable glAutoDrawable) {

                    GL2 gl2 = glAutoDrawable.getGL().getGL2();
                    Texture texture = null;

                    try {
                        texture = TextureIO.newTexture(Paths.get("C:\\faks\\ra\\lab2\\resources\\cestica.bmp").toFile(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    gl2.glClearColor(0, 0, 0, 0);
                    gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
                    gl2.glLoadIdentity();


                    for (ParticleSystem ps : particleSystems) {
                        if (!ps.exploded()) {
                            TextureCoords coords = texture.getImageTexCoords();
                            texture.enable(gl2);
                            texture.bind(gl2);

                            gl2.glBegin(GL2.GL_QUADS);
                            gl2.glColor3f(1f, 1f, 1f);
                            int x = ps.getX();
                            int y = ps.getY();
                            float size = 10;
                            // gl2.glVertex2i(ps.getX(), ps.getY());
                            gl2.glVertex2d(x, y);
                            gl2.glTexCoord2f(coords.left(), coords.top());
                            gl2.glVertex2d(x, y + size);
                            gl2.glTexCoord2f(coords.right(), coords.top());
                            gl2.glVertex2d(x + size, y + size);
                            gl2.glTexCoord2f(coords.right(), coords.bottom());
                            gl2.glVertex2d(x + size, y);
                            gl2.glTexCoord2f(coords.left(), coords.bottom());
                            gl2.glEnd();
                            texture.disable(gl2);
                        } else {
                            List<Particle> particles = ps.getParticles();

                            texture.enable(gl2);
                            texture.bind(gl2);

                            gl2.glBegin(GL2.GL_QUADS);
                            for (Particle p : particles) {
                                float[] color = p.getColor();
                                gl2.glColor4f(color[0], color[1], color[2], 0.5f);

                                int x = p.getX();
                                int y = p.getY();
                                float pSize = p.getSize();
                                TextureCoords coords = texture.getImageTexCoords();

                                gl2.glVertex2d(x, y);
                                gl2.glTexCoord2f(coords.left(), coords.top());
                                gl2.glVertex2d(x, y + pSize);
                                gl2.glTexCoord2f(coords.right(), coords.top());
                                gl2.glVertex2d(x + pSize, y + pSize);
                                gl2.glTexCoord2f(coords.right(), coords.bottom());
                                gl2.glVertex2d(x + pSize, y);
                                gl2.glTexCoord2f(coords.left(), coords.bottom());


                                p.update();
                            }
                            gl2.glEnd();
                            texture.disable(gl2);

                        }

                        ps.update();

                    }
                }


                @Override
                public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                    GL2 gl2 = glAutoDrawable.getGL().getGL2();
                    gl2.glMatrixMode(GL2.GL_PROJECTION);
                    gl2.glLoadIdentity();

                    GLU glu = new GLU();
                    glu.gluOrtho2D(0.0f, width, 0.0f, height);

                    gl2.glMatrixMode(GL2.GL_MODELVIEW);
                    gl2.glLoadIdentity();
                    gl2.glViewport(0, 0, width, height);
                }
            });

            final JFrame jFrame = new JFrame(
                    "2. vježba - Sustav čestica");
            jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            jFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    jFrame.dispose();
                    System.exit(0);
                }
            });
            jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
            jFrame.setSize(WIDTH, HEIGHT);
            jFrame.setVisible(true);
            glCanvas.requestFocusInWindow();
        });
    }
}


