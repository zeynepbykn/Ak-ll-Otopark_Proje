package exception;
//Bu hata kontrol edilmesi zorunlu(checked) bir hata oluyor.
public class OtoparkDoluException extends Exception {

  public OtoparkDoluException(String message){
    // 'super' anahtar kelimesi ile bu mesajı Baba Sınıfa (Java'nın Exception sistemine) teslim ediyoruz.
    // Çünkü mesajı saklama ve ekrana basma yeteneği Babadadır. Bizde o yetenek yok.
    super(message);
  }
}
