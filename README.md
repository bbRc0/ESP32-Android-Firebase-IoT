# 🏠 Native IoT Smart Home Automation ⚡

An end-to-end native IoT smart home system built from scratch without using any third-party middleware (like Sinric Pro or Blynk). The system features an ESP32 microcontroller fetching real-time JSON commands via REST API from Firebase, controlled by a custom Android Java application with built-in voice control.

## 🚀 Key Features
* **Native Android App:** Custom-built user interface using Java and XML in Android Studio.
* **Voice Assistant Integration:** Implemented Google Speech-to-Text API for natural language commands (e.g., "Turn on the light", "Turn off").
* **REST API Communication:** The ESP32 utilizes native HTTP GET requests to parse raw JSON data directly from Firebase, avoiding heavy libraries and optimizing hardware memory.
* **Real-Time State Management:** Instant synchronization between the mobile app and hardware via Google Firebase Realtime Database.
* **Smart Wi-Fi Manager:** Implemented `WiFiMulti` on ESP32 to automatically scan and connect to the strongest available network.

## 🛠️ Technologies & Tools
* **Hardware:** ESP32-WROOM-32, 5V Relay Module
* **Embedded Software:** C/C++ (Arduino IDE), HTTPClient, WiFiClientSecure
* **Mobile Development:** Java, Android SDK, Android Studio
* **Cloud Infrastructure:** Google Firebase Realtime Database

## 📸 Project Showcase
> **Note:** Here you can add a GIF or YouTube link showing the mobile app and the relay clicking in real-time!

## ⚙️ Architecture Brief
Unlike standard beginner projects that rely on heavy wrapper libraries, this project takes a Native approach. The Android application acts as the client, updating the state (1 or 0) on the Firebase Realtime Database. The ESP32 continuously polls the database URL via a secure HTTPS connection, parses the raw JSON string, and triggers the GPIO pins accordingly.
