package exception;

// Bu sınıf, plaka kurallarına uymayan durumlar için özel hata fırlatır.
public class HataliPlakaException extends Exception {

    public HataliPlakaException(String message) {
        super(message);
    }
}