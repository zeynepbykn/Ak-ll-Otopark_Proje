import model.Arac;
import model.Otomobil;
import model.ParkYeri;
import model.AylikAbone;

public class OtoparkUygulama {

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("🚧 ÖĞRENCİ 1 (MODEL) - SİSTEM KONTROL TESTİ 🚧");
        System.out.println("==========================================\n");

        // ---------------------------------------------------------
        // TEST 1: PLAKA KONTROLÜ (GÖREV: Hatalı girişin yakalanması)
        // ---------------------------------------------------------
        System.out.println(">>> TEST 1: Kısa Plaka Girişi ('abc') deneniyor...");

        // 1. Hatalı bir otomobil oluşturmaya çalışıyoruz.
        // Arac sınıfının constructor'ı hatayı yakalayıp plakayı "HATALI-PLAKA" yapmalı.
        Arac hataliArac = new Otomobil("abc");

        // 2. Kontrol ediyoruz: Sistem hatayı fark edip etiketi yapıştırdı mı?
        if (hataliArac.getPlaka().equals("HATALI-PLAKA")) {
            System.out.println("✅ BAŞARILI! Sistem tehdidi algıladı ve plakayı 'HATALI-PLAKA' yaptı.");
        } else {
            System.out.println("❌ BAŞARISIZ! Sistem hatalı plakayı ('" + hataliArac.getPlaka() + "') kabul etti.");
        }
        System.out.println("--------------------------------------------------\n");

        // ---------------------------------------------------------
        // TEST 2: PARK YERİ KONTROLÜ (GÖREV: -1 Damgası)
        // ---------------------------------------------------------
        System.out.println(">>> TEST 2: Negatif Kat Girişi (-5. Kat) deneniyor...");

        // ParkYeri constructor'ı negatif sayı görünce yer numarasını -1 yapmalı.
        ParkYeri hataliYer = new ParkYeri(101, -5, 2);

        if (hataliYer.getYerNumarasi() == -1) {
            System.out.println("✅ BAŞARILI! Park yeri 'Hatalı' (-1) olarak etiketlendi.");
        } else {
            System.out.println("❌ BAŞARISIZ! Park yeri hatalı veriye rağmen oluşturuldu.");
        }
        System.out.println("--------------------------------------------------\n");

        // ---------------------------------------------------------
        // TEST 3: AYLIK ABONE HESABI (GÖREV: 0 TL Çıkmalı)
        // ---------------------------------------------------------
        System.out.println(">>> TEST 3: Aylık Abone Ücret Hesabı...");

        AylikAbone vipMusteri = new AylikAbone("999", "Test Kullanıcısı");

        // 5 saat kalsa, saati 100 TL olsa bile Aylık Abone olduğu için 0 dönmeli.
        double ucret = vipMusteri.ucretHesapla(5, 100);

        if (ucret == 0.0) {
            System.out.println("✅ BAŞARILI! Aylık abone ücreti 0.0 TL olarak hesaplandı.");
        } else {
            System.out.println("❌ BAŞARISIZ! Aylık aboneden para istendi: " + ucret);
        }

        System.out.println("\n==========================================");
        System.out.println("🏁 TESTLER TAMAMLANDI - MODEL KATMANI HAZIR");
        System.out.println("==========================================");
    }
}