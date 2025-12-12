package model;

public class Otomobil extends Arac {

    public Otomobil(String plaka) {
        super(plaka); // Babaya (Arac) plakayı gönder
    }

    @Override
    public String yerKaplamaDurumu() {
        return "Standart binek araç alanı kaplar.";
    }
}