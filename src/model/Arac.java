package model;

import java.time.LocalDateTime;
import exception.GecersizMiktarException;

public abstract class Arac {
    private String plaka;
    private LocalDateTime girisZamani;
    private boolean parktaMi;

    public Arac(String plaka) {
        this.plaka = plaka;
        this.parktaMi = false;
    }

    public abstract String yerKaplamaDurumu();

    public LocalDateTime girisYap() {
        this.girisZamani = LocalDateTime.now();
        this.parktaMi = true;
        return this.girisZamani;
    }

    // --- EKSIK OLAN GETTER BURASI ---
    public LocalDateTime getGirisZamani() {
        return girisZamani;
    }
    // --------------------------------

    public String getPlaka() { return plaka; }

    public boolean isParktaMi() { return parktaMi; }

    public void setPlaka(String plaka) throws GecersizMiktarException {
        if ( plaka == null || plaka.length() < 5) {
            throw new GecersizMiktarException("Plaka uzunluğu 5 karakterden az olamaz.");
        }
        this.plaka = plaka;
    }

    public void setParktaMi(boolean parktaMi) {
        this.parktaMi = parktaMi;
    }
}