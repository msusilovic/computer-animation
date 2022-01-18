package hr.fer.zemris.ra.lab1;

import hr.fer.zemris.ra.lab1.linearna.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class BSpline {

    private static IMatrix B = new Matrix(4, 4, new double[][]{{-1, 3, -3, 1}, {3, -6, 3, 0}, {-3, 0, 3, 0}, {1, 4, 1, 0}}, true).scalarMultiply(1 / 6.);
    private static IMatrix BTangent = new Matrix(3, 4, new double[][]{{-1, 3, -3, 1}, {2, -4, 2, 0}, {-1, 0, 1, 0}}, true).scalarMultiply(0.5);

    private int numSegments;
    private List<IVector> controlPoints;
    private List<IMatrix> segments = new ArrayList<>();
    private List<IMatrix> tSegments = new ArrayList<>();
    List<IVector> samplePoints;
    List<IVector> sampleTangents;

    /**
     * Creates new BSpline object representing a cube B-spline curve.
     *
     * @param filename name of file to parse control points from
     */
    public BSpline(String filename) {
        controlPoints = new ArrayList<>();
        try {
            String[] lines = Files.readString(Path.of(filename)).split("\n");
            for (String line : lines) {
                String[] coordinates = line.split("\\s+");
                controlPoints.add(new Vector(Arrays.stream(coordinates).mapToDouble(Double::parseDouble).toArray()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.numSegments = controlPoints.size() - 3;
        calculateSegments();
    }

    /**
     * Calculates segments of this B-spline. Called once.
     */
    private void calculateSegments() {
        for (int i = 1; i <= numSegments; i++) {
            IMatrix Ri = getRi(i);
            IMatrix segment = B.nMultiply(Ri);
            IMatrix tSegment = BTangent.nMultiply(Ri);
            segments.add(segment);
            tSegments.add(tSegment);
        }
    }

    /**
     * Generates sample points and calculates direction from cube B-spline curve.
     *
     * @param numSamples number of samples in each segment of curve
     */
    public List<IVector> samplePoints(int numSamples) {
        samplePoints = new ArrayList<>();
        sampleTangents = new ArrayList<>();
        double[] t_values = IntStream.rangeClosed(0, numSamples - 1).mapToDouble(i -> i / ((double) numSamples - 1)).toArray();

        for (int i = 0; i < numSegments; i++) {
            for (double t : t_values) {
                IMatrix T = new Matrix(1, 4, new double[][]{{t * t * t, t * t, t, 1}}, true);
                IMatrix tangentT = new Matrix(1, 3, new double[][]{{t * t, t, 1}}, true);
                samplePoints.add(new VectorMatrixView(T.nMultiply(this.segments.get(i))));
                sampleTangents.add(new VectorMatrixView(tangentT.nMultiply(this.tSegments.get(i))));
            }
        }
        return samplePoints;

    }

    /**
     * Method to get control points relevant for calculating i-th segment of B-spline.
     *
     * @param i order number of segment
     * @return matrix containing relevant control points
     */
    private IMatrix getRi(int i) {
        List<IVector> vectors = this.controlPoints.subList(i - 1, i + 3);
        double[][] elements = new double[vectors.size()][vectors.get(0).getDimension()];
        for (int k = 0; k < 4; k++) {
            elements[k] = vectors.get(k).toArray();
        }

        return new Matrix(elements.length, elements[0].length, elements, true);
    }

    /**
     * Get sample points from B-spline curve.
     *
     * @return list of sample poinst
     */
    public List<IVector> getSamplePoints() {
        return samplePoints;
    }

    /**
     * Get tangent values corresponding to sampled points of B-spline curve.
     *
     * @return list of tangent vectors in sample points
     */
    public List<IVector> getSampleTangents() {
        return sampleTangents;
    }

}