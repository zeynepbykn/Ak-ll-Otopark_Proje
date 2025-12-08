package util;

import java.time.Duration;
import java.time.LocalDateTime;

//bu sınıftan baska bir sinif turetilmemesi icin final yaptik.
public final class UcretHesapla {
    /*
    'final' kelimesini SİLDİK. Artık değeri değişebilir.
    private static double SAATLIK_UCRET = 20.0;

 YENİ METOT: Fiyatı değiştirmek için
    public static void setSaatlikUcret(double yeniUcret) {
        SAATLIK_UCRET = yeniUcret;
        System.out.println("Yeni tarife güncellendi: " + yeniUcret + " TL");
    }
     */

    private static final double SAATLIK_UCRET = 20.0;

    //Constructoru private yaptik cunku disaridan new ile nesne olusturulmasina gerek yok.
    //bu sinifin icindeki hersey statictir(sinifa ait) nesne olusturulmasina gerek yok(gereksiz bellek kullanımı olmaz)
    private UcretHesapla() {
    }

    //LocalDateTime-> hem tarihi hem saati tutar.
    public static double parkSuresiDakikaHesapla(LocalDateTime giris, LocalDateTime cikis) {
        //kontrol bloku
        if (giris == null || cikis == null || cikis.isBefore(giris)) {
            return 0;//Gecersiz tarih veya null kontrolu yapar-> Hata durumunda 0 dondurerek islemi sonlandirir.
        }
        //Duration->Javanın kronometre sinifidir.Iki zaman arasindaki zamansal farki hesaplamak icin kullandik.
        Duration sure = Duration.between(giris, cikis);
        return sure.toMinutes();//farki dakikaya cevirir.
    }

    public static double standartUcretHesapla(double sureDakika) {
        /*Math.ceil->Tavana yuvarlar.
        mesela 1 saati gectiyse artik o 2. saatin parasini öder*/
        double saat = Math.ceil(sureDakika / 60.0);
        return saat * SAATLIK_UCRET; //saat basi 15 tl ile carpilacak.

    }


}
