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
        SAATLIK_UCRET =
        yeniUcret;
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
        if (giris == null || cikis == null) {
            System.out.println("Hata :Giris veya Cikis saati bos!");
            return 0;//Gecersiz tarih veya null kontrolu yapar-> Hata durumunda 0 dondurerek islemi sonlandirir.
        }
        //  Duration sure = Duration.between(giris, cikis);
        // Dakikayı değil, toplam saniyeyi alıp 60'a bölüyoruz.
        // Örnek: 90 saniye -> 1.5 dakika olur.
        //  double hassasDakika = sure.toSeconds() / 60.0;

        //Duration->Javanın kronometre sinifidir.Iki zaman arasindaki zamansal farki hesaplamak icin kullandik.
        double dakikaFarki = Duration.between(giris, cikis).toMinutes();
        //Duration.toMinutes()long döndürür dakikaFarki double Java otomatik olarak long → double çevirir
        // implicit (örtük) type conversion
        if (dakikaFarki < 0) {
            throw new IllegalArgumentException(
                    "Çıkış zamanı giriş zamanından önce olamaz!"
            );
        }
        return dakikaFarki;

    }
    /* Bu metot artık kullanılmamaktadır.
             Bunun yerine Arac sınıflarındaki 'odenecekTutar()' metodunu kullanın.
            */
@Deprecated
    public static double standartUcretHesapla(double sureDakika) {
        /*Math.ceil->Tavana yuvarlar.
        mesela 1 saati gectiyse artik o 2. saatin parasini öder*/
        double saat = Math.ceil(sureDakika / 60.0);
        return saat * SAATLIK_UCRET;

    }

}
