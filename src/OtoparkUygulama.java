import model.*;
import service.OtoparkService;
import util.Raporlayici;
import model.AracTipi;

import java.util.Scanner;

public class OtoparkUygulama {
    // Tüm static metotların (main ve yardımcılar) ortak kullanabilmesi için sınıf seviyesinde tanımlanan giriş aracı.
    //her metodda new scanner dememize gerek kalmaz(new..- bellek yönetimi)
    private static Scanner tarayici = new Scanner(System.in);

    public static void main(String[] args) {

        //Otoparki hazirliyoruz.(3 kat,5 sıra)
        System.out.println("Sistem baslatiliyor...");

        // (DIZI + DATE + STRING)
        String[] gunler = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
//Pazartesi için 1 döner. Ama diziler 0'dan başlar. O yüzden -1 yaparak dizideki doğru günü bulduk.
        int gunIndex = java.time.LocalDate.now().getDayOfWeek().getValue() - 1;
        String bugun = gunler[gunIndex];

        String bugunBuyuk = bugun.toUpperCase();
        System.out.println("Bugün günlerden:" + bugunBuyuk);


        OtoparkService service = new OtoparkService(3, 5);

        boolean devamMi = true; //Dongu anahtari
        //Kullanıcının klavyeden basacağı harfi (a, b, c, q...) tutmak için geçici bir kutu
        char secim = ' ';

        //dongu
        while (devamMi) {
            System.out.println("\n---------AKILLI OTOPARK MENÜSÜ---------");
            System.out.println("->Araç Girişi Yapmak İçin -a- tuşlayınız.");
            System.out.println("-----------------------------------------");
            System.out.println("->Araç Çıkışı Yapmak İçin -b- tuşlayınız.");
            System.out.println("-----------------------------------------");
            System.out.println("->Otopark Durumunu Görmek İçin -c- tuşlayınız.");
            System.out.println("-----------------------------------------");
            System.out.println("->Yeni Abone Eklemek İçin -d- tuşlayınız.");
            System.out.println("-----------------------------------------");
            System.out.println("->Aboneleri Listelemek İçin -e- tuşlayınız.");
            System.out.println("-----------------------------------------");
            System.out.println("->Parktaki Araç Sayısını Görmek İçin -f- tuşlayınız.");
            System.out.println("-----------------------------------------");
            System.out.println("->Çıkış Yapmak İçin -q- tuşlayınız.");
            System.out.println("-----------------------------------------");
            System.out.println("Seçiminiz: ");



            try {
                String girdi = tarayici.next().toLowerCase();
                secim = girdi.charAt(0);
                tarayici.nextLine();
            } catch (Exception e) {
                System.err.println("❌Hata: Lütfen sadece sayı giriniz!");
                tarayici.nextLine();//hatali girdiyi temizler.
                continue; //Dongumuzun basina doner.
            } //Secime Gore Yonlendirme
            switch (secim) {
                case 'a':
                    aracGirisEkrani(service);
                    break;
                case 'b':
                    aracCikisEkrani(service);
                    break;
                case 'c':
                    Raporlayici.matrisiKonsolaYazdir(service.getParkMatrisi());
                    break;
                case 'd':
                    aboneEkleEkrani(service);
                    break;
                case 'e'://k=anahtar v==value
                    service.getAboneler().forEach((k, v) -> System.out.println(k + " -> " + v));
                    break;

                case 'f':
                    // Service'ten matrisi (binanın planını) istiyoruz
                    ParkYeri[][] matris = service.getParkMatrisi();
                    int sayac = 0;

                    // Tüm katları ve sıraları tek tek geziyoruz
                    for (int i = 0; i < matris.length; i++) {
                        for (int j = 0; j < matris[i].length; j++) {
                            //park yeri doluysa sayacı artır
                            if (matris[i][j].isDoluMu()) {
                                sayac++;
                            }
                        }
                    }

                    System.out.println("Parktaki anlık arac sayisi: " + sayac);
                    break;

                case 'q':
                    System.out.println("Sistem kapatiliyor.İyi günler!😊");
                    devamMi = false;
                    break;
                default:
                    System.err.println("❌Hatalı seçim! Tekrar deneyiniz.");

            }
        }

    }

    //Kullanicidan gerekli tum bilgileri(Plaka,tip ,yer..) burada toplar,ve srvice e gonderir.
//bu metot topladıgı bilgileri service'e isler.


    // int sayi okunurken sondaki \n okunmaz ve bufferda kalir sonraki okumada sıkıntı cıkmasın diye onu temizliyoruz.
    //nextLine() -->  \n dahil olmak uzere herseyi tuketir.

    private static void aracGirisEkrani(OtoparkService service) {
        //Otoparkın kapasitesinin dolu olup olmadıgını kontrol etme
        if (service.otoparkDoluMu()) {
            System.err.println("\n🛑 HATA: OTOPARK TAMAMEN DOLU! 🛑");
            System.out.println(">>> ❗Kapasite dolu olduğu için yeni araç girişi yapılamaz.");
            return; // Metodu burada kesip ana menüye fırlatır.
        }
        System.out.println("\n--->Araç girişi: ");
        System.out.print("Plaka: ");

        String plaka = tarayici.nextLine().toUpperCase().replaceAll("\\s+", "");

        if (plaka.trim().isEmpty()) {
            System.err.println("❌Hata: Plaka boş olamaz!");
            return;
        }
        //OtoparkUygulama.java -> aracGirisEkrani metodu
        System.out.println("Tip (1-Otomobil---2-Motosiklet): ");
        int tip = -1;

        try {
            tip = tarayici.nextInt();
            tarayici.nextLine(); // Buffer temizliği
        } catch (Exception e) {
            System.err.println("❌Hata: Sayı girmelisiniz!");
            tarayici.nextLine();
            return;
        }

        AracTipi secilenTip = null;
        // Kullanıcının girdiği sayıyı (1 veya 2) bizim Enum yapımıza çeviriyoruz.
        if (tip == 1) {
            secilenTip = AracTipi.OTOMOBIL;
        } else if (tip == 2) {
            secilenTip = AracTipi.MOTOSIKLET;
        } else {
            System.err.println("❌ Hatalı araç tipi seçimi!");
            return;
        }

        Arac arac = null;
        switch (secilenTip) {
            case OTOMOBIL:
                arac = new Otomobil(plaka);
                break;
            case MOTOSIKLET:
                arac = new Motosiklet(plaka);
                break;
        }
        // Bunu görürsek işlemi burada iptal edip ana menüye dönüyoruz.
        if (arac.getPlaka().equals("HATALI-PLAKA")) {
            System.out.println(">>> ❗Giriş işlemi iptal edildi. Lütfen geçerli bir plaka giriniz. <<<");
            return; // Metottan çıkar, ana menüye döner.
        }

        while (true) {

            System.out.println("Abone ID (Yoksa Enter'a basiniz):");
            String aboneId = tarayici.nextLine().trim();

            // Enter'a basıldıysa → abonesiz devam
            if (aboneId.isEmpty()) {
                break;
            }

            // FORMAT KONTROLÜ (Axxx)
            if (!aboneId.matches("A\\d{3}")) {
                System.err.println("❗Hata:Abone ID formata uygun değil.");
                continue;
            }

            // KAYITLI MI?
            if (!service.getAboneler().containsKey(aboneId)) {
                System.out.println("❗Hata:Bu Abone ID sistemde kayıtlı değil.");
                continue;
            }

            // HER ŞEY DOĞRU → ABONEYİ ATA
            arac.setAbone(service.getAboneler().get(aboneId));
            System.out.println("Abone girişi algılandı.");
            break;
        }


        boolean parkIslemiBasarili = false;

        while (!parkIslemiBasarili) {

            int sonKatIndex = service.getParkMatrisi().length - 1;
            int sonSiraIndex = service.getParkMatrisi()[0].length - 1;
            // Kullanıcıya tekrar tekrar başlık basmak yerine direkt soruyoruz
            System.out.println("\n--- Park Yeri Seçiniz (Çıkış için Kat: -1) ---");

            System.out.print("Hangi Kat (0-" + sonKatIndex + "): ");
            int kat = -1;
            int sira = -1;

            try {
                kat = tarayici.nextInt();
                if (kat == -1) {
                    System.out.println("❗İşlem iptal edildi.❗");
                    return;
                }

                System.out.print("Hangi Sira (0-" + sonSiraIndex + "): ");
                sira = tarayici.nextInt();
                tarayici.nextLine(); // Enter tuşunu temizle

                // Park etmeyi dene
                service.aracGiris(arac, kat, sira);

                // Hata vermediyse buraya gelir ve döngü biter
                parkIslemiBasarili = true;

            } catch (Exception e) {
                // Hata mesajını bas ama BEKLEME YAPMA
                System.err.println(">> UYARI: " + e.getMessage());
                System.err.println(">> Lütfen boş bir yer seçiniz:");

                // BURADAKİ nextLine()'ı SİLDİK. ARTIK TAKILMADAN BAŞA DÖNECEK.
            }
        }
    }

    private static void aracCikisEkrani(OtoparkService service) {
        System.out.println("\n--- ARAÇ ÇIKIŞ ---");

        System.out.println("Çıkış yapacak aracın plakası: ");

        // DÜZELTME 1: Plakadaki tüm boşlukları siliyoruz (örn: "06 DDL 107" -> "06DDL107")
        String plaka = tarayici.nextLine().toUpperCase().replaceAll("\\s+", "");
        try {
            // Service sınıfı zaten detaylı fişi ekrana basıyor.
            // Biz sadece dönen rakamı alıp aşağıda göstereceğiz.
            double ucret = service.aracCikis(plaka);

            // DÜZELTME 2: Buradaki "Standart ücret uygulandı" vs. kodlarını SİLDİK.
            // Çünkü Service sınıfı zaten fişin üzerine "ABONE TARİFESİ" yazıyor.

            // DÜZELTME 3: Parayı virgülden sonra 2 basamak olacak şekilde düzeltiyoruz.
            String formatliUcret = String.format("%.2f", ucret);

            System.out.println("------------------------------------");
            System.out.println(">>>> ÖDENECEK TUTAR: " + formatliUcret + " TL <<<<");
            System.out.println("------------------------------------");

        } catch (Exception e) {
            System.err.println("❌HATA: Çıkış yapılamadı! (" + e.getMessage() + ")");
        } finally {
            System.out.println("Çıkış işlemi tamamlandı.");
        }
    }

    private static void aboneEkleEkrani(OtoparkService service) {
        System.out.println("\n--- YENİ ABONE KAYDI ---");

        System.out.print("Abone ID (örn: A001): ");
        String id = tarayici.nextLine().trim();

        if (id.isEmpty()) {
            System.err.println("❌Hata: ID boş olamaz!");
            return; // 🔁 ANA MENÜ
        }
        // FORMAT + BOŞ KONTROL (EN BAŞTA)
        if (!id.matches("A\\d{3}")) {
            System.err.println("❌Hata:Abone ID formata uygun değil!");
            return; // 🔁 ANA MENÜYE DÖNER
        }

        // AYNI ID VAR MI?
        if (service.getAboneler().containsKey(id)) {
            System.out.println("Bu Abone ID zaten kayıtlı.");
            return; // 🔁 ANA MENÜYE DÖNER
        }

        // SADECE BURAYA GELİRSE DEVAM EDER
        System.out.print("Ad Soyad: ");
        String adSoyad = tarayici.nextLine();

        String tip;
        while (true) {
            System.out.print("Abone Tipi (Aylık / Saatlik): ");
            String girdi = tarayici.nextLine().trim();

            if (girdi.equalsIgnoreCase("Aylık") || girdi.equalsIgnoreCase("Aylik")) {
                tip = "Aylık";
                break;
            } else if (girdi.equalsIgnoreCase("Saatlik")) {
                tip = "Saatlik";
                break;
            } else {
                System.err.println("❌ Hatalı giriş! Lütfen sadece 'Aylık' veya 'Saatlik' yazınız.");
            }
        }

        service.yeniAboneEkle(id, adSoyad, tip);
        System.out.println("✔ Başarılı! " + adSoyad + " sisteme eklendi.");
    }
}


