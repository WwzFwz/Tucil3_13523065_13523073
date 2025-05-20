#  Puzzle Rush Hour 

[ Puzzle Rush Hour ](doc/awok.png)

## Deskripsi Program
Program ini adalah sebuah solver untuk permainan ** Puzzle Rush Hour ** yang menggunakan **algoritma Path Finding** dengan teknik . Program ini memiliki fitur utama sebagai berikut:

- Memuat konfigurasi papan dan blok dari file input ".txt".
- Menyelesaikan rush hour dengan berbagai macam algoritma dan heuristic
- Menyimpan solusi ke dalam file teks 
- Menampilkan solusi secara visual menggunakan **antarmuka grafis (GUI)** berbasis **JavaFX**.


## Requirements Opsional (Jika ingin pake extension vs code )
- Extension JavaFX 
- Extension Pack for Java 

## Requirements Wajib
-  [JavaFx 17](https://maven.apache.org/download.cgi)
-  [Java 23](https://maven.apache.org/download.cgi)  (Note : Salinlah path tempat anda manruhnya karena dibutuhkan untuk cara menjalankan program selanjutnya)
-  [Maven](https://maven.apache.org/download.cgi) (Note :Tidak perlu, kalau sudah pake extension) 

## Cara Menjalankan Program 1 (Jika menggunakan extension vs code)
1. Pastikan sucah mendownload requirement opsional
2. Buka src/src/main/java/com/App.java
3. Tekan Run pada method main



## Cara Menjalankan Program 2 (Jika ingin compile ulang)
1. Pastikan sudah mendownload requirement wajib
2. Masuk ke src
```
cd src
```
3. Jalankan perintah ini jika ingin membuat ulang executable file
```
mvn clean javafx:run
```

## Cara Menjalankan Program 3 (Menggunakan executable file yang telah ada )
1. Pastikan sudah mendownload requirement wajib
2. Masuk ke src 
```
cd src
```
3. Jalankan executable file
```
java --module-path "C:/Program Files/Java/javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml -cp target/classes com.jawa.App
```

Note :   
-"C:\Program Files\Java\javafx-sdk-21.0.3\lib" merupakan folder dimana javafx kalian diinstal (path yg saya mont auntuk dicopy sblmnya)
- "target/classes" dapat diubah sesuai dengan path relative terminal anda berada terhadap folder target/classes, semisal jika anda berada di root project maka bisa diubah menjadi "src/target/classes"


## Struktur File
```
Tuci3_13523065_13523073
├── bin/
├── doc/
├── src/
│     ├── src/main/
│     │     ├── java/com/jawa
│     │     │     ├── models // model algoritma yang telah dibuat
│     │     │     ├── 
│     │     │     ├── App.java
|     |     |     ├── PrimaryController.java
│     │     ├── resources/com/jawa  #Resource gui (css,fxml)
├── test/
│     ├── input/        # Input percobaan puzzle dalam txt
│     ├── output/       # Output Hasil percobaan

```
## Penjelasan GUI
Pada Graphical User Interface kami, user akan diminta memberi masukan sesuai alur input yang tertera pada spek tugas kecil. Suatu button atau combobox akan di disable(lock) jika user belum memasukkan input yang diminta sebelumnya.
Adapun berikut keterangan button, combobox, serta component pada aplikasi kami : 
1. Select File Button :  Button yang bila ditekan akan menampilkan folder sehingga user dapat memasukkan masukan file
2. Algorithm Combobox : Combobox yang bila ditekan akan menampilkan list algoritma yang tersedia
3. Heuristic Combobox : Combobox yang bila ditekan akan menampilkan list heuristic yang tersedia untuk suatu algoritma yang telah dipilih sebelumnya
4. Solve Button : Button yang bila ditekan akan menjalankan program solver sesuai dengan algoritma dan atau heuristic yang telah dipilih sebelumnya
5. Play Button : Button yang bila ditekan akan menjalankan animasi dari suatu state ke solusi
6. Pause Button : Button yang bila ditekan akan menge-pause 
7. Next button : Button yang bila ditekan akan menampilkan step selanjutnya relative dari step sekarang
8. Back Button : Button yang bila ditekan akan menampilka step sebelumnya relative dari step sekarang
9. Solution Steps : Keterangan setiap langkah yang terjadi hingga mencapai solution
10. Pagination :  Hasil dari solusion steps dipaginasi, maksimum step list yang ditampilkan berjumlah x dan maksimum keterangan halaman yang ditampilkan berjumlah y , 	
11. Next Pagination Button : Lanjut ke halaman selanjutnya
12. Back Pagination Button : Kembali ke halaman sebelumnya
13. Board : visualisasi board, dapat menvisualisasikan board dalam tiap langkah


## Contributors

| **NIM**  | **Nama Anggota**               | **Github** |
| -------- | ------------------------------ | ---------- |
| 13523065 | Dzaky Aurelia Fawwaz           | [WwzFwz](https://github.com/WwzFwz) |
| 13523073 | Alfian Hanif                   | [AlfianHanifFY](https://github.com/AlfianHanifFY) | 


## References
- [Spesifikasi Tugas Kecil 3 Stima 2024/2025](https://docs.google.com/document/d/1NXyjtIHs2_tWDD37MYtc0VhWtoU2wIH8A95ImttmMXk/edit?usp=sharing)
- [Slide Kuliah IF2211 2024/2025](https://informatika.stei.itb.ac.id/~rinaldi.munir/Stmik/2024-2025/17-Algoritma-Branch-and-Bound-(2025)-Bagian1.pdf)
