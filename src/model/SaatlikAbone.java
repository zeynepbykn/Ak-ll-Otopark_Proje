package model;

public class SaatlikAbone extends Abone {

    /*private olan eriim belirleyicisini kullandik cunku
    disardan degistirilebilsin istemiyorum baska siniflar tarafindan
    indirim oranini duruma gore degıstırılebilir yapcaz ama main buraya karisabilsin istemiyoruz.
     */

    private double anlikIndirimOrani = 0.20;

    public SaatlikAbone(String aboneId, String adSoyad) {
        /*Abone sinifinda degiskenleri private yapmistik
        sımdı burada bilgileri super yaptik ki burda gelen bilgilere super
        basamak atlatip aboneye gecmesini saglasin
         */
        super(aboneId, adSoyad);
    }

    // Polimorfizm(belirli metot icinden duruma gore farklı cevaplar alabilme hali)
    /*Indirimi degisken yapmak istedim yani ozel gunlerde farkli
    indirimler uygulanabilsin. Bu yuzden belirlenen orani donduruyo.
     */
    @Override
    public double indirimOraniBelirle() {
        return anlikIndirimOrani;
    }

    /*Program kullanicisi istedigi indirimi istedigi oranda tanimlayabilsin diye
    gereken esnekligi burda verdik.
     */
    public void setAnlikIndirimOrani(double yeniOran) {
        // Validation(kontrol):oran olarak verecegi icin 0 ile 1 arasi olmasi kontrolu yoksa "-" ye duser.
        if (yeniOran < 0 || yeniOran > 1.0) {
            System.err.println("HATA: Geçersiz indirim oranı!");
            return;
        }
        this.anlikIndirimOrani = yeniOran;
        System.out.println(">>> Standart Müşteri İndirimi Güncellendi: %" + (yeniOran * 100));
    }

    /*Bu metotta da indirim varsa onu uygulayip yoksa da olan fiyati
    verecegi metotu ezdik ana metot olan ucretHesapladan.
     */


    @Override
    public String toString() {

        return super.toString() + " (Tip: Saatlik - %" + (anlikIndirimOrani * 100) + " İndirimli)";
    }
}