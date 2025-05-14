#  Puzzle Rush Hour 

[ Puzzle Rush Hour ](doc/alfi.png)

## Deskripsi Program
Program ini adalah sebuah solver untuk permainan ** Puzzle Rush Hour ** yang menggunakan **algoritma Path Finding** dengan teknik . Program ini memiliki fitur utama sebagai berikut:

- Memuat konfigurasi papan dan blok dari file input ".txt".
- Menyelesaikan rush hour ....
- Menyimpan solusi ke dalam file teks atau gambar.
- Menampilkan solusi secara visual menggunakan **antarmuka grafis (GUI)** berbasis **JavaFX**.


## Requirements Opsional (Jika ingin pake extension vs code )
- Extension JavaFX 
- Extension Pack for Java 

## Requirements Wajib
- JavaFX  ........(nanti kasih link zip / link)
- Java 23 .........(nanti kasih zip / link)
- Maven (Tidak perlu, kalau sudah pake extension)  ............(nanti kasih zip / link)

## Cara Menjalankan Program 1 (Jika menggunakan extension vs code)
1. Pastikan suah mendownload requirement opsional
2. Buka src/src/main/java/com/App.java
3. Tekan Run

## Cara Menjalankan Program 2 (Manual Compile and Run Via Cli)
1. Pastikan sudah mendownload requirement wajib
2. Masuk ke src
```
cd src
```
3. Jalankan perintah ini
```
mvn clean javafx:run
```


## Cara Menjalankan Program 3 (Manual Menggunakan Executable JAR)
1. Pastikan sudah mendownload requirement wajib
2. Masuk ke src
```
cd src
```
3.  Buat jar 
```
mvn clean package
```

4. Jalankan jar
```
java -jar target/myjavafxapp-1.0-SNAPSHOT.jar

```
atau bisa juga 
```
java --module-path "C:\Program Files\Java\javafx-sdk-21.0.3\lib" \
     --add-modules javafx.controls,javafx.fxml \
     --add-opens javafx.base/com.sun.javafx=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
     -jar bin/App-1.0-SNAPSHOT-shade-plugin.jar
```

Note :  Kalimat "C:\Program Files\Java\javafx-sdk-21.0.3\lib" bisa diganti dengan path ke javafx sdk yang sesuai yang sudah di download sebelumnya Kalimat "bin/App-1.0-SNAPSHOT-shade-plugin.jar bisa diganti dan disesuaikan tergantung dari lokasi jar file App-1.0-SNAPSHOT-shade-plugin.jar.


## Cara Menggunakan Aplikasi 
1. (Jelasin alur user make app nanti)
2. 


## Struktur File
```
Tuci3_13523065_13523073
├── bin/
├── doc/
├── src/
│     ├── src/main/
│     │     ├── java/com/jawa
│     │     │     ├── 
│     │     │     ├── 
│     │     │     ├── App.java
|     |     |     ├── PrimaryController.java
│     │     ├── resources/com/jawa  #Resource gui (css,fxml)
├── test/
│     ├── input/        # Input percobaan puzzle dalam txt
│     ├── output/       # Output Hasil percobaan
```

## Contributors

| **NIM**  | **Nama Anggota**               | **Github** |
| -------- | ------------------------------ | ---------- |
| 13523065 | Dzaky Aurelia Fawwaz           | [WwzFwz](https://github.com/WwzFwz) |
| 13523073 | Alfian Hanif                   | [AlfianHanifFY](https://github.com/AlfianHanifFY) | 


## References
- [Spesifikasi Tugas Kecil 3 Stima 2024/2025](https://docs.google.com/document/d/1NXyjtIHs2_tWDD37MYtc0VhWtoU2wIH8A95ImttmMXk/edit?usp=sharing)
- [Slide Kuliah IF2211 2024/2025](https://informatika.stei.itb.ac.id/~rinaldi.munir/Stmik/2024-2025/17-Algoritma-Branch-and-Bound-(2025)-Bagian1.pdf)
