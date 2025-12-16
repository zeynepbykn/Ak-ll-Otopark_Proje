package service;

import model.AylikAbone;
import model.SaatlikAbone;

import util.DosyaGirisCikisKayit;
import util.DosyaAboneKayit;
import model.Abone;

import exception.OtoparkDoluException;
import model.Arac;
import model.ParkYeri;
import util.UcretHesapla;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class OtoparkService {

    // < key-plaka, value-Parkyeri nesnesi >
    private Map<String, ParkYeri> parktakiAraclar; //kayit defteri
    // OtoparkService içinde
    public Map<String, Abone> getAboneler() {
        return aboneler;
    }

    /*
    "parkMatrisi, otoparkın fiziksel kat ve sütun düzenini temsil eden ve her hücresinde ParkYeri sınıfından oluşturulmuş bir nesne barındıran iki boyutlu bir dizidir.
     ParkYeri sınıfı model paketinden import edilir."
     */
    private ParkYeri[][] parkMatrisi; //otoparkin fiziksel binasi-kat ve oda
    //otoparkin sinirlari
    //daha sonra bu satır ve sutun isimize yarayacak o yuzden constructor blokunun disina bu degiskenleri tanimladik.
    private final int MAX_SATIR;
    private final int MAX_SUTUN;
    private Map<String, Abone> aboneler = DosyaAboneKayit.aboneListesiniGetir();

    // Bu sinif ilk "new"lendiginde (Main icinde) burasi çalisir
    public OtoparkService(int satir, int sutun) {

        this.MAX_SATIR = satir;
        this.MAX_SUTUN = sutun;


        this.parktakiAraclar = new HashMap<>();//liste suan bos
// ParkYeri türünde iki boyutlu bir matris için bellek ayırıyoruz
        this.parkMatrisi = new ParkYeri[satir][sutun];

        //yer numarasi bir etiket(mesela 15 numarali parkyeri)
        int yerNo = 1;//oda numarası 1 den baslatiyoruz.(matrisler[0][0] dir)
        //dis dongu->katlari geziyor.

// İki boyutlu otopark matrisini ParkYeri nesneleriyle dolduruyoruz
        for (int i = 0; i < satir; i++) {
            //ic dongu-> O kattaki odalari geziyor.
            for (int j = 0; j < sutun; j++) {

                //Otoparktaki her konum için bir ParkYeri nesnesi oluşturup matrise yerleştiriyoruz
                this.parkMatrisi[i][j] = new ParkYeri(yerNo, i, j);
                yerNo++;
            }
        }
    }


    public void aracGiris(Arac arac, int sira, int sutun) throws OtoparkDoluException {

        // 1. Güvenlik Kontrolü: Araç boş mu?
        if (arac == null) {
            System.out.println("HATA: Boş (null) bir araç otoparka giremez!");
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

        // EKLENECEK: Dosyaya giriş kaydı
        DosyaGirisCikisKayit.girisKaydet(arac.getPlaka());

        //Basarı Mesaji
        System.out.println("Arac basariyla " + sira + ". Kat, " + sutun + ". Sıraya park edildi.");
        System.out.println("KAYT: Plaka (" + arac.getPlaka() + ") Map listesine kaydedildi.");
    }

    //Cikmak isteyen aracin borccunu hesaplar,tahsil eder ve otoparki siler
    public double aracCikis(String plaka)  {
        //Plakadan bize o aracin durdugu parkYeri nesnesini veriyor.
        //get.(key) diuerek value degerini cagiririz.
        ParkYeri yer = parktakiAraclar.get(plaka);
        if (yer == null) {
            throw new IllegalArgumentException("Bu plakaya ait araç otoparkta yok!");

        }

        //bilgileri al
        Arac arac = yer.getParkEdenArac();
        if (arac == null) {
            throw new IllegalStateException("Park yeri dolu görünüyor ama araç yok!");
        }

        LocalDateTime girisZamani = DosyaGirisCikisKayit.girisZamaniGetirVeSil(plaka);
        if (girisZamani == null) {
            throw new IllegalStateException("Giriş kaydı bulunamadı!");
        }
        LocalDateTime cikisZamani = LocalDateTime.now();

        //util sinifindan  dakikaHesapla ile aracin toplam durdugu dk'yi alıyoruz.
        double sureDakika = UcretHesapla.parkSuresiDakikaHesapla(girisZamani, cikisZamani);


        //double ucret=UcretHesapla.standartUcretHesapla(sureDakika);
        double ucret = arac.odenecekTutar(sureDakika);

        yer.cikisYap();//ParkYeri nesnesini bosalt.
        parktakiAraclar.remove(plaka);//Defterden (MAP) kaydi siliniyor.
        return ucret;

    }

    public void yeniAboneEkle(String id, String adSoyad, String tip) {
        // Dosyaya yaz
        DosyaAboneKayit.aboneEkle(id, adSoyad, tip);

        // Runtime Map güncellemesi
        aboneler.put(id, tip.equalsIgnoreCase("Aylik")
                ? new AylikAbone(id, adSoyad)
                : new SaatlikAbone(id, adSoyad));
    }
    //Raporlama ve Gorsellestirme
    //Otoparkin guncel durumunu dis siniflara göstermek icin kullanilir.
    // !parkMatrisi private oldugundan getter metodu ile otoparkın anlık doluluk gorselini gorebiliriz.
    public ParkYeri[][] getParkMatrisi() {
        return parkMatrisi;

    }

}