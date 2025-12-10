package model;

import exception.GecersizMiktarException;

public class ParkYeri {

    // Ozellikleri tanimladim
    private int yerNumarasi;
    private int sira;        // Matris satiri (X koordinati)
    private int sutun;       // Matris sutunu (Y koordinati)
    private boolean doluMu;

    // Composition=Sahip olabilme yani bir arac degil, bir arac baridirabilir.

    private Arac parkEdenArac;

    // Contructor=kurucu metot yani nesne olusturuldugu an calısan ilk yer.

    public ParkYeri(int yerNumarasi, int sira, int sutun) {
        //this.:private degisken olan yer numarasidir kalicidir.
        //=yerNumarasi:parametre olan degerdir gecicidir.
        this.yerNumarasi = yerNumarasi;

        /*Direkt atama yapmak yerine once bi kontrolden geciriyoruz cunku
        belirledigimiz matris degerlerinin disina cikilmasi program akisinda hataya neden olabilir.*/
        try {
            setSira(sira);
            setSutun(sutun);
        } catch (GecersizMiktarException hataKutusu) {
            /*yakalama yapiyo cunkü hem akis bozulmuyo yanş programş kapatmiyo hem de
            hata programi patlatamiyo.
             */

            System.err.println("Park Yeri Hatası: Sıra negatif olamaz!" + hataKutusu.getMessage());
            System.err.println(">>> Gönderilen Hatalı Değerler: Sıra=" + sira + ", Sütun=" + sutun);
            // Hata olsa bile program çökmesin diye varsayılan değer (0) atıyoruz
            /*Yani bi yer acilip icinde kullanissiz degerler kalmassin diye o yeri yine olusturmus oluyoruz
            ama kullanimi olmasin diye 0 olarak atiyoruz bi nevi var olmayan bi yere,cope atmak icin,
             */
            this.sira = 0;
            this.sutun = 0;
            /* Bunu ekliyoruz cunku hata vermemesi adına [0,0] konumuna
            atayinca bu sefer matriste gercekten var olan [0,0]
            konumu isgal edebilir hatali olup orda bekledigini bi sekilde ayirt etmekiyiz.
            o yuzden bi nevi belirtec yapıp sonra gereli sinifta burdaki atamaya gore sartlandirip
            kullanmayalim.
            */
            this.yerNumarasi = -1;
        }

        this.doluMu = false; //Ilk olustugunda bos
    }

    //Setterlar

    public void setSira(int sira) throws GecersizMiktarException {
        // Matriste gecerli sıra numarasi sart
        if (sira < 0) {
            throw new GecersizMiktarException("HATA: Sıra numarası negatif olamaz! (" + sira + ")");
        }
        this.sira = sira;
    }

    public void setSutun(int sutun) throws GecersizMiktarException {
        //Matriste gecerli sutun numarası sart.
        if (sutun < 0) {
            throw new GecersizMiktarException("HATA: Sütun numarası negatif olamaz! (" + sutun + ")");
        }
        this.sutun = sutun;
    }


    //Aracı iceri almak icin metot
    public void parkEt(Arac arac) {
        this.parkEdenArac = arac;
        this.doluMu = true;
        //Disaridan bakabilmek adina yazdiriyoruz ki bi nevi tablo olusturabilelim.
        System.out.println("Park Yeri [" + sira + "," + sutun + "] doldu -> " + arac.getPlaka());
    }

    //Araci disari cikarmak icin metot
    public void cikisYap() {
        this.parkEdenArac = null;
        this.doluMu = false;
        System.out.println("Park Yeri [" + sira + "," + sutun + "] boşaldı.");
    }

    //Park halindeki aracin bilgilerini gormek icin.
    public Arac getParkEdenArac() {
        return parkEdenArac;
    }

    //Getterlar
    public int getYerNumarasi() {
        return yerNumarasi;
    }

    public int getSira() {
        return sira;
    }

    public int getSutun() {
        return sutun;
    }

    public boolean isDoluMu() {
        return doluMu;
    }

    //Okunabilir string bir cikti vermesi icin adres olarak vermemesi icin;
    @Override
    public String toString() {
        return "ParkYeri [" + sira + "," + sutun + "] - " + (doluMu ? "DOLU" : "BOŞ");
    }
}