import model.Arac;
import model.Motosiklet;
import model.Otomobil;
import service.OtoparkService;


public class OtoparkUygulama {

    public static void main(String[] args) {
        System.out.println("=== OTOPARK SİMÜLASYONU BAŞLIYOR ===");

        // 1. Otoparkı İnşa Et (3 Katlı, Her katta 5 yer)
        System.out.println("-> Otopark servisi başlatılıyor...");
        OtoparkService service = new OtoparkService(3, 5);

        // 2. Araçları Oluştur (Polimorfizm burada!)
        System.out.println("-> Araçlar kapıya geldi...");
        Arac arac1 = new Otomobil("34 IST 001"); // Otomobil
        Arac arac2 = new Otomobil("06 ANK 999"); // Diğer Otomobil
        Arac arac3 = new Motosiklet("35 IZM 35");

        try {
            // 3. Araçları İçeri Al
            System.out.println("\n--- GİRİŞ İŞLEMLERİ ---");

            // 1. Aracı 0. Kat 0. Sıraya koy
            service.aracGiris(arac1, 0, 0);

            // 2. Aracı 0. Kat 1. Sıraya koy
            service.aracGiris(arac2, 0, 1);

            // HATA TESTİ: Aynı yere tekrar araç koymaya çalışalım (Bakalım hata verecek mi?)
            System.out.println("\n(Test) Dolu yere araç park etmeye çalışılıyor...");
            service.aracGiris(new Otomobil("99 TEST 99"), 0, 0); // Burası hata vermeli!

        }
    catch (Exception e) {
        System.out.println("BEKLENEN HATA YAKALANDI: " + e.getMessage());
    }
        // --- ÇIKIŞ TESTLERİ ---
        try {
            System.out.println("\n--- ÇIKIŞ İŞLEMLERİ ---");

            // 34 IST 001 plakalı aracı çıkar
            double ucret = service.aracCikis("34 IST 001");
            System.out.println("Tahsil Edilen Ücret: " + ucret + " TL");

            // OLMAYAN PLAKA TESTİ
            System.out.println("\n(Test) Olmayan plakayı çıkarmaya çalışıyoruz...");
            service.aracCikis("00 YOK 00"); // Burası hata fırlatmalı!

        } catch (Exception e) {
            System.out.println(">>> BEKLENEN HATA YAKALANDI: " + e.getMessage());
        }
         /* PAZARTESİ YAPILACAKLAR (KULLANICI ARAYÜZÜ):
           1. Scanner kütüphanesi eklenecek.
           2. while(true) sonsuz döngüsü kurulacak.
           3. Ekrana menü seçenekleri (1-Giriş, 2-Çıkış, 3-Durum) yazdırılacak.
           4. Switch-Case yapısı ile kullanıcının seçimi yönetilecek.
        */
    }
}