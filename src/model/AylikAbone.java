package model;

public class AylikAbone extends Abone {

   //Aylık abone, otoparkın parasını ay başında pesin oder o yuzden ındırım orani 1 yani ucret yok

    // Esneklik icin yine de bir indirim oranı degiskeni var.
    private double aylikIndirimOrani = 1.0;

    //Constructor
    public AylikAbone(String aboneId, String adSoyad) {
        super(aboneId, adSoyad);
    }

    //Polimorfizm
    @Override
    public double indirimOraniBelirle() {
        return aylikIndirimOrani;
    }

    //Setter indirim degistirmek istersek
    public void setAylikIndirimOrani(double yeniOran) {
        if (yeniOran < 0 || yeniOran > 1.0) {
            System.err.println("❌HATA: Geçersiz oran!");
            return;
        }
        this.aylikIndirimOrani = yeniOran;
    }

    // Raporlama için toString kullanıyoruz
    @Override
    public String toString() {
        return super.toString() + " (Tip: Aylık Abone - Ücretsiz Çıkış)";
    }
}