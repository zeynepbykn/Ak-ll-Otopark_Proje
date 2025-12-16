import model.*;
import service.OtoparkService;
import exception.OtoparkDoluException;

import java.util.Scanner;

public class OtoparkUygulama {
    // Tüm static metotların (main ve yardımcılar) ortak kullanabilmesi için sınıf seviyesinde tanımlanan giriş aracı.
    //her metodda new scanner dememize gerek kalmaz(new..- bellek yönetimi)
    private static Scanner tarayici=new Scanner(System.in);

    public static void main(String[] args) {

   //1. Otoparki hazirliyoruz.(3 kat,5 sıra)
        System.out.println("Sistem baslatiliyor...");
        OtoparkService service= new OtoparkService(3,5);

        boolean devamMi=true; //Dongu anahtari
        int secim=-1;
        //2. Oyun dongusu basliyor.
        while(devamMi){
            System.out.println("\n---------AKILLI OTOPARK MENUSU---------");
            System.out.println("1- Arac Girisi");
            System.out.println("2- Arac Cikisi");
            System.out.println("3- Durum Goster (map)");
            System.out.println("4- Yeni Abone Ekle");
            System.out.println("0- Cikis");
            System.out.println("Seciminiz: ");

            //Kullanicidan sayi aliniyor.

            try{
              secim=tarayici.nextInt();
              tarayici.nextLine();//Enter tusunu temizler!!
            }
            catch (Exception e){
                System.out.println("Hata: Lutfen sadece sayi giriniz!");
                tarayici.nextLine();//hatali girdiyi temizler.
                continue; //Dongumuzun basina doner.
            }
        }

        //3. Secime Gore Yonlendirme
        switch (secim){
            case 1:

            case 2:

                case 3:

            case 4:

            case 0:

            default:
                System.out.println("Hatali secim! Tekrar deneyiniz.");

        }
    }
}
