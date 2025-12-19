import model.*;
import service.OtoparkService;
import exception.OtoparkDoluException;
import util.Raporlayici;

import java.util.Scanner;

public class OtoparkUygulama {
    // Tüm static metotların (main ve yardımcılar) ortak kullanabilmesi için sınıf seviyesinde tanımlanan giriş aracı.
    //her metodda new scanner dememize gerek kalmaz(new..- bellek yönetimi)
    private static Scanner tarayici = new Scanner(System.in);

    public static void main(String[] args) {

        //1. Otoparki hazirliyoruz.(3 kat,5 sıra)
        System.out.println("Sistem baslatiliyor...");

        // === GUN BILGISI (DIZI + DATE + STRING) ===
        String[] gunler = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};

        int gunIndex = java.time.LocalDate.now().getDayOfWeek().getValue() - 1;
        String bugun = gunler[gunIndex];

        String bugunBuyuk = bugun.toUpperCase();
        System.out.println("Bugun gunlerden: " + bugunBuyuk);


        OtoparkService service = new OtoparkService(3, 5);

        boolean devamMi = true; //Dongu anahtari
        char secim = ' ';

        //2. Oyun dongusu basliyor.
        while (devamMi) {
            System.out.println("\n---------AKILLI OTOPARK MENUSU---------");
            System.out.println("a- Arac Girisi");
            System.out.println("b- Arac Cikisi");
            System.out.println("c- Durum Goster (map)");
            System.out.println("d- Yeni Abone Ekle");
            System.out.println("e- Aboneleri Listele");
            System.out.println("f- Parktaki Arac Sayisi");
            System.out.println("q- Cikis");
            System.out.println("Seciminiz: ");

            //Kullanicidan sayi aliniyor.

            try {
                String girdi = tarayici.next().toLowerCase();
                secim = girdi.charAt(0);
                tarayici.nextLine();
            } catch (Exception e) {
                System.out.println("Hata: Lutfen sadece sayi giriniz!");
                tarayici.nextLine();//hatali girdiyi temizler.
                continue; //Dongumuzun basina doner.
            } //3. Secime Gore Yonlendirme
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
                case 'e':
                    service.getAboneler().forEach((k, v) -> System.out.println(k + " -> " + v));
                    break;

                case 'f':
                    // 1. Service'ten matrisi (binanın planını) istiyoruz
                    ParkYeri[][] matris = service.getParkMatrisi();
                    int sayac = 0;

                    // 2. Tüm katları ve sıraları tek tek geziyoruz
                    for (int i = 0; i < matris.length; i++) {
                        for (int j = 0; j < matris[i].length; j++) {
                            // 3. Eğer o kutu (park yeri) doluysa sayacı artır
                            if (matris[i][j].isDoluMu()) {
                                sayac++;
                            }
                        }
                    }

                    System.out.println("Parktaki anlik arac sayisi: " + sayac);
                    break;

                case 'q':
                    System.out.println("Sisctem kapatiliyor.Iyi gunler!");
                    devamMi = false;
                    break;
                default:
                    System.out.println("Hatali secim! Tekrar deneyiniz.");

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
            System.out.println("\n🛑 HATA: OTOPARK TAMAMEN DOLU! 🛑");
            System.out.println(">>> Kapasite dolu olduğu için yeni araç girişi yapılamaz.");
            return; // Metodu burada kesip ana menüye fırlatır.
        }
        System.out.println("\n---Arac girisi: ");
        System.out.print("Plaka: ");

        String plaka = tarayici.nextLine().toUpperCase().replaceAll("\\s+", "");

        if (plaka.trim().isEmpty()) {
            System.out.println("Hata: Plaka boş olamaz!");
            return;
        }

        System.out.println("Tip (1-Otomobil ,2-Motosiklet): ");
        int tip = -1;
        try {
            tip = tarayici.nextInt();
            tarayici.nextLine(); // Buffer temizliği
        } catch (Exception e) {
            System.out.println("Hata: Sayı girmelisiniz!");
            tarayici.nextLine();
            return;
        }

        Arac arac = null;
        if (tip == 1) {
            arac = new Otomobil(plaka);
        } else if (tip == 2) {
            arac = new Motosiklet(plaka);
        } else {
            System.out.println("Hatali arac tipi!");
            return;
        }
        // Bunu görürsek işlemi burada iptal edip ana menüye dönüyoruz.
        if (arac.getPlaka().equals("HATALI-PLAKA")) {
            System.out.println(">>> Giriş işlemi iptal edildi. Lütfen geçerli bir plaka giriniz. <<<");
            return; // Metottan çıkar, ana menüye döner.
        }

        System.out.println("Abone ID: (Yoksa enter'a basiniz)");
        String aboneId = tarayici.nextLine();
        if (!aboneId.isEmpty() && service.getAboneler().containsKey(aboneId)) {
            arac.setAbone(service.getAboneler().get(aboneId));
            System.out.println("Abone girisi algilandi.");
        }

        // --- DÖNGÜ BAŞLIYOR ---
        boolean parkIslemiBasarili = false;

        while (!parkIslemiBasarili) {

int sonKatIndex=service.getParkMatrisi().length -1;
int sonSiraIndex=service.getParkMatrisi()[0].length -1;
            // Kullanıcıya tekrar tekrar başlık basmak yerine direkt soruyoruz
            System.out.println("\n--- Park Yeri Seçiniz (Çıkış için Kat: -1) ---");

            System.out.print("Hangi Kat (0-" +sonKatIndex+ "): ");
            int kat = -1;
            int sira = -1;

            try {
                kat = tarayici.nextInt();
                if (kat == -1) {
                    System.out.println("İşlem iptal edildi.");
                    return;
                }

                System.out.print("Hangi Sira (0-" +sonSiraIndex+ "): ");
                sira = tarayici.nextInt();
                tarayici.nextLine(); // Enter tuşunu temizle

                // Park etmeyi dene
                service.aracGiris(arac, kat, sira);

                // Hata vermediyse buraya gelir ve döngü biter
                parkIslemiBasarili = true;

            } catch (Exception e) {
                // Hata mesajını bas ama BEKLEME YAPMA
                System.out.println(">> UYARI: " + e.getMessage());
                System.out.println(">> Lütfen boş bir yer seçiniz:");

                // BURADAKİ nextLine()'ı SİLDİK. ARTIK TAKILMADAN BAŞA DÖNECEK.
            }
        }
    }

    private static void aracCikisEkrani(OtoparkService service) {
        System.out.println("\n--- ARAC CIKIS ---");

        System.out.println("Cıkıs yapacak aracı plakasi: ");

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
            System.out.println(">>>> ODENECEK TUTAR: " + formatliUcret + " TL <<<<");
            System.out.println("------------------------------------");

        } catch (Exception e) {
            System.out.println("HATA: Cıkıs yapilamadi! (" + e.getMessage() + ")");
        } finally {
            System.out.println("Cikis islemi tamamlandi.");
        }
    }

    private static void aboneEkleEkrani(OtoparkService service) {
        System.out.println("\n--- YENI ABONE KAYDI ---");

        System.out.print("Abone ID (orn: A001): ");
        String id = tarayici.nextLine();
        if (id.isEmpty()) {
            System.out.println("Hata: ID boş olamaz!");
            return;
        }

        System.out.print("Ad Soyad: ");
        String adSoyad = tarayici.nextLine();

        // --- TİP KONTROLÜ (SONSUZ DÖNGÜ) ---
        // Kullanıcı doğru yazana kadar buradan çıkamaz
        String tip = "";
        while (true) {
            System.out.print("Abone Tipi (Aylik / Saatlik): ");
            String girdi = tarayici.nextLine().trim();

            if (girdi.equalsIgnoreCase("Aylik")) {
                tip = "Aylik";
                break; // Doğru girdi, döngüden çık
            } else if (girdi.equalsIgnoreCase("Saatlik")) {
                tip = "Saatlik";
                break; // Doğru girdi, döngüden çık
            } else {
                System.out.println("❌ Hatalı giriş! Lütfen sadece 'Aylik' veya 'Saatlik' yazınız.");
            }
        }

        // Servise gonderiyoruz, o hem dosyaya hem listeye kaydediyor
        service.yeniAboneEkle(id, adSoyad, tip);

        System.out.println("Basarili! " + adSoyad + " sisteme eklendi.");
    }


}

