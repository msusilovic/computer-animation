package hr.fer.zemris.ra.lab1.linearna;

public interface IVector {

    double get(int d);

    IVector set(int position, double value);

    int getDimension();

    IVector copy();

    IVector copyPart(int part);

    IVector newInstance(int dimension);

    IVector add(IVector other);

    IVector nAdd(IVector other);

    IVector sub(IVector other);

    IVector nSub(IVector other);

    IVector scalarMultiply(double scalar);

    IVector nScalarMultiply(double scalar);

    double norm();

    IVector normalize();

    IVector nNormalize();

    double cosine(IVector other);

    double scalarProduct(IVector other);

    IVector nVectorProduct(IVector other);

    IVector nFromHomogenous();

    IMatrix toRowMatrix(boolean b);

    IMatrix toColumnMatrix(boolean b);

    double[] toArray();

    String toString(int precision);

}
