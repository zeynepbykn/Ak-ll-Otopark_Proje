package util;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class DosyaGirisCikisKayit {

    // Dosyamızın adı. Proje klasöründe otomatik oluşacak.
    private static final String DOSYA_ADI = "giris_kayitlari.txt";

    // Constructor private, çünkü bu sınıftan nesne üretilmesini istemiyoruz.
    // Metotları static yaptık, direkt SınıfAdı.metot() diye çağıracağız.
    private DosyaGirisCikisKayit() {}

    // --- 1. KAYDETME (YAZMA) ---
    // Araç girince Service sınıfı burayı çağırır.
    public static void girisKaydet(String plaka, String tip, int kat, int sira) {
        // FileWriter(..., true) komutundaki 'true', "Dosyanın sonuna ekle" demektir.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI, true))) {

            // Verileri aralarına noktalı virgül (;) koyarak birleştiriyoruz.
            // Örnek Satır: 34ABC123;Otomobil;0;1;2025-12-18T15:30:00
            String kayitSatiri = plaka + ";" + tip + ";" + kat + ";" + sira + ";" + LocalDateTime.now();

            writer.write(kayitSatiri);
            writer.newLine(); // Alt satıra geç

        } catch (IOException e) {
            System.err.println("Dosyaya giriş kaydı yazılamadı!");
        }
    }

    // --- 2. OKUMA (TÜM LİSTEYİ GETİRME) ---
    // Program ilk açıldığında Service sınıfı burayı çağırır.
    public static List<String> kayitlariOku() {
        List<String> satirlar = new ArrayList<>();
        File dosya = new File(DOSYA_ADI);

        // Eğer dosya henüz oluşmamışsa (ilk çalıştırma), boş liste dön.
        if (!dosya.exists()) {
            return satirlar;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dosya))) {
            String satir;
            // Dosyanın sonuna gelene kadar satır satır oku
            while ((satir = reader.readLine()) != null) {
                satirlar.add(satir);
            }
        } catch (IOException e) {
            System.err.println("Dosya okunamadı!");
        }
        return satirlar;
    }

    // --- 3. SİLME VE ZAMANI ALMA ---
    // Araç çıkarken çalışır. O aracı bulur, siler, dosyanın geri kalanını tekrar yazar.
    public static LocalDateTime girisZamaniGetirVeSil(String plaka) {
        List<String> tumSatirlar = kayitlariOku(); // Önce hepsini hafızaya al
        List<String> kalanSatirlar = new ArrayList<>();
        LocalDateTime girisZamani = null;

        for (String satir : tumSatirlar) {
            // Satırı parçala: "34ABC;Oto;0;1;Zaman"
            String[] parca = satir.split(";");

            // parca[0] -> Plaka
            // parca[4] -> Zaman (0,1,2,3,4. sırada)

            if (parca[0].equals(plaka)) {
                // Aranan araç bulundu! Zamanını al, listeye ekleme (silinmiş olur)
                girisZamani = LocalDateTime.parse(parca[4]);
            } else {
                // Bu araç otoparkta kalmaya devam ediyor, listeye ekle
                kalanSatirlar.add(satir);
            }
        }

        // Dosyayı silip temiz halini (kalanları) yeniden yazıyoruz
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (String s : kalanSatirlar) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Dosya güncellenemedi!");
        }

        return girisZamani;
    }
}