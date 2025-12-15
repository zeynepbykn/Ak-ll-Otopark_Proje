package model;

public class AracOzel extends Arac {

    public AracOzel(String plaka) {
        super(plaka);
    }

    @Override
    public double odenecekTutar(double sureDakika) {
        // Örneğin saatlik ücret 2 TL/dk olarak hesaplanabilir
        double ucret = sureDakika * 0.033; // dakika başına 0.033 TL ≈ 2 TL/saat
        return ucret;
    }

    @Override
    public String yerKaplamaDurumu() {
        // Örnek bir değer döndürebiliriz
        return "Beton";  // veya "Asfalt", ihtiyaca göre değiştir
    }
}

