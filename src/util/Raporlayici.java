package util;

import model.ParkYeri;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Raporlayici {

    //bu siniftan nesne uretilmesine gerek yok zaten her sey static olacak
    //Private constructor ile disaridan nesne olusturamaz!
    private Raporlayici() {
    }
    /*
    Raporlayici sınıfı bir 'Utility Class' (Yardımcı Araç) sınıfıdır. Bu sınıfın amacı veri saklamak (plaka, renk vb.) değil, sadece matematiksel bir işlem yapıp (ekrana yazıp) çıkmaktır.

Bu yüzden içindeki tüm metotları static yaptık. Metotlar statik olduğu için, bu sınıftan new ile bir nesne üretmemize gerek yoktur. Eğer üretilirse RAM'de (hafızada) boşuna yer kaplar.

Ben de hafıza yönetimini doğru yapmak ve gereksiz nesne üretimini engellemek için Constructor'ı private yaparak kapıları kilitledim."
     */

    //Otoparkin haritasini çizdirir
    //Dolu yerlere PLAKA, bos yerlere BOS yazar.
    public static void matrisiKonsolaYazdir(ParkYeri[][] parkMatrisi) {
        //ParkYeri turunde parkmatrisini parametre olarak aliyoruz.

        if (parkMatrisi == null) {
            System.out.println("HATA! Otopark verisi okunamadi.");
            return;
        }
        System.out.println("\n--------------------- OTOPARK HARİTASI ---------------------");

        int satir = parkMatrisi.length;//Satir sayisini verir
        int sutun = parkMatrisi[0].length;//sutun sayisini verir

        //Ust baslik(SUTUN numaralari: 0  1  2 ..)
        System.out.print("      ");//sol kose bosluk birakiyoruz.
        for (int i = 0; i < sutun; i++) {

            // HESAPLAMA:
            // "   " (3 boşluk) + "Sutun " (6 karakter) + "%-2d" (2 rakam) + "  " (2 boşluk)
            // 3 + 6 + 2 + 2 = 13 Karakter (Tam kutunun genişliği kadar)

            //%-2d  ---> - isareti yazı sola dayali olsun demek. 2 = bu sayi icin 2 karakterlik yer ayir demek.d = decimal(tamsayi)
            System.out.printf("   Sutun %-2d  ", i);//printf = print formatted(formatli yazdirma)
        }
        System.out.println("\n      " + "-------------".repeat(sutun)); // Ayırıcı çizgi
        // .repeat --> icine yazdigimiz degisken kadar tre isaretini koyacak =bu sayede tablonun genisligi kadar otomatik cizgi ceker.


        //2. MATRİSİ ÇİZME (Tersten - Çatıdan Zemine)
        // DİKKAT: i = satir - 1 (En üst kat), i >= 0 (Zemin kata kadar)
        for (int i = satir - 1; i >= 0; i--) {
//sol tarafa kat numarasini yaziyor
            System.out.printf("Kat %d |", i);//printf ve %d ile hizali yazdirir.

            //O kattaki odalari(sutunlari) soldan saga gezer
            for (int j = 0; j < sutun; j++) {
                ParkYeri alan = parkMatrisi[i][j];//ParkYeri nesnesi turunde alan degiskenine o matrisin icini atiyoruz.

                //Doluluk Kontrolu
                if (alan.isDoluMu() && alan.getParkEdenArac() != null) {
                    //Dolu ise o aracn plakasini cekelim.
                    String plaka = alan.getParkEdenArac().getPlaka();
                    //Plakayi yazdir.%-8s :Plaka icin 8 karakterlik yer ayirir ve sola yaslar.
                    System.out.printf("[%-10s] ", plaka);
                } else {
                    //Bossa standart bos kutu cizilir.
                    System.out.print("[   BOS    ] ");
                }
            }
            //Kat bitti, asagi kata geciliyor.
            System.out.println();

            //Katlar arasi ayirici cizgi cekiliyor
            //kac kere cizilecegini dongu belirler.Cizginin ne kdar uzun olacagini sutun(boy) repeat belirler.
            System.out.println("      " + "-------------".repeat(sutun));
        }
        System.out.println("------------------------------------------------------------\n");
    }

    //Tarih-Saat formatlamak icin yardimci metot
    //Gelen LocalDateTime 'i gun/ay/yil saat:dakika formatina cevirir(daha duzgun gozukur.)
    public static String formatla(java.time.LocalDateTime tarih) {
        if (tarih == null) return "Belirsiz";
// Java'nın varsayılan tarih formatı (örn: 2025-12-10T15:30:00) okuması zor olduğu için,
        // burada özel bir 'Şablon' (Pattern) oluşturuyoruz.
        // Hedeflediğimiz Format: "Gün/Ay/Yıl Saat:Dakika" (Örn: 10/12/2025 14:30)
        java.time.format.DateTimeFormatter formatim = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return tarih.format(formatim);
    }

}
