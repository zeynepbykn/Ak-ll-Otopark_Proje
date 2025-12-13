package model;

public class Otomobil extends Arac {

    public Otomobil(String plaka) {
        super(plaka);
    }

    //abstract metod olan yerKaplamaDurumu metodunu override etmek zorundaydik..
    @Override
    public String yerKaplamaDurumu() {
        return "Binek arac(2 birim)";
    }

    //Arac sinifinin zorunlu metodu(fiyat hesaplama)
    @Override
    public double odenecekTutar(double sureDakika) {
//Ornek tarife : Saati 20TL;
        double saatlikUcret = 20.0;

        double toplamTutar = (sureDakika / 60.0) * saatlikUcret;
        System.out.println("Hesaplama: Otomobil tairfesi(saati 20 TL) uygulandi.");
        return toplamTutar;
    }
}
