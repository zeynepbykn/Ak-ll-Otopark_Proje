package service;

import model.AylikAbone;
import model.SaatlikAbone;

import java.time.format.DateTimeFormatter;

import util.DosyaGirisCikisKayit;
import util.DosyaAboneKayit;
import model.Abone;
import model.*;
import exception.OtoparkDoluException;
import model.Arac;
import model.ParkYeri;
import util.UcretHesapla;

import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class OtoparkService {

    // < key-plaka, value-Parkyeri nesnesi >
    private Map<String, ParkYeri> parktakiAraclar; //kayit defteri
    private ParkYeri[][] parkMatrisi; //otoparkin fiziksel binasi-kat ve oda

    public boolean aboneIdFormatiDogruMu(String id) {
        return id.matches("A\\d{3}");
    }

    //Key -> AboneId, Value -> Abone nesnesi(ad soyad ) + abonetipide dosya okudugumuzda eklenir.
    private Map<String, Abone> aboneler = DosyaAboneKayit.aboneListesiniGetir();

    // Get ile aboneleri dondurur.(ıd-adsoyad-abonetipi)
    public Map<String, Abone> getAboneler() {
        return aboneler;
    }

    /*
    "parkMatrisi, otoparkın fiziksel kat ve sütun düzenini temsil eden ve her hücresinde ParkYeri sınıfından oluşturulmuş bir nesne barındıran iki boyutlu bir dizidir.
     ParkYeri sınıfı model paketinden import edilir."
     */
    //otoparkin sinirlari
    //daha sonra bu satır ve sutun isimize yarayacak o yuzden constructor blokunun disina bu degiskenleri tanimladik.(sınır kontrolu)
    private final int MAX_SATIR;
    private final int MAX_SUTUN;

    // Bu sinif ilk "new"lendiginde (Main icinde) burasi çalisir
    public OtoparkService(int satir, int sutun) {

        this.MAX_SATIR = satir;
        this.MAX_SUTUN = sutun;

        this.parktakiAraclar = new HashMap<>();//liste suan bos
// ParkYeri türünde iki boyutlu bir matris için bellek ayırıyoruz
        this.parkMatrisi = new ParkYeri[satir][sutun];

        //yer numarasi bir etiket(mesela 15 numarali parkyeri)
        double baslangicDegeri = 1.0;
        int yerNo = (int) baslangicDegeri; // Acık(Explicit) tip donusumu
//oda numarası 1 den baslatiyoruz.(matrisler[0][0] dir)


// İki boyutlu otopark matrisini ParkYeri nesneleriyle dolduruyoruz
        //dis dongu->katlari geziyor.
        for (int i = 0; i < satir; i++) {
            //ic dongu-> O kattaki odalari geziyor.
            for (int j = 0; j < sutun; j++) {

                //Otoparktaki her konum için bir ParkYeri nesnesi oluşturup matrise yerleştiriyoruz
                this.parkMatrisi[i][j] = new ParkYeri(yerNo, i, j);
                yerNo++;
            }
        }
        // Program açılınca eski kayıtları dosyadan geri yüklüyoruz
        eskiAraclariYukle();
    }

    // --- BU METOT PROGRAM AÇILINCA ÇALIŞIR ---
    private void eskiAraclariYukle() {
        // Dosya sınıfından satırları istiyoruz. Dosya yoksa boş liste gelir, sorun çıkmaz.
        List<String> kayitlar = DosyaGirisCikisKayit.kayitlariOku();

        if (!kayitlar.isEmpty()) {
            System.out.println("Sistem: Dosyadan " + kayitlar.size() + " araç geri yukleniyor...");
        }

        for (String satir : kayitlar) {
            try {
                String[] veri = satir.split(";");
                // Veri sırası: [0]=Plaka, [1]=Tip, [2]=Kat, [3]=Sira, [4]=Zaman

                String plaka = veri[0];
                String tip = veri[1];
                int kat = Integer.parseInt(veri[2]);
                int sira = Integer.parseInt(veri[3]);
                LocalDateTime zaman = LocalDateTime.parse(veri[4]);

                // Dosyada 6. veri (AboneID) var mı diye bakıyoruz. Yoksa "YOK" kabul et.
                String dosyaAboneId = (veri.length > 5) ? veri[5] : "YOK";

                // 1. Aracı Tipine Göre Canlandır
                Arac arac = null;
                if (tip.equals("Otomobil")) {
                    arac = new Otomobil(plaka);
                } else if (tip.equals("Motosiklet")) {
                    arac = new Motosiklet(plaka);
                } else {
                    arac = new Otomobil(plaka); // Bilinmeyen tipse varsayılan olarak otomobil kabul ediyorum
                    //Yoksa dosyada anlamsiz birsey varsa ve bu blok olmazsa asagida giriszamanını aldıgımız zaman program patlar.(NulPointerException)
                }

                // 2. Eski zamanı ayarla (Şimdi girmiş gibi olmasın)
                arac.setGirisZamani(zaman);

                // Eğer dosyada bir abone ID yazıyorsa ve o abone listemizde varsa, araca ekle.
                if (!dosyaAboneId.equals("YOK")) {
                    if (aboneler.containsKey(dosyaAboneId)) //containsKey -> map icinde verilen key varmı yokmu kontrol eder
                    {//aboneler.get ile abone nesnesini getirir. ve arac.setAbone ile araca yükleriz.
                        arac.setAbone(aboneler.get(dosyaAboneId));
                        //aracın abone b
                    }
                }

                // 3. Matrise ve Map'e yerleştir
                ParkYeri yer = parkMatrisi[kat][sira];
                yer.parkEt(arac);
                parktakiAraclar.put(plaka, yer);

                System.out.println("-> Geri yüklendi: " + plaka + " (" + tip + ")");

            } catch (Exception e) {
                System.err.println("Eski kayıt yüklenirken hata: " + e.getMessage());
            }
        }
    }

    //Fail Fast
    public boolean otoparkDoluMu() {
        // 1. Toplam kapasiteyi hesapla (Kat Sayısı x Sıra Sayısı)
        int toplamKapasite = parkMatrisi.length * parkMatrisi[0].length;

        // 2. Şu an içeride kaç araç var? (Map'in boyutuna bakıyoruz)
        int icerdekiAracSayisi = parktakiAraclar.size();

        // 3. Eşitse doludur (true döner), değilse yer vardır (false döner)
        return icerdekiAracSayisi >= toplamKapasite;
    }

    public void aracGiris(Arac arac, int sira, int sutun) throws OtoparkDoluException {

        // Güvenlik Kontrolü: Araç boş mu?
        if (arac == null) {
            System.err.println("HATA: Boş (null) bir araç otoparka giremez!");
            return; // İşlemi anında durdur
        }

        // Sinir kontrolu
        if (sira < 0 || sira >= MAX_SATIR || sutun < 0 || sutun >= MAX_SUTUN) {
            throw new IllegalArgumentException("Geçersiz park yeri seçimi!");
        }

        ParkYeri hedefYer = parkMatrisi[sira][sutun];

        // Hedeflenen kutuyu (nesneyi) seçiyoruz ve "Dolu musun?" diye soruyoruz.
        if (hedefYer.isDoluMu()) {
            throw new OtoparkDoluException("DİKKAT: " + sira + ".Kat, " + sutun + ".sıra zaten dolu!!");
        }
        if (arac.isParktaMi()) {
            throw new IllegalStateException("Araç zaten otoparkta!");
        }

        //Park etme islemi
        //Aracin kendi saatini baslatiyoruz.
        arac.girisYap();

        //Araci matrisin icine koyuyoruz.
        hedefYer.parkEt(arac);

        //Deftere kayit(MAP)
        //Plakayi anahtar, park yerini deger olarak kaydediyoruz.

        /*ParkYeri nesnesi daha önce (Constructor'da) üretilirken içine 'yerNo' yazıldığı için,
         Map'e eklediğimizde yer numarası bilgisi de otomatik olarak kaydedilmiş olur.
       */
        parktakiAraclar.put(arac.getPlaka(), parkMatrisi[sira][sutun]);// o parkMatrisi[i][j]->oradaki nesneyi cagiririz

        String kaydedilecekId = "YOK";//varsayilan yok degeri yazıyoruz.
        if (arac.isAbone()) {//arac abone ise
            kaydedilecekId = arac.getAbone().getAboneId();//abone nesnesinden abone ıd bilgisini bu degiskene atıyoruz.
        }
        // EKLENECEK: Dosyaya giriş kaydı
        DosyaGirisCikisKayit.girisKaydet(arac.getPlaka(), arac.getTip(), sira, sutun, kaydedilecekId);

        //Hedeflenen yerin park numarasini alıyoruz.
        int parkNo = hedefYer.getYerNumarasi();
        //Basarı Mesaji
        System.out.println("✅Araç başariyla Park No: " + parkNo + " " + sira + ". Kat, " + sutun + ". Sıraya park edildi.\n");
        System.out.println("KAYIT: Plaka (" + arac.getPlaka() + ") Otopark listesine kaydedildi.");
    }

    //Cikmak isteyen aracin borccunu hesaplar,tahsil eder ve otoparki siler
    public double aracCikis(String plaka) {
        //Plakadan bize o aracin durdugu parkYeri nesnesini veriyor.
        //get.(key) diyerek value degerini cagiririz.
        ParkYeri yer = parktakiAraclar.get(plaka);
        if (yer == null) {
            throw new IllegalArgumentException("Bu plakaya ait araç otoparkta yok!");

        }

        //bilgileri al
        Arac arac = yer.getParkEdenArac();
        if (arac == null) {
            throw new IllegalStateException("Park yeri dolu görünüyor ama araç yok!");
        }
//Dosyadan aracin giris zamanini aliyoruz ve sonra dosyadan o araci siliyoruz.
        LocalDateTime girisZamani = DosyaGirisCikisKayit.girisZamaniGetirVeSil(plaka);
        if (girisZamani == null) {
            throw new IllegalStateException("Giriş kaydı bulunamadı!");
        }
        LocalDateTime cikisZamani = LocalDateTime.now();

        //util sinifindan  dakikaHesapla ile aracin toplam durdugu dk'yi alıyoruz.
        double sureDakika = UcretHesapla.parkSuresiDakikaHesapla(girisZamani, cikisZamani);


        //double ucret=UcretHesapla.standartUcretHesapla(sureDakika);
        double ucret = arac.odenecekTutar(sureDakika);
        //Park numarasini alıyoruz.
        int parkNo = yer.getYerNumarasi();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String tarifeTipi = "STANDART TARİFE";
        if (arac.isAbone()) {
            tarifeTipi = "ABONE TARİFESİ (%20 İndirim)";
            // Eğer indirim oranı 1.0 ise (Aylık Abone)
            if (arac.indirimOrani() == 1.0) {
                tarifeTipi = "AYLIK ABONE (Ücretsiz)";
            }
        }
        System.out.println("\n=================================");
        System.out.println("       OTOPARK ÇIKIŞ FİŞİ       ");
        System.out.println("=================================");
        System.out.println("Araç Plakası : " + plaka);
        System.out.println("Park Yeri No : " + parkNo);
        System.out.println("Tarife Tipi  : " + tarifeTipi); // <-- BURASI EKLENDİ
        System.out.println("Giriş Zamanı : " + girisZamani.format(format));
        System.out.println("Çıkış Zamanı : " + cikisZamani.format(format));
        System.out.println("Toplam Süre  : " + (int) sureDakika + " dakika");
        System.out.println("=================================\n");
        // ----------------------------------------------

        yer.cikisYap();//ParkYeri nesnesini bosalt.
        parktakiAraclar.remove(plaka);//Defterden (MAP) kaydi siliniyor.
        return ucret;

    }


    public boolean yeniAboneEkle(String id, String adSoyad, String tip) {

        // FORMAT KONTROLÜ
        if (!aboneIdFormatiDogruMu(id)) {
            return false;
        }

        // AYNI ID VAR MI
        if (aboneler.containsKey(id)) { //true yada false doner
            return false;
        }

        // Abone ekle metodu ile Dosyaya yaz.
        DosyaAboneKayit.aboneEkle(id, adSoyad, tip);

        // Map'e ekle
        aboneler.put(id, tip.equalsIgnoreCase("Aylik")
                ? new AylikAbone(id, adSoyad)
                : new SaatlikAbone(id, adSoyad));

        return true;
    }


    //Raporlama ve Gorsellestirme
    //Otoparkin guncel durumunu dis siniflara göstermek icin kullanilir.
    // ! parkMatrisi private oldugundan getter metodu ile otoparkın anlık doluluk gorselini gorebiliriz.
    public ParkYeri[][] getParkMatrisi() {
        return parkMatrisi;

    }

}

