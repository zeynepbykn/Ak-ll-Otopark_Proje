package model;// Bu sınıfın 'model' (Nesne) kutusunda olduğunu belirtir.

import java.time.LocalDateTime;

// Kendi yazdığımız hata sınıfını çağırıyoruz. (Plaka hatalıysa fırlatacağız)
import exception.GecersizMiktarException;

import ınterface.GirisCikisTakip;

/* Abstract soyut kavramlari bir baslikta toplar, bu yuzden nesne olusturamaz ama miras alinabilir.
   Sadece tum araclarin ortak ozelliklerini kod tekrari olmadan bir arada toplayabilmek icin. */

// GirisCikisTakip sinifi otopark sistemine "Ben giris ve cikis yapabilirim" taahhudu verir.
public abstract class Arac implements GirisCikisTakip {
    // Değişkenleri 'private' yaparak dış dünyadan koruyoruz. Sadece metodlarla erişilebilir.
    private String plaka;
    private LocalDateTime girisZamani;
    private boolean parktaMi;//(true/false) olarak

    // --- CONSTRUCTOR
    public Arac(String plaka) {
        this.plaka = plaka;
        //sol taraf = aracin artik kalici ozelligi /sag taraf = disaridan alinan gecici bilgi
        //this = nesnenin kendisi yani dısaridan gelen plakayi kaydeder.
        this.parktaMi = false;
        // Araç ilk oluşturulduğunda henüz park etmemiş varsayıyoruz. Aksini soyleedigimiz surece.

    }
    // Hoca Soru Sorarsa Savunma Metni:
    // "Hocam, Matris (2D Dizi) yapısında teknik olarak her hücre 1 araç tutar.
    // Ancak bu metot sayesinde 'Otopark Doluluk Oranı' ile 'Alan Kullanım Verimliliği'ni ayrıştırıyorum.
    // Otopark %100 dolu olsa bile, bu metot sayesinde hacimsel verimliliği raporlayabilirim."
/*Ancak bu kısıtlamaya rağmen 'Verimliliği' ölçmek istedim. yerKaplamaDurumu metodunu tam olarak bu yüzden; 'Otopark Doluluk Oranı' ile **'Alan Kullanım Verimliliği'**ni ayırt etmek için yazdım.

Şu an fiziksel olarak her araç 1 kutu kaplıyor,
evet. Ama gün sonunda rapor aldığımda şunu görmek istiyorum: 'Otoparkımız %100 dolu görünüyor AMA içerideki araçların hacmi sebebiyle aslında kapasitemizin sadece %50'sini kullanıyoruz.'*/
    //antık: Her aracın yeri bellidir. "3. Sıra, 4. Koltuk" diyebilirsin ([2][3]).
    //Eğer ArrayList<Arac> kullansaydık:
    //Mantık: Araçlar arka arkaya dizilir. Sıra numarası yoktur, sadece "Listeye Ekle" vardır.
    public abstract String yerKaplamaDurumu();

    @Override
    public void girisYap() {
        // Aracın kendi üzerindeki "Saat" ve "Durum" bilgisini güncelliyoruz.
        // Otoparkın doluluk hesabı burada DEĞİL, Service katmanında yapılır.
        this.girisZamani = LocalDateTime.now();
        this.parktaMi = true;
        System.out.println(plaka + " giriş saati kaydedildi: " + girisZamani);
    }
    @Override
    public void cikisYap() {
        // Araç çıkış statüsüne geçer.
        // Ücret hesaplaması 'UcretHesapla' sınıfında yapılacağı için burada para konuşulmaz.
        this.parktaMi = false;
        System.out.println(plaka + " çıkış işlemi başlatıldı.");
    }
    // --- GETTER & SETTER (Kontrollü Erişim) ---
//sadece plakanin ne oldugunu gostermek icin plakayi ceker
    public String getPlaka() {
        return plaka;
    }
    // GÜNCELLENMİŞ ULUSLARARASI PLAKA KONTROLÜ
    public void setPlaka(String plaka) throws GecersizMiktarException {
        // 1. ADIM: Boş mu kontrolü
        if (plaka == null) {
            throw new GecersizMiktarException("HATA: Plaka boş olamaz!");
        }

        // Küçük harf girildiyse otomatik BÜYÜK harfe çevir.
        // Ayrıca baştaki ve sondaki gereksiz boşlukları (trim) temizle.
        plaka = plaka.trim().toUpperCase();

        // 2. ADIM: GENEL GEÇER PLAKA FORMATI (REGEX)
        // Açıklama:
        // [A-Z0-9 -]  -> Sadece Harf, Rakam, Boşluk ve Tire kabul et.
        // {5,15}      -> En az 5, en çok 15 karakter olsun.
        //{5,15} Anlamı: "İzin verilen karakterlerden en az 5 tane, en çok 15 tane yan yana gelebilir."
        String globalPlakaKalibi = "^[A-Z0-9 -]{5,15}$";
        //"^"Yazı tamamen plaka olmalı, başında başka bir şey olmamalı anlamına gelir

        //"$"Satırın sonuna geldik. Arkasından başka kaçak karakter gelmesin.
        if (!plaka.matches(globalPlakaKalibi)) {
            // Eğer içinde @, *, ? gibi yasaklı karakter varsa veya çok kısaysa hata ver.
            throw new GecersizMiktarException("HATA: Plaka geçersiz karakterler içeriyor veya uzunluk hatalı! (Girdi: " + plaka + ")");
        }

        // Her şey düzgünse kaydet.
        this.plaka = plaka;
    }

        public LocalDateTime getGirisZamani() {
            return girisZamani;
        }
//Java'da boolean (evet/hayır) değer döndüren getter metotları get ile değil, genellikle is (öyle mi?) ile başlar.
        public boolean isParktaMi() {
            return parktaMi;
        }
//olurda bulunma durumunu manuel kontrol etmemiz gerekirse diye set ile ayarlama olasiligini ekledik
        public void setParktaMi(boolean parktaMi) {
            this.parktaMi = parktaMi;
        }
    }