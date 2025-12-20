package model;

//Miras almayi saglamak icin extends
public class Otomobil extends Arac {

//plakayi ata sinifa gonderiyor plaka cagirmali cunku ata sinif constructorı plaka istiyor.
    public Otomobil(String plaka) {
        super(plaka);
    }

    //abstract metod olan yerKaplamaDurumu metodunu override etmek zorundaydik.
    @Override
    public String yerKaplamaDurumu() {
        return "Binek arac(2 birim)";
    }

    //Arac sinifinin zorunlu metodu(fiyat hesaplama)
    @Override
    public double odenecekTutar(double sureDakika) {
        Double saatlikUcret = 20.0;

        double normalTutar = (sureDakika / 60.0) * saatlikUcret;
        double indirim = normalTutar * indirimOrani();

        System.out.println("Hesaplama: Otomobil tarifesi (Saati 20 TL) uygulandı.");
        return normalTutar - indirim;
    }
}
