package exception;
//Custom Exception
// Bu sinif, plaka kurallarina uymayan durumlar icin ozel hata firlatir.
public class HataliPlakaException extends Exception {
//ayrica sinif olusturdumki direkt hata sebebi belli olsun
    public HataliPlakaException(String message) {
        super(message);
    }
}