package hr.fer.zemris.ra.lab1.linearna;

public interface IMatrix {

    int getRowsCount();

    int getColsCount();

    double get(int row, int col);

    IMatrix set(int row, int col, double value);

    IMatrix copy();

    IMatrix newInstance(int row, int col);

    IMatrix nTranspose(boolean b);

    IMatrix add(IMatrix other);

    IMatrix nAdd(IMatrix other);

    IMatrix sub(IMatrix other);

    IMatrix nSub(IMatrix other);

    IMatrix nMultiply(IMatrix other);

    double determinant();

    IMatrix subMatrix(int row, int col, boolean b);

    IMatrix nInvert();

    double[][] toArray();

    IVector toVector(boolean b);

    IMatrix scalarMultiply(double scalar);
}
