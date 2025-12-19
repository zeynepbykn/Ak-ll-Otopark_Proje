package model;// Bu sınıfın 'model' (Nesne) kutusunda olduğunu belirtir.

import exception.HataliPlakaException;
import interfaces.Fiyatlanabilir;
import model.Abone;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; //

// Kendi yazdığımız hata sınıfını çağırıyoruz. (Plaka hatalıysa fırlatacağız)
import interfaces.GirisCikisTakip;

/* Abstract sinifi soyuttur bi nevi ust baslik.
Yani bi başlik içinde ortak ozellikleri  toplamak icin kullandildi.
Bu yüzden new Arac() seklinde bir nesne uretilemez ortak bi turle degil spesifik bir seyle nesne uretebiliriz.
Fakat (public oldugu icin) herhangi bir sinif miras alabilir
 */

// GirisCikisTakip sinifi otopark sistemine "Ben giris ve cikis yapabilirim" taahhudu verir.
public abstract class Arac implements GirisCikisTakip, Fiyatlanabilir {
    protected Abone abone; // varsa abone

    public void setAbone(Abone abone) {
        this.abone = abone;
    }

    public  Abone getAbone(){return abone;}

    public double indirimOrani() {
        return (abone != null) ? abone.indirimOraniBelirle() : 0.0;
    }

    /*Encapsulation kavrami ile değişkenleri 'private' yaparak korumaya aliyoruz.
    Sadece get ve set metotları ile  erişilebilirler.*/


    private LocalDateTime girisZamani;
    private Boolean parktaMi;//(true/false) olarak
    private String plaka;
    /* Constructor nesne hafizada olusurken calisir
     */

    //Plaka parametreli constructor
    public Arac(String plaka) {
        try {
            this.plaka = plakaKontrol(plaka);
        } catch (HataliPlakaException e) {
            System.err.println(e.getMessage());
            this.plaka = "HATALI-PLAKA";
        }
        this.parktaMi = false;
    }

    /*Sadece Türk plakasi olmamasi durumunda standart bi sart kosmak problem
    yaratacagi icin bi kontrol sarti olmali fakat cok fazla sart oldugu ıcın standrt bir dongu
    verimsiz ve hataya acik bi kod yigini olacakti bu yuzden=Regex (Regular Expression) kullandim.
     */
    // model/Arac.java içindeki plakaKontrol metodunu bununla değiştir:
    private String plakaKontrol(String plaka) throws HataliPlakaException {
        if (plaka == null) {
            throw new HataliPlakaException("HATA: Plaka boş olamaz!");
        }

        // --- KESİN ÇÖZÜM ---
        // 1. Önce trim() yapıp kenarları temizle.
        // 2. Sonra toUpperCase() ile büyüt.
        // 3. EN ÖNEMLİSİ: replaceAll("\\s+", "") ile ARADAKİ boşlukları da yok et.
        // Örn: " 06  abc 12 "  --->  "06ABC12" olur.
        String temizPlaka = plaka.trim().toUpperCase().replaceAll("\\s+", "");

        // Regex'i de boşluksuz haliyle güncelledim (Tire ve boşlukları kaldırdım)
        String globalPlakaKalibi = "^[A-Z0-9]{5,15}$";

        if (!temizPlaka.matches(globalPlakaKalibi)) {
            throw new HataliPlakaException(
                    "HATA: Plaka geçersiz! (Temizlenmiş hali: " + temizPlaka + ")"
            );
        }

        return temizPlaka;
    }

    //1.Dosyadan okudugumuz eski tarihi buraya set edecegiz.
    public void setGirisZamani(LocalDateTime girisZamani) {
        this.girisZamani = girisZamani;
        this.parktaMi = true;
    }

    //2.Dosyaya yazarken otomobilmi motosikletmi oldugunu anlamak icin
    public String getTip() {
        return this.getClass().getSimpleName();
    }

/* Otopark doluluk ve verimlilik raporu icin alt siniflar bunu doldurmak zorunda.
 Matris (2D Dizi) yapısında teknik olarak her hücre 1 araç tutar Yani aslinda verimlilik acisindan %100 degil fakat
 ArrayList<Arac> ile sadece arclarin girisini kaydetmek yerine matris ile kat konum bilgilerini ozel olarak tutmak istedik*/


    //Aracin bir abonesi varmi(varsa true ypksa false doner)
    public boolean isAbone() {
        return this.abone != null;
    }

    public abstract String yerKaplamaDurumu();

    @Override
    public void girisYap() {
        // Aracın kendi üzerindeki "Saat" ve "Durum" bilgisini güncelliyoruz.
        this.girisZamani = LocalDateTime.now();
        this.parktaMi = true;
        // --- GÜZELLEŞTİRME KISMI ---
        // Tarihi "Gün-Ay-Yıl Saat:Dakika:Saniye" formatına çeviren kalıp
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        // formattedZaman değişkenine o kalıbı uyguluyoruz
        String susluZaman = girisZamani.format(format);

        // Artık ekrana ham veri değil, süslü zamanı yazıyoruz
        System.out.println(plaka + " giriş saati kaydedildi: " + susluZaman);
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


    public LocalDateTime getGirisZamani() {
        return girisZamani;
    }

    //Java'da boolean (evet/hayır) değer döndüren getter metotları get ile değil, genellikle is (öyle mi?) ile başlar.
    public boolean isParktaMi() {
        return parktaMi;
    }

    //Her arac kendi ucretini hesaplayacak.(Polimorfizm)
    //override edilmek zorunda->override edilip o nesneye gore metodun ici doldurulmalı.
    public abstract double odenecekTutar(double sureDakika);

}