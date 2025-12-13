package model;

import interfaces.UcretHesaplayici;
import exception.AboneKaydiBulunamadiException;

public abstract class Abone implements UcretHesaplayici {
    //Encapsulation
    private String aboneId;
    private String adSoyad;

    /*Constructor. Direkt this. deyip atamak yerine yine hata kontrolu saglıyoruz
    bi problem varsa hataKutusunda hata tutuluyor.
     */
    public Abone(String aboneId, String adSoyad) {
        try {
            setAboneId(aboneId);
        } catch (AboneKaydiBulunamadiException hataKutusu) {
            System.out.println(">>> HATA: Abone oluşturulurken ID hatası: " + hataKutusu.getMessage());
            this.aboneId = "HATALI-ID"; // Program çökmesin, hatalı olarak işaretlensin.
        }

        this.adSoyad = adSoyad;
    }

    // Abstract Metot(Polimorfizm): Farklı indirim mantığı için
    /*Polimorfizm=alt siniflarda tanimli metotu ata sinifta tek bir komutla her nesne
    icin kendine has sekilde calismasini saglayan komut.
  */
    // Yani gerekli alt siniflar bunu override etmek zorunda.
    public abstract double indirimOraniBelirle();

    //Getter ve Setter.
    /*gerekli durumlarda alt siniflar private halinde
     kullanamayacagi icin set ile gorunur ve kullanilabilir kiliyoruz
     */
    public String getAboneId() {
        return aboneId;
    }

    //Abone ID kontrolu saglamak icin(Validation=Dogrulama)
    public void setAboneId(String aboneId) throws AboneKaydiBulunamadiException {
        /*Eger herhangi bir deger girilmediyse ya da sadece bosluk tusuna basilmis ise
        cunku( aboneId.trim().isEmpty())) burada bosluklari temizlemek icin kullandim
        hata verir.
        Bunu program cokmesin karmasa olmasin diye yaptirdik
         */
        if (aboneId == null || aboneId.trim().isEmpty()) {
            throw new AboneKaydiBulunamadiException("Abone ID boş bırakılamaz.");
        }
        //Her sey yolundaysa atama islemi.
        this.aboneId = aboneId;
    }

    /*isim ve soyisim de herhangi bi dogrulma bir sart bulundurmadim cünkü
    aslında bi veri saklama ya da kontrolu ıcın degılde sadece ıd tek basina
    kalmasin  cikti isimde icersin ve daha guzel gorunsun diye isim almak istedik.
     */
    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    // Adres olarak degilde string cikti verip duzgun gozukmesi icin.
    @Override
    public String toString() {
        return "Abone ID: " + aboneId + " | İsim: " + adSoyad;
    }
}
