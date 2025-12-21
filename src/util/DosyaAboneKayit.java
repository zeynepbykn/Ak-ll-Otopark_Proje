package util;

import model.AylikAbone;
import model.SaatlikAbone;
import model.Abone;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
/*Construtor private fakat sinif final cunku bu sadece yardimci sinif dosya okuyup
yazmasi yeterli ekstra hiç bir abilitye sahip olmasi gerekmez*/
public final class DosyaAboneKayit {

    private static final String DOSYA_ADI = "aboneler.txt";

    private DosyaAboneKayit() {}

    // Dosyadaki aboneleri Map olarak getir
    public static Map<String, Abone> aboneListesiniGetir() {
        Map<String, Abone> aboneler = new HashMap<>();
        //buffer tek tek degil toplu islemi sagliyo
        try (BufferedReader reader = new BufferedReader(new FileReader(DOSYA_ADI))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parca = satir.split(";");
                if (parca.length < 3) continue;

                String id = parca[0];
                String adSoyad = parca[1];
                String tip = parca[2];

                if (tip.equalsIgnoreCase("Aylık")) {
                    aboneler.put(id, new AylikAbone(id, adSoyad));
                } else if (tip.equalsIgnoreCase("Saatlik")) {
                    aboneler.put(id, new SaatlikAbone(id, adSoyad));
                }
            }
        } catch (IOException e) {
            System.err.println("❌Abone dosyası okunamadı!");
        }
        return aboneler;
    }

    // Yeni abone ekleme
    public static void aboneEkle(String id, String adSoyad, String tip) {
        //new FileWriter(DOSYA_ADI, true) hafizada hatırlama isini bu true yapiyo (Append Mode)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI, true))) {
            writer.write(id + ";" + adSoyad + ";" + tip);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("❌Dosyaya abone eklenemedi!");
        }
    }
}
