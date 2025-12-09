package service;

import exception.AboneKaydiBulunamadiException;
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

    /*
    "parkMatrisi, otoparkın fiziksel kat ve sütun düzenini temsil eden ve her hücresinde ParkYeri sınıfından oluşturulmuş bir nesne barındıran iki boyutlu bir dizidir.
     ParkYeri sınıfı model paketinden import edilir."
     */
    private ParkYeri[][] parkMatrisi; //otoparkin fiziksel binasi-kat ve oda
    //otoparkin sinirlari
    //daha sonra bu satır ve sutun isimize yarayacak o yuzden constructor blokunun disina bu degiskenleri tanimladik.
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
        // Eğer satır 0'dan küçükse VEYA sınırdan büyükse hata ver.
        if (sira < 0 || sira >= MAX_SATIR || sutun < 0 || sutun >= MAX_SUTUN) {
            throw new OtoparkDoluException("Hata Gecersiz yer secimi !! Otopark sinirlari disinda.");
        }
        // Hedeflenen kutuyu (nesneyi) seçiyoruz ve "Dolu musun?" diye soruyoruz.
        if (parkMatrisi[sira][sutun].isDoluMu()) {
            throw new OtoparkDoluException("Hata :" + sira + ".kat" + sutun + " .sira zaten dolu!!");

        }
        //Park etme islemi
        //Aracin kendi saatini baslatiyoruz.
        arac.girisYap();

        //Araci matrisin icine koyuyoruz.
        //Artik o ParkYeri nesnesi bos degil.Icinde  bu araci sakliyor.
        parkMatrisi[sira][sutun].parkEt(arac);

        //Deftere kayit(MAP)
        //Plakayi anahtar, park yerini deger olarak kaydediyoruz.

        /*ParkYeri nesnesi daha önce (Constructor'da) üretilirken içine 'yerNo' yazıldığı için,
         Map'e eklediğimizde yer numarası bilgisi de otomatik olarak kaydedilmiş olur.
       */
        parktakiAraclar.put(arac.getPlaka(),parkMatrisi[sira][sutun]);// o parkMatrisi[i][j]->oradaki nesneyi cagiririz
    }

//Cikmak isteyen aracin borccunu hesaplar,tahsil eder ve otoparki siler
    public double aracCikis(String plaka) throws AboneKaydiBulunamadiException{
        //Plakadan bize o aracin durdugu parkYeri nesnesini veriyor.
        ParkYeri yer=parktakiAraclar.get(plaka);
        if(yer==null){
            throw new AboneKaydiBulunamadiException("Hata : Bu playa ait arac otoparkta bulunmuyor!.");
        }

        //bilgileri al
        Arac arac=yer.getParkEdenArac();
        LocalDateTime cikisZamani=LocalDateTime.now();//suan cikiyor.
        //util sinifindan  dakikaHesapla ile aracin toplam durdugu dk'yi alıyoruz.
        double sureDakika= UcretHesapla.parkSuresiDakikaHesapla(arac.getGirisZamani(),cikisZamani);
        double ucret=UcretHesapla.standartUcretHesapla(sureDakika);

        yer.cikisYap();//ParkYeri nesnesini bosalt.
        parktakiAraclar.remove(plaka);//Defterden (MAP) kaydi siliniyor.
        return ucret;

    }
    //Raporlama ve Gorsellestirme
    //Otoparkin guncel durumunu dis siniflara göstermek icin kullanilir.
    // !parkMatrisi private oldugundan getter metodu ile otoparkın anlık doluluk gorselini gorebiliriz.
    public  ParkYeri[][] getParkMatrisi(){
        return parkMatrisi;

    }

}
