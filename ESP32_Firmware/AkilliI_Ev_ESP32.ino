#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <WiFiClientSecure.h>

WiFiMulti wifiMulti;

// --- FİREBASE BAĞLANTISI ---
String firebaseURL = "https://akillievamu-default-rtdb.europe-west1.firebasedatabase.app/isik.json";

// Röleyi taktığımız pin
#define RELAY_PIN 19

void setup() {
  Serial.begin(115200);

  // Başlangıçta röle kapalı dursun
  pinMode(RELAY_PIN, INPUT); 

  // --- İNTERNET BİLGİLERİN (Kendi Ağlarını Yaz) ---
  wifiMulti.addAP("Everest", "kumbetli.1938.1945.");
  wifiMulti.addAP("HONOR Magic7 Pro", "ahmet11111");
  wifiMulti.addAP("TURKNET_8416B", "UXPdx9CQ");

  Serial.println("\nEn guclu internete baglaniyor...");
  
  while (wifiMulti.run() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi Baglandi! Sinyal gucu harika.");
}

void loop() {
  if (wifiMulti.run() == WL_CONNECTED) {
    WiFiClientSecure client;
    client.setInsecure(); 
    
    HTTPClient http;
    http.begin(client, firebaseURL);
    
    int httpCode = http.GET();
    
    if (httpCode > 0) {
      String payload = http.getString();
      
      // KRİTİK ÇÖZÜM: Gelen JSON'daki tırnak işaretlerini temizliyoruz!
      payload.replace("\"", ""); 
      payload.trim(); // Varsa görünmez boşlukları da siler
      
      Serial.println("Temizlenmis emir: " + payload);
      
      if (payload == "1") {
        // Işığı AÇ
        pinMode(RELAY_PIN, OUTPUT);
        digitalWrite(RELAY_PIN, LOW); 
      } 
      else if (payload == "0" || payload == "null") {
        // Işığı KAPAT
        pinMode(RELAY_PIN, INPUT); 
      }
    } else {
      Serial.println("Firebase'e baglanilamadi. Hata Kodu: " + String(httpCode));
    }
    
    http.end(); 
  } 
  delay(1000); 
}
