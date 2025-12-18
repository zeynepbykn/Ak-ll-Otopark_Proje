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
        String[] gunler = {"Pzt", "Sal", "Car", "Per", "Cum", "Cmt", "Paz"};

        int gunIndex = java.time.LocalDate.now().getDayOfWeek().getValue() - 1;
        String bugun = gunler[gunIndex];

        String bugunBuyuk = bugun.toUpperCase();
        boolean haftaSonuMu =
                bugun.equalsIgnoreCase("Cmt") ||
                        bugun.equalsIgnoreCase("Paz");

        System.out.println("Bugun gunlerden: " + bugunBuyuk);


        OtoparkService service = new OtoparkService(3, 5);

        boolean devamMi = true; //Dongu anahtari
        int secim = -1;
        //2. Oyun dongusu basliyor.
        while (devamMi) {
            System.out.println("\n---------AKILLI OTOPARK MENUSU---------");
            System.out.println("1- Arac Girisi");
            System.out.println("2- Arac Cikisi");
            System.out.println("3- Durum Goster (map)");
            System.out.println("4- Yeni Abone Ekle");
            System.out.println("5- Aboneleri Listele");
            System.out.println("6- Parktaki Arac Sayisi");
            System.out.println("0- Cikis");
            System.out.println("Seciminiz: ");

            //Kullanicidan sayi aliniyor.

            try {
                secim = tarayici.nextInt();
                tarayici.nextLine();//Enter tusunu temizler!!
            } catch (Exception e) {
                System.out.println("Hata: Lutfen sadece sayi giriniz!");
                tarayici.nextLine();//hatali girdiyi temizler.
                continue; //Dongumuzun basina doner.
            } //3. Secime Gore Yonlendirme
            switch (secim) {
                case 1:
                    aracGirisEkrani(service);
                    break;
                case 2:
                    aracCikisEkrani(service);
                    break;
                case 3:
                    Raporlayici.matrisiKonsolaYazdir(service.getParkMatrisi());
                    break;
                case 4:
                    aboneEkleEkrani(service);
                    break;
                case 5:
                    service.getAboneler().forEach((k, v) -> System.out.println(k + " -> " + v));
                    break;

                case 6:
                    System.out.println("Parktaki arac sayisi: " + service.getParkMatrisi().length);
                    break;


                case 0:
                    System.out.println("Sistem kapatiliyor.Iyi gunler!");
                    devamMi = false;
                    break;
                default:
                    System.out.println("Hatali secim! Tekrar deneyiniz.");

            }
        }

    }

    //Kullanicidan gerekli tum bilgileri(Plaka,tip ,yer..) burada toplar,ve srvice e gonderir.
//bu metot topladıgı bilgileri service'e isler.
    private static void aracGirisEkrani(OtoparkService service) {
        System.out.println("\n---Arac girisi: ");
        System.out.print("Plaka: ");
        String plaka = tarayici.nextLine();

        System.out.println("Tip (1-Otomobil ,2-Motosiklet): ");
        int tip = tarayici.nextInt();
        tarayici.nextLine();

        // int sayi okunurken sondaki \n okunmaz ve bufferda kalir sonraki okumada sıkıntı cıkmasın diye onu temizliyoruz.
        //nextLine() -->  \n dahil olmak uzere herseyi tuketir.

        Arac arac = null;
        if (tip == 1) {  //Polimorfizm
            arac = new Otomobil(plaka);
        } else if (tip == 2) {
            arac = new Motosiklet(plaka);
        } else {
            System.out.println("Hatali arac tipi!");
            return;
        }

        System.out.println("Abone ID: (Yoksa enter'a basiniz)");
        String aboneId = tarayici.nextLine();
        //Eger kullanıcı bos birsey girmediyse ve yazdıgı aboneId defterimize kayitli ise
        if (!aboneId.isEmpty() && service.getAboneler().containsKey(aboneId)) {
            // ADIM A (Sağ Taraf): service.getAboneler().get(aboneId) -> Dosyadan o aboneyi bul ve getir.
            // ADIM B (Sol Taraf): arac.setAbone(...) -> Bulunan aboneyi otoparka giren araca etiketle (bağla).
            arac.setAbone(service.getAboneler().get(aboneId));
            System.out.println("Abone girisi algilandi.");
        }

        //Konum secimi
        System.out.println("Hangi Kat (0-2): ");
        int kat = tarayici.nextInt();
        System.out.println("Hangi Sira (0-4): ");
        int sira = tarayici.nextInt();
        tarayici.nextLine();// temizlik
        try {//bu aracı o kata ve sıraya park eder.
            service.aracGiris(arac, kat, sira);
        }
        //Park yeri dolu ise yada girilen kat veya sira sunmarasi yanlissa hata fırlatıp yakalıyoruz.
        catch (Exception e) {
            System.out.println("HATA: " + e.getMessage());
        }
    }

    private static void aracCikisEkrani(OtoparkService service) {
        System.out.println("\n--- ARAC CIKIS ---");

        //hangi arac cikiyor
        System.out.println("Cıkıs yapacak aracı plakasi: ");
        String plaka = tarayici.nextLine();

        try {
            //Service'e bu plakayı cıkart diyoruz
            //Service hesaplamayi yapip bize bir ucret donduruyor.
            double ucret = service.aracCikis(plaka);

            String aciklama = (ucret == 0)
                    ? "Aboneden ucret alinmadi."
                    : "Standart ucret uygulandi.";

            System.out.println(aciklama);


            System.out.println("------------------------------------");
            System.out.println(">>>> ODENECEK TUTAR: " + ucret + " TL <<<<");
            System.out.println("------------------------------------");

        } catch (Exception e) {
            //Eger plaka icerde yoksa veya baska hata olusursa burasi calisir.
            System.out.println("HATA: Cıkıs yapilamadi!(" + e.getMessage() + ")");
        }finally {
            System.out.println("Cikis islemi tamamlandi.");
        }

    }

    private static void aboneEkleEkrani(OtoparkService service) {
        System.out.println("\n--- YENI ABONE KAYDI ---");

        System.out.print("Abone ID (orn: A001): ");
        String id = tarayici.nextLine();

        System.out.print("Ad Soyad: ");
        String adSoyad = tarayici.nextLine();

        System.out.print("Abone Tipi (Aylik / Saatlik): ");
        String tip = tarayici.nextLine();

        // Servise gonderiyoruz, o hem dosyaya hem listeye kaydediyor
        service.yeniAboneEkle(id, adSoyad, tip);

        System.out.println("Basarili! " + adSoyad + " sisteme eklendi.");
    }


}

