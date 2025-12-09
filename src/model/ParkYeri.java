package model;
import exception.OtoparkDoluException; // İleride lazım olacak
import exception.GecersizMiktarException; // İleride lazım olacak
public class ParkYeri {

    // 1. ÖZELLİKLER (Fields)
    private int yerNumarasi; // Tabeladaki numara (Örn: 1, 2, 3)
    private int sira;        // Kat numarası (X koordinatı)
    private int sutun;       // Oda numarası (Y koordinatı)
    private boolean doluMu;  // Dolu mu boş mu?

    private Arac parkEdenArac;
    // Şimdilik Aracı tutacak değişkeni yazmıyoruz, kafa karışmasın.
    // Önce temeli kuralım.

    // 2. KURUCU METOT (CONSTRUCTOR) - HATAYI ÇÖZEN KISIM
    // OtoparkService'den gönderilen 3 sayıyı burada karşılıyoruz.
    public ParkYeri(int yerNumarasi, int sira, int sutun) {
        this.yerNumarasi = yerNumarasi;
        this.sira = sira;
        this.sutun = sutun;
        this.doluMu = false; // İlk üretildiğinde boş olsun
    }

    // 1. Aracı içeri alma (parkEt)
    public void parkEt(Arac arac) {
        this.parkEdenArac = arac; // Aracı değişkenin içine koy
        this.doluMu = true;       // Bayrağı kaldır: DOLU
    }

    // 2. Aracı dışarı çıkarma (cikisYap)
    // Birazdan çıkış işleminde lazım olacak
    public void cikisYap() {
        this.parkEdenArac = null; // Aracı sil
        this.doluMu = false;      // Bayrağı indir: BOŞ
    }

    // 3. İçindeki aracı görme
    public Arac getParkEdenArac() {
        return parkEdenArac;
    }

    // 3. GETTER METOTLARI (Dışarıdan okumak için)
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

    // Durumu değiştirmek için basit bir Setter (Şimdilik)
    public void setDoluMu(boolean doluMu) {
        this.doluMu = doluMu;
    }
}
