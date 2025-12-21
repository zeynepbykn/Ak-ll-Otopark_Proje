package model;
// Arac sinifi gibi abstracttir.
public abstract class Abone  {

    //Encapsulation
    private String aboneId;
    private String adSoyad;


    public Abone(String aboneId, String adSoyad) {
//ID ıcın kullanici bazli hata girisleri kontrolü ve donut
        if (aboneId == null || aboneId.trim().isEmpty()) {
            //bu javanin kendi kutuphanesinden bir hatadir yani ayrica ozel hata tanimlamadik zaten bos olma durumu evrensel bi hata
            throw new IllegalArgumentException("Abone ID boş olamaz.");
        }
        this.aboneId = aboneId;
        this.adSoyad = adSoyad;
    }

    //Polimorfizm alt siniflarda tanimli metotu ata sinifta tek bir komutla her nesne icin kendine has sekilde calismasini saglar.
    // Yani gerekli alt siniflar bunu override etmek zorunda.


    public abstract double indirimOraniBelirle();

    //Getter ve Setter.
    //gerekli durumlarda alt siniflar private halinde kullanamayacagi icin set ile gorunur ve kullanilabilir kiliyoruz

    public String getAboneId() {
        return aboneId;
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
