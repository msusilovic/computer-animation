package hr.fer.zemris.ra.lab1;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import hr.fer.zemris.ra.lab1.linearna.IVector;
import hr.fer.zemris.ra.lab1.linearna.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Demo {
    public static void main(String[] args) {
        Map<String, Float> objectScales = Stream.of(new Object[][]{
                {"frog.obj", 0.5f},
                {"747.obj", 8f},
                {"teddy.obj", 0.1f}
        }).collect(Collectors.toMap(pair -> (String) pair[0], pair -> (Float) pair[1]));


        String filename = "frog.obj";
        String path = "objects/" + filename;
        ObjectModel model = new ObjectModel(path);
        IVector s = new Vector(new double[]{0., 0., 1.});

        final long[] startTime = {System.currentTimeMillis()};
        long interval = 20;

        BSpline spline = new BSpline("b_spline.txt");
        spline.samplePoints(100);

        EyePosition eye = new EyePosition(60, 30, 0);

        SwingUtilities.invokeLater(() -> {
            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

            eye.setCanvas(glCanvas);
            glCanvas.addKeyListener(eye.getKeyAdapter());
            Animator animator = new Animator();
            animator.add(glCanvas);
            animator.start();

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

                    gl2.glClearColor(1, 1, 1, 0);
                    gl2.glLineWidth((float) 2.);
                    gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
                    gl2.glLoadIdentity();

                    GLU glu = new GLU();
                    IVector v = eye.getEyeVector();
                    glu.gluLookAt(v.get(0), v.get(1), v.get(2), 5f, 5f, 20f, 0f, 0f, 1f);

                    List<IVector> samples = spline.getSamplePoints();
                    List<IVector> tangents = spline.getSampleTangents();

                    // draw curve
                    gl2.glColor3f(0, 0, 0);
                    gl2.glBegin(GL.GL_LINE_STRIP);
                    for (IVector point : samples) {
                        gl2.glVertex3f((float) point.get(0), (float) point.get(1), (float) point.get(2));
                    }
                    gl2.glEnd();

                    double time = System.currentTimeMillis();
                    int index = (int) ((time - startTime[0]) / interval);
                    if (index >= samples.size()) {
                        index = 0;
                        startTime[0] = System.currentTimeMillis();
                    }

                    IVector position = samples.get(index);
                    IVector e = tangents.get(index);
                    IVector axis = s.nVectorProduct(e);
                    double angle = Math.toDegrees(Math.acos(s.scalarProduct(e) / (s.norm() * e.norm())));
                    IVector scaledT = e.normalize().nScalarMultiply(5);

                    gl2.glTranslatef((float) position.get(0), (float) position.get(1), (float) position.get(2));

                    // draw tangent
                    gl2.glColor3f(1f, 0f, 0f);
                    gl2.glBegin(GL.GL_LINE_STRIP);
                    gl2.glVertex3f(0f, 0f, 0f);
                    gl2.glVertex3f((float) scaledT.get(0), (float) scaledT.get(1), (float) scaledT.get(2));
                    gl2.glEnd();

                    gl2.glColor3f(0, 0, 0);
                    gl2.glRotatef((float) angle, (float) axis.get(0), (float) axis.get(1), (float) axis.get(2));

                    float scaleFactor = objectScales.getOrDefault(filename, 1f);
                    gl2.glScalef(scaleFactor, scaleFactor, scaleFactor);

                    // draw object
                    for (Face3D face : model.getFaces()) {
                        Vertex3D[] vertices = face.getVertices();
                        gl2.glBegin(GL2.GL_POLYGON);
                        gl2.glVertex3f((float) vertices[0].getX(), (float) vertices[0].getY(), (float) vertices[0].getZ());
                        gl2.glVertex3f((float) vertices[1].getX(), (float) vertices[1].getY(), (float) vertices[1].getZ());
                        gl2.glVertex3f((float) vertices[2].getX(), (float) vertices[2].getY(), (float) vertices[2].getZ());
                        gl2.glEnd();
                    }
                    gl2.glPopMatrix();
                }


                @Override
                public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                    GL2 gl2 = glAutoDrawable.getGL().getGL2();
                    gl2.glMatrixMode(GL2.GL_PROJECTION);
                    gl2.glLoadIdentity();
                    gl2.glFrustum(-0.5f, 0.5, -0.5f, 0.5f, 1, 100);

                    gl2.glMatrixMode(GL2.GL_MODELVIEW);
                    gl2.glViewport(0, 0, width, height);
                }
            });

            final JFrame jFrame = new JFrame(
                    "1. vježba - B-spline krivulja");
            jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            jFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    jFrame.dispose();
                    System.exit(0);
                }
            });
            jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
            jFrame.setSize(700, 800);
            jFrame.setVisible(true);
            glCanvas.requestFocusInWindow();

        });
    }

}
