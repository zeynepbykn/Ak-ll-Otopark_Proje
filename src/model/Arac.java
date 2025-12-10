package model;// Bu sınıfın 'model' (Nesne) kutusunda olduğunu belirtir.

import java.time.LocalDateTime;

// Kendi yazdığımız hata sınıfını çağırıyoruz. (Plaka hatalıysa fırlatacağız)
import exception.GecersizMiktarException;

import ınterface.GirisCikisTakip;

/* Abstract sinifi soyuttur bi nevi ust baslik.
Yani bi başlik içinde ortak ozellikleri  toplamak icin kullandildi.
Bu yüzden new Arac() seklinde bir nesne uretilemez ortak bi turle degil spesifik bir seyle nesne uretebiliriz.
Fakat (public oldugu icin) herhangi bir sinif miras alabilir
 */

// GirisCikisTakip sinifi otopark sistemine "Ben giris ve cikis yapabilirim" taahhudu verir.
public abstract class Arac implements GirisCikisTakip {

    /*Encapsulation kavrami ile değişkenleri 'private' yaparak korumaya aliyoruz.
    Sadece get ve set metotları ile  erişilebilirler.*/

    private String plaka;
    private LocalDateTime girisZamani;
    private boolean parktaMi;//(true/false) olarak

    /* Constructor nesne hafizada olusurken calisir
    */

    //Plaka parametreli constructor
    public Arac(String plaka) {
        try {
            //Plakayi kaydetmek icin.
            // Direkt this.plaka = plaka atamasi yaparsak plaka kontrolunun anlami kalmaz.
            setPlaka(plaka);
        } catch (GecersizMiktarException hataKutusu) {
            // Eger plaka hataliysa program cokmesin, hatayi hataKutusunun icine atsin diye onlem.
            System.out.println(">>> HATA: Girilen '" + plaka + "' plakasi kurallara uymuyor!");
            System.out.println(">>> Sistem mesaji: " + hataKutusu.getMessage());

            /*Plakayı boş (null) birakirsam program çökebilir
            ve ayni zamanda hangi aracin hatali oldugunu kaydetmis olur.*/
            this.plaka = "HATALI-PLAKA";
        }
        this.parktaMi = false;
    }

/* Otopark doluluk ve verimlilik raporu icin alt siniflar bunu doldurmak zorunda.
 Matris (2D Dizi) yapısında teknik olarak her hücre 1 araç tutar Yani aslinda verimlilik acisindan %100 degil fakat
 ArrayList<Arac> ile sadece arclarin girisini kaydetmek yerine matris ile kat konum bilgilerini ozel olarak tutmak istedik*/

        public abstract String yerKaplamaDurumu();

    @Override
    public void girisYap() {
        // Aracın kendi üzerindeki "Saat" ve "Durum" bilgisini güncelliyoruz.
        this.girisZamani = LocalDateTime.now();
        this.parktaMi = true;
        System.out.println(plaka + " giriş saati kaydedildi: " + girisZamani);
    }
    @Override
    public void cikisYap() {
        // Araç çıkış statüsüne geçer.
        this.parktaMi = false;
        System.out.println(plaka + " çıkış işlemi başlatıldı.");
    }

    //Getter & Setter (Kontrollü Erişim) icin sadece plakanin ne oldugunu gostermek icin
    //Daha sonra diger siniflarda baska metotlarda ihityac duyulacagi icin
public String getPlaka() {
        return plaka;
    }

    /*Sadece Türk plakasi olmamasi durumunda standart bi sart kosmak problem
    yaratacagi icin bi kontrol sarti olmali fakat cok fazla sart oldugu ıcın standrt bir dongu
    verimsiz ve hataya acik bi kod yigini olacakti bu yuzden=Regex (Regular Expression) kullandim.
     */
    public void setPlaka(String plaka) throws GecersizMiktarException {
        //Plaka Kontrol adimlari
        if (plaka == null) {
            throw new GecersizMiktarException("HATA: Plaka boş olamaz!");
        }

        // Küçük harf girildiyse buyuk  harfe çevirmek ve başta ve sonda(trim) olusmus olabilecek bosluklari temizlemek icin.
        plaka = plaka.trim().toUpperCase();


        // [A-Z0-9 -]=Sadece Harf, Rakam, Bosluk ve Tire sarti icin.
        // {5,15}=En az 5, en cok 15 karakter sarti.
        //"^"Yazi tamamen plaka olmalı anlamina gelmesi icin.
      //"$" Arkasından baska karakter gelmesini engellemek icin.
        String globalPlakaKalibi = "^[A-Z0-9 -]{5,15}$";

        if (!plaka.matches(globalPlakaKalibi)) {
            //Sartlara olan uygunlugunun kontrolu,
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