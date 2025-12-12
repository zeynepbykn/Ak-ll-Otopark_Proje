package model;

import interfaces.UcretHesaplayici;

public class AylikAbone extends Abone {

   /* Aylık abone, otoparkın parasını ay başında toplu olarak (Peşin) ödemiştir.

    Senin yazdığın kod, Çıkış Kapısındaki Bariyeri kontrol eden koddur.

    Bariyer sisteme sorar: "Bu adamın çıkarken ödemesi gereken borcu var mı?"

    Sistem cevap verir: "Hayır, indirim oranı 1.0 (%100). Yani şu an ödeyeceği tutar 0 TL."*/
    // Biz yine de bir indirim oranı değişkeni tutalım.
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

    //Setter
    public void setAylikIndirimOrani(double yeniOran) {
        if (yeniOran < 0 || yeniOran > 1.0) {
            System.err.println("HATA: Geçersiz oran!");
            return;
        }
        this.aylikIndirimOrani = yeniOran;
    }


    // ****Parametreler "LocalDateTime" DEĞİL, "double" olmalı.
    // Proje Planındaki Talimat: "return 0 yaz."
    // Mantık: Aylık abone ücretini ay başında peşin öder.
    // Otoparktan çıkarken bariyerde "0 TL" yazar, cebinden para çıkmaz.
    @Override
    public double ucretHesapla(double sure, double saatlikUcret) {
        // Aylık aboneler çıkışta bariyerde para ödemez.
        return 0.0;
    }

    // Raporlama için toString kullanıyoruz
    @Override
    public String toString() {
        return super.toString() + " (Tip: Aylık Abone - Ücretsiz Çıkış)";
    }
}