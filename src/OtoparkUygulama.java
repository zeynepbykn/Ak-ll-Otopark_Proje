import model.Arac;
import service.OtoparkService;
import exception.OtoparkDoluException;
import model.AracOzel;

public class OtoparkUygulama {
    public static void main(String[] args) {

        // Otopark oluştur: 3 kat x 4 sıra
        OtoparkService otopark = new OtoparkService(3, 4);


        // Araç oluştur
        Arac arac1 = new AracOzel("34ABC01");
        Arac arac2 = new AracOzel("06XYZ99");

        try {
            // Araçları park et
            otopark.aracGiris(arac1, 0, 0);
            otopark.aracGiris(arac2, 1, 1);

            // Araç çıkışı ve ücret hesaplama
            double ucret1 = otopark.aracCikis("34ABC01");
            System.out.println("34ABC01 aracının ücreti: " + ucret1 + " TL");

        } catch (OtoparkDoluException e) {
            System.err.println(e.getMessage());
        }

        // Güncel park durumu göster
        for (var kat : otopark.getParkMatrisi()) {
            for (var yer : kat) {
                System.out.print((yer.isDoluMu() ? "[D]" : "[B]") + " ");
            }
            System.out.println();
        }
    }
}
