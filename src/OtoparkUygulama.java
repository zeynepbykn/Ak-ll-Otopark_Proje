import model.*;
import service.OtoparkService;
import exception.OtoparkDoluException;

public class OtoparkUygulama {

    public static void main(String[] args) {

        // 1. Otopark oluştur (3 kat x 4 sıra)
        OtoparkService otopark = new OtoparkService(3, 4);

        // 2. Aboneler ekle
        otopark.yeniAboneEkle("A001", "Ali Veli", "Aylik");
        otopark.yeniAboneEkle("S001", "Ayşe Yılmaz", "Saatlik");

        // 3. Araçlar oluştur
        Arac arac1 = new Otomobil("34ABC01");       // Binek araba
        Arac arac2 = new Motosiklet("06XYZ99");     // Motosiklet
        Arac arac3 = new AracOzel("35DEF02");       // Özel araç
        Arac arac4 = new Otomobil("34ABC01");       // Aynı plaka ile test (Hatalı durum)

        // 4. Aboneleri araçlara ata (isteğe bağlı)
        arac1.setAbone(otopark.getAboneler().get("A001")); // Ali Veli aylık abone
        arac2.setAbone(otopark.getAboneler().get("S001")); // Ayşe Yılmaz saatlik abone

        try {
            // 5. Araç girişleri
            otopark.aracGiris(arac1, 0, 0); // 1. kat, 0. sıra
            otopark.aracGiris(arac2, 0, 1); // 1. kat, 1. sıra
            otopark.aracGiris(arac3, 1, 0); // 2. kat, 0. sıra

            // 6. Dolu yere giriş denemesi
            otopark.aracGiris(arac4, 0, 0); // Aynı plaka ve dolu yer
        } catch (OtoparkDoluException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Giriş Hatası: " + e.getMessage());
        }

        // 7. Park durumunu görselleştir
        System.out.println("\n--- PARK DURUMU ---");
        ParkYeri[][] matris = otopark.getParkMatrisi();
        for (int i = 0; i < matris.length; i++) {
            for (int j = 0; j < matris[i].length; j++) {
                System.out.print((matris[i][j].isDoluMu() ? "[D]" : "[B]") + " ");
            }
            System.out.println();
        }

        // 8. Araç çıkışları ve ücret hesaplama
        System.out.println("\n--- ÇIKIŞ VE ÜCRET HESAPLAMA ---");
        try {
            double ucret1 = otopark.aracCikis("34ABC01");
            System.out.println("34ABC01 çıkış ücreti: " + ucret1 + " TL");

            double ucret2 = otopark.aracCikis("06XYZ99");
            System.out.println("06XYZ99 çıkış ücreti: " + ucret2 + " TL");

            double ucret3 = otopark.aracCikis("35DEF02");
            System.out.println("35DEF02 çıkış ücreti: " + ucret3 + " TL");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Çıkış Hatası: " + e.getMessage());
        }

        // 9. Son park durumu
        System.out.println("\n--- SON PARK DURUMU ---");
        for (int i = 0; i < matris.length; i++) {
            for (int j = 0; j < matris[i].length; j++) {
                System.out.print((matris[i][j].isDoluMu() ? "[D]" : "[B]") + " ");
            }
            System.out.println();
        }

    }
}
