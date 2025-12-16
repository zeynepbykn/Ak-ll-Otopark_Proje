package interfaces;
/*
 * Bu interface, araçların giriş ve çıkış işlemlerini takip etmek için kullanılacaktır.
 */
public interface GirisCikisTakip {
    /*
     * Aracın otoparka giriş işlemini gerçekleştirir.
     */
    void girisYap();
    /*
     * Aracın otoparktan çıkış işlemini gerçekleştirir.
     */
    void cikisYap();
}