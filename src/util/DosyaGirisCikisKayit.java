package util;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class DosyaGirisCikisKayit {

    private static final String DOSYA_ADI = "giris_kayitlari.txt";

    private DosyaGirisCikisKayit() {}

    // Araç girişinde çağrılır
    public static void girisKaydet(String plaka) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI, true))) {
            writer.write(plaka + ";" + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Dosyaya giriş kaydı yazılamadı!");
        }
    }

    // Araç çıkışında çağrılır, giriş zamanını getirir ve kaydı siler
    public static LocalDateTime girisZamaniGetirVeSil(String plaka) {
        Map<String, LocalDateTime> kayitlar = new HashMap<>();
        LocalDateTime girisZamani = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(DOSYA_ADI))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parca = satir.split(";");
                if (parca[0].equals(plaka)) {
                    girisZamani = LocalDateTime.parse(parca[1]);
                } else {
                    kayitlar.put(parca[0], LocalDateTime.parse(parca[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunamadı!");
        }

        // Çıkan aracı dosyadan sil
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (Map.Entry<String, LocalDateTime> entry : kayitlar.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Dosya güncellenemedi!");
        }

        return girisZamani;
    }
}
