//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import util.UcretHesapla;//main ile farklı klasörlerde oldugu icin(paket)import ettik.

import java.time.LocalDateTime;

public class OtoparkUygulama {
    public static void main(String[] args) {

        System.out.println("=== 8 ARALIK: UTIL TESTİ BAŞLIYOR ===");

        // 1. SENARYO: Araç saat 10:00'da girsin, 12:30'da çıksın.
        // (Toplam 2 saat 30 dakika = 150 dakika eder)
        LocalDateTime girisZamani = LocalDateTime.of(2025, 12, 9, 10, 0);
        LocalDateTime cikisZamani = LocalDateTime.of(2025, 12, 9, 12, 30);

        // 2. Senin yazdığın Util sınıfını kullanarak hesaplatalım
        double sureDakika = UcretHesapla.parkSuresiDakikaHesapla(girisZamani, cikisZamani);
        double ucret = UcretHesapla.standartUcretHesapla(sureDakika);

        // 3. Sonuçları Ekrana Basalım
        System.out.println("Giriş Saati: " + girisZamani);
        System.out.println("Çıkış Saati: " + cikisZamani);
        System.out.println("-------------------------------------");

        System.out.println("Hesaplanan Süre (Dakika): " + sureDakika);
        // Beklenen: 150.0

        System.out.println("Hesaplanan Ücret (TL): " + ucret);
        // Beklenen: 2.5 saat -> 3 saate yuvarlanır. 3 * 20 TL = 60.0 TL

        System.out.println("=====================================");

        if (sureDakika == 150.0 && ucret == 60.0) {
            System.out.println("✅ TEST BAŞARILI! Hesap makinesi doğru çalışıyor.");
        } else {
            System.out.println("❌ TEST HATALI! Kodda bir yanlışlık var.");
        }
    }
}