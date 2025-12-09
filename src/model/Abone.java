package model;


import exception.AboneKaydiBulunamadiException;



    public Abone(String aboneId, String adSoyad) {
        this.aboneId = aboneId;
        this.adSoyad = adSoyad;
    }

    // Abstract Metot: Farklı indirim mantığı için
    public abstract double indirimOraniBelirle();

    // --- Getter'lar ve Setter'lar ---
    public String getAboneId() { return aboneId; }
    public String getAdSoyad() { return adSoyad; }

    // Setter Kontrolü 2: Abone ID kontrolü
    public void setAboneId(String aboneId) throws AboneKaydiBulunamadiException {
        if (aboneId == null || aboneId.trim().isEmpty()) {
            throw new AboneKaydiBulunamadiException("Abone ID boş bırakılamaz.");
        }
        this.aboneId = aboneId;
    }
