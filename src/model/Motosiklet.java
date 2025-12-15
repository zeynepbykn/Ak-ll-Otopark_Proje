package model;

public class Motosiklet extends Arac {

    public Motosiklet(String plaka) {
        super(plaka);
    }

    @Override
    public String yerKaplamaDurumu() {
        return "Motosiklet (1 birim yer kaplar)";
    }

    //Fiyat Hesaplama(zorunlu override metod)
    @Override
    public double odenecekTutar(double sureDakika) {
        //Motosiklet tarifesi: Saati 10 TL
        double saatlikUcret = 10.0;
        double normalTutar = (sureDakika / 60.0) * saatlikUcret;
        double indirim = normalTutar * indirimOrani();
        System.out.println("Hesaplama: Motosiklet tarifesi(Saati 10TL) uygulandi.");
        return normalTutar - indirim;
    }
}
