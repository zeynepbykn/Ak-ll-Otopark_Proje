package model;

public enum AracTipi {
    //Enum sabitleri(Genelde buyuk harfle yazılır)
     OTOMOBIL("Otomobil"),
    MOTOSIKLET("Motosiklet");

     // Enum'ın içinde bir değer tutmak için değişken
    private final String tipIsmi;

    // Constructor (Enum'a özel)
    AracTipi(String tipIsmi) {
        this.tipIsmi = tipIsmi;
    }

    // Değeri okumak için
    public String getTipIsmi() {
        return tipIsmi;
    }
}
