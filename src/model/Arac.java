package model;

//Projede model package da bulunmayan siniflari kullanabilmek icin tanitiyoruz.
import exception.HataliPlakaException;
import interfaces.Fiyatlanabilir;
import interfaces.GirisCikisTakip;

//Javanin kendi içinde bulunan tarih/saat kutuphanesinde yararlabilmek icin kullandık.
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Abstract sinifi soyuttur bi nevi ust baslik. Yani bi başlik içinde ortak ozellikleri  toplamak icin kullandildi.
Ortak bi turle degil spesifik bir seyle nesne uretebiliriz bu yüzden new Arac() seklinde bir nesne uretilemez.
Fakat (public oldugu icin) herhangi bir sinif miras alabilir.*/

// GirisCikisTakip interfacei tum araclarin giris-cikis islemi yapmasini garanti ediyor.
public abstract class Arac implements GirisCikisTakip, Fiyatlanabilir {

    //Protected yapip miras almaya acik hale getirdik.
   //Her aracin bir abonesi olabilecegi icin ona yer ayiriyor.(Car Has-A Subscriber=Association ilişki)
    protected Abone abone;

//Protected degiskeni disaridan gelen gecici degiskene esitlemek icin.
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

    // Constructor nesne hafizada olusurken calisir bu sebepten eş zamanlı hata kontrolu ile bilgi karmasasi ve kirliligini onler
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

    private String plakaKontrol(String plaka) throws HataliPlakaException {
        if (plaka == null) {
            throw new HataliPlakaException("HATA: Plaka boş olamaz!");
        }

        //trim() metodu ile bas ve son bosluklarini temizledik. toUpperCase() ile Plaka girdisini buyuk harflerle yazdirdik.
        //replaceAll("\\s+", "") ile ara boşluklari temizledik. Boylece kullanicidan alinan plakayi min hata ciktisi ile duzenleyip kullanabiliriz.
        String temizPlaka = plaka.trim().toUpperCase().replaceAll("\\s+", "");

        // Regex'i burada sartlandirdik uzunlugu da sadece Türk plakasi olmamasi ihtimali ile genis tuttuk.
        //^ Bu kural en bastan itibaren uygulansin diye.
        String globalPlakaKalibi = "^[A-Z0-9]{5,15}$";

        if (!temizPlaka.matches(globalPlakaKalibi)) {
            throw new HataliPlakaException(
                    //hata firlatma sisteme hatali plaka kacmasin nesne hic olusturmasin diye.
                    "HATA: Plaka geçersiz! (Temizlenmiş hali: " + temizPlaka + ")");
        }
        return temizPlaka;
    }

    //Giris saatimi anlik verilerle degil gercek zamanli kaydedip gerektiginde kayitli veriyi hatirlasin diye.
    public void setGirisZamani(LocalDateTime girisZamani) {
        this.girisZamani = girisZamani;
        this.parktaMi = true;
    }

    //Dosyaya yazarken aynalama yapiyo yani tek tek tur belirtmek yerine
    public String getTip() {
        return this.getClass().getSimpleName();
    }

    //Aracin bir abonesi varmi(varsa true yoksa false doner)
    public boolean isAbone() {
        return this.abone != null;
    }
//gelistirilebilirlik icin biraktik
    public abstract String yerKaplamaDurumu();

    @Override
    public void girisYap() {

        // Aracın kendi üzerindeki "Saat" ve "Durum" bilgisini güncelliyoruz.
        this.girisZamani = LocalDateTime.now();
        this.parktaMi = true;

        // Tarihi okunabilir formata donusturur
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        // formattedZaman değişkenine o kalıbı uyguluyoruz
        String susluZaman = girisZamani.format(format);
        System.out.println(plaka + "Giriş saati kaydedildi: " + susluZaman);
    }

    @Override
    public void cikisYap() {
        // Arac cikis islemleri.
        this.parktaMi = false;
        System.out.println(plaka + "Çıkış işlemi başlatıldı.");
    }

    //Getter & Setter (Kontrollü Erişim) icin ve sadece plakanin ne oldugunu gostermek icin
    //Daha sonra diger siniflarda baska metotlarda ihityac duyulacagi icin
    //Encapsulation (Kapsülleme)
    public String getPlaka() {
        return plaka;
    }

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