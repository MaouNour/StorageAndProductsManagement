package BackEnd.Exceptions;

public class NegativePriceException extends Exception {
    public NegativePriceException(String msg) {
        super(msg);
    }
}
