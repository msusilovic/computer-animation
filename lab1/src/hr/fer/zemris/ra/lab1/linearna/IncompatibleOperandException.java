package hr.fer.zemris.ra.lab1.linearna;

public class IncompatibleOperandException extends RuntimeException {

    public IncompatibleOperandException() {
        super();
    }

    public IncompatibleOperandException(String message){
        super(message);
    }

}
