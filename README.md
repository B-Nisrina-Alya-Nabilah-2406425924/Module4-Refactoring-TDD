# TUTORIAL MODUL 1 - CODING STANDARDS

```
Nama    : Nisrina Alya Nabilah
NPM     : 2406425924
Kelas   : Pemrograman Lanjut B
```

---

## Reflection 1
### Clean Code Principles & Secure Coding Practices
Dalam pengerjaan fitur *Create, Find, Edit,* dan *Delete* product menggunakan Spring Boot, saya telah menerapkan beberapa prinsip **Clean Code** dan **Secure Coding** sebagai berikut:

1.  **Meaningful Names**: Saya menggunakan nama variabel dan metode yang deskriptif. Contohnya, menggunakan `productId` dan `productName` daripada hanya `id` atau `n`, sehingga code mudah dipahami tanpa perlu komentar tambahan untuk menjelaskan variabel tersebut
2.  **Small Functions**: Setiap method di dalam `ProductController` dan `ProductServiceImpl` difokuskan untuk melakukan satu hal saja (*Single Responsibility Principle*), seperti hanya menangani logika penghapusan atau pembaruan data
3.  **Command Query Separation**: Metode seperti `create()` pada service melakukan perubahan state (Command), sedangkan `findAll()` hanya mengembalikan data (Query), tidak melakukan keduanya sekaligus
4.  **Secure Coding - Input Validation**: Meskipun masih sederhana, penggunaan model objek `Product` membantu dalam memetakan data input secara eksplisit, mengurangi risiko manipulasi data mentah yang tidak terstruktur
5.  **Boy Scout Rule**: Saya telah memastikan tidak ada dead code atau komentar yang tidak perlu (seperti potongan code yang di-comment out) tetap tinggal di dalam repositori.

**Improvement Plan:**
Kedepannya, saya perlu meningkatkan aspek **Error Handling** dengan menggunakan blok `try-catch` yang lebih spesifik atau menggunakan `@ControllerAdvice` untuk menangani kasus ketika produk yang ingin diedit atau dihapus tidak ditemukan (ID tidak valid), agar aplikasi lebih robust dan aman untuk project ini

---

## Reflection 2

### 1. Unit Testing & Code Coverage
Setelah menulis Unit Test, saya merasa lebih percaya diri saat melakukan perubahan code (refactoring) karena adanya secure net dari unit test ini
    - Berapa banyak Unit Test dalam satu kelas?
        - Tidak ada jumlah pasti, namun setiap alur logika (termasuk positive dan negative scenario) harus di-cover oleh unit-test
    - Apakah 100% Code Coverage menjamin bebas bug?
        - Tidak selalu. Code coverage hanya menunjukkan baris code mana yang dieksekusi saat test berjalan, namun tidak menjamin bahwa logika baik dari segi logika bisnis atua logika teknis nya sudah benar secara kontekstual atau mampu menangani semua kemungkinan corner cases, sehingga, bug logika masih sangat mungkin terjadi meskipun semua baris sudah 'passed' oleh test.

### 2. Evaluasi Kebersihan Code pada Functional Test Suite
Jika saya membuat kelas baru `CreateProductFunctionalTest.java` dengan prosedur setup yang identik dengan kelas sebelumnya (misal: `HomePageFunctionalTest`), muncul masalah kebersihan code yaitu Code Duplication
* Masalah yang diidentifikasi:
    * Redundansi Setup: Inisialisasi `WebDriver`, pengaturan port, dan penutupan driver yang berulang di setiap kelas test.
* Maintainability: 
    * Jika di masa depan ada perubahan (misalnya ganti dari `ChromeDriver` ke `FirefoxDriver`), saya harus mengubahnya di banyak tempat.

* Saran Perbaikan:
  * Base Class Implementation: Membuat sebuah base class (misalnya `BaseFunctionalTest`) yang berisi semua konfigurasi `@SpringBootTest`, setup driver, dan pembersihan driver. Kelas test fungsional lainnya cukup melakukan `extends` ke base class tersebut. 
  * Page for Object Model: Menggunakan pola page object model untuk memisahkan logika interaksi elemen UI dengan logika verifikasi test, sehingga code lebih mudah dibaca.

---

# TUTORIAL MODUL 2 - CI/CD & DevOps

---

## Refleksi Tutorial 2

1. Daftar masalah kualitas kode yang diperbaiki dan strategi memperbaikinya
    Selama pengerjaan tutorial dan latihan ini, beberapa masalah kualitas kode yang saya temukan dan perbaiki adalah:
    - Keamanan Izin Token (Token Permissions): Scorecard Bot mendeteksi bahwa izin ``GITHUB_TOKEN`` terlalu luas, yang ditunjukkan dengan skor 0, Strategi perbaikannya adalah dengan menambahkan blok permissions: `read-all` atau membatasi izin secara spesifik menjadi `contents: read` pada file workflow YAML agar sistem hanya memiliki hak akses minimum yang diperlukan.
    - Ketidakcocokan Versi Gradle (Gradle Compatibility): Terjadi error `getConvention()` karena penggunaan Gradle 9 yang tidak kompatibel dengan beberapa plugin lama, strategi perbaikannya adalah melakukan downgrade versi Gradle ke 8.10 menggunakan perintah ./gradlew wrapper agar sesuai dengan spesifikasi plugin SonarQube dan Spring Boot.
    - Security Hotspot: `SonarCloud` mendeteksi ketiadaan verifikasi integritas pada library yang diunduh, strategi perbaikannya adalah menjalankan perintah `--write-verification-metadata` untuk membuat file `verification-metadata.xml` guna memastikan setiap library yang digunakan telah melalui proses pengecekan `checksum`.
   

2. Apakah implementasi saat ini sudah memenuhi definisi Continuous Integration (CI) dan Continuous Deployment (CD)?
   - Menurut saya, implementasi saat ini sudah memenuhi kriteria CI/CD, hal ini dikarenakan setiap kali ada perubahan kode yang dikirim (push) ke repositori, GitHub Actions secara otomatis menjalankan rangkaian unit test dan analisis kualitas kode (CI) untuk memastikan tidak ada fitur yang rusak, selain itu, proses code delivery ke Platform as a Service (PaaS) seperti Koyeb juga sudah otomatis terpicu begitu kode digabungkan ke branch main, sehingga aplikasi versi terbaru dapat langsung diakses oleh pengguna tanpa intervensi manual (CD).

---

# TUTORIAL MODUL 3 - Maintainability & OO Principles

---

## Refleksi Tutorial 3

1. Prinsip SOLID yang Saya Terapkan
   - Single Responsibility Principle (SRP)
         - Saya memisahkan `CarController` dari file `ProductController.java` dan memindahkannya ke file baru. Sebelumnya, satu file menangani dua hal sekaligus: `Product` dan `Car`. Dengan dipisah, jika di kemudian ahri ada error di bagian `Car`, developer hanya perlu check satu file tersebut tanpa takut merusak fitur `Product`. Sehingga, dapat dipastikan satu kelas memiliki satu responsibility
   
    - Open-Closed Principle (OCP)
      - Saya memastikan `ProductController` dan `CarController` memanggil service lewat interface, bukan langsung ke kelas implementasinya, supaya code "closed" dari perubahan tapi "opened" untuk penambahan fitur baru. Contohnya, kalau di kemudian hari cara menyimpan datanya ganti dari pakai ArrayList ke database SQL, developer hanya perlu membuat class implementasi baru tanpa perlu mengubah code di Controller-nya sama sekali.

    - Liskov Substitution Principle (LSP)
      - Saya menghapus hubungan extends antara `CarController` dan `ProductController`. Karena, dulu `CarController` dipaksa jadi "anak" dari `ProductController`, padahal mereka punya urusan yang beda. Dampaknya, endpoint product jadi ikut terbawa ke car, padahal tidak relevan. Sekarang mereka independent supaya fungsionalitasnya tidak rusak atau janggal ketika dipanggil

    - Interface Segregation Principle (ISP)
      - Saya tetap memisahkan `CarService` dan `ProductService`, tapi kali ini saya standarisasi nama method-method-nya, supaya interface-nya tidak over dan tidak memaksa satu kelas buat pakai method yang tidak diperlukan. Dengan nama yang seragam (misal: findAll, update), pengerjaan jadi lebih rapi dan tidak perlu banyak mengingat nama berbeda ketika ingin diimplementasikan.

    - Dependency Inversion Principle (DIP)
      - Saya mengubah `@Autowired` di CarController supaya mengarah ke interface `CarService`, bukan ke class `CarServiceImpl`, supaya modul yang lebih tinggi tidak bergantung langsung dengan modul yang lebih rendah. Keduanya harus bergantung sama abstraksi (interface). Hasilnya, kode jadi lebih fleksibel dan tidak kaku ketika ada perubahan di level service.

2. Keuntungan Pakai SOLID
   - Maintainability: Karena kodenya rapi dan tugasnya dipisah-pisah (SRP), nyari bug bisa menjadi lebih mudah, karena kita tahu persis bagian mana yang harus diperbaiki.
    - Lebih Fleksibel (Scalability): Kalau ingin menambahkan fitur baru, tidak perlu merombak total kode yang sudah ada, bisa hanya dengan menambah kelas baru sesuai interface yang ada (OCP).
   - Testability: Karena kita pakai interface (DIP), kita bisa bikin Mock pas lagi testing. Jadi, ketika nge-test satu fungsi tidak harus menunggu database atau sistem lain siap dulu.
   - Readable: Penamaan yang konsisten membuat tim atau teman sekelompok project (atau kita sendiri ketika buka project ini lagi di masa depan) tidak pusing membaca alur kodenya.

3. Kerugian Kalau Nggak Pakai SOLID
   - Kalau semua fungsi ditumpuk jadi satu (melanggar SRP), fixing error di satu tempat bisa berdampak ke fitur lain yang sebenarnya tidak relevant.
   - Kalau kita bergantung langsung sama kelas concrete (melanggar DIP), sekali ada perubahan kecil di service, kita harus ganti kode di banyak tempat sekaligus, yang membuat developer harus mengeluarkan tenaga extra dan dengan resiko rawan salah
   - Bawa "Sampah" Kode: Kalau asal pakai extends (melanggar LSP), kelas kita bisa punya fitur atau endpoint yang sebenarnya tidak berguna. Ini yang membuat aplikasi jadi berat dan tidak aman.
   - Testing jadi susah: Tanpa SOLID, fungsi biasanya jadi terlalu besar dan saling berelasi satu sama lain, sehingga ketika mau ngetest satu fitur kecil, kita harus nyiapin banyak hal yang sebenernya nggak perlu, yang membuat proses testing memakan lebih banyak waktu.

modul 3 done :p

---

## Reflection 4

1. Is the TDD flow useful enough?
   - Ya, TDD membantu developer memecah masalah besar menjadi potongan kecil. Dengan menulis tes dulu, developer dipaksa memikirkan output yang diharapkan sebelum pusing dengan logika internalnya. Ini sangat membantu saat debugging algoritma yang kompleks.

2. Refleksi Prinsip F.I.R.S.T. pada Unit Test
   - Fast: Tes harus berjalan secepat mungkin tanpa mengganggu alur kerja. Penggunaan stubs pada tutorial bertujuan agar tes tidak bergantung pada database asli, sehingga tetap cepat
   - Isolated : Sebuah tes tidak boleh memengaruhi atau bergantung pada hasil tes lain. Tutorial mengajarkan penggunaan metode setUp (dengan @BeforeEach) untuk mereset objek dummy atau mock sebelum setiap kasus tes dijalankan.
   - Repeatable : Tes harus memberikan hasil yang konsisten setiap kali dijalankan. Dengan mengisolasi tes dari layanan eksternal yang tidak menentu, hasil tes akan tetap sama.
   - Self-Validating: Tes harus memiliki assertion (pernyataan) yang jelas untuk menentukan lulus atau gagal tanpa perlu pengecekan manual. Di tutorial, penggunaan assertEquals atau assertThrows memastikan tes tervalidasi sendiri.
   - Thorough/Timely: Tes harus mencakup happy path (jalur normal) dan unhappy path (jalur error). Prinsip "Timely" juga berarti tes ditulis sebelum kode fungsionalnya, sesuai dengan alur RED-GREEN-REFACTOR.

### Bonus 2 Reflections
Link PR Bonus 2: https://github.com/B-Hasanul-Muttaqin-2406413331/Modul-1-Coding-Standard/pull/7

Suggested changes for my team mate (my comment in their PR): https://github.com/B-Hasanul-Muttaqin-2406413331/Modul-1-Coding-Standard/pull/6#issuecomment-4031534339

1. Analysis of Partner's Code
   The initial implementation of the Payment module by my partner was functional and successfully passed all basic unit tests, which is a great starting point. However, there were a few architectural aspects that could be improved:
     - Tight Coupling: The core Payment model was directly responsible for the validation logic of every supported payment method (Voucher and COD).
     - Low Extensibility: Adding a new payment method required modifying the internal logic of the Payment class, making it harder to scale the project without introducing bugs.

2. My Contributions
   To improve the code quality of the order branch, I contributed the following:
      - Code Review: Analyzed Payment.java, PaymentRepository.java, and PaymentServiceImpl.java to identify potential maintenance risks.
     - Architecture Redesign: Introduced the Strategy Pattern to decouple validation logic from the data model.
     - Refactoring Execution: Created a new package for payment strategies, extracted validation logic into dedicated classes, and updated the Payment model to use these strategies.
     - Testing & Verification: Ensured that the refactored code still passes all existing test cases using ./gradlew test.

3. Identified Code Smells
   During the review, I identified two primary code smells:
      - Long Method & Large Class: The calculateStatus method and its helpers (validateVoucher, countDigits, validateCashOnDelivery) made the Payment class bloated and gave it too many responsibilities.
     - Switch Statements / Conditional Complexity: The use of multiple if statements to check the payment method string violates the Open-Closed Principle, as the class must be modified every time a new method is added.

4. Refactoring Steps
   I suggested and executed the following steps to fix the identified smells:
     - Interface Extraction: Created the PaymentStrategy interface to define a common contract for all payment validations.
     - Concrete Strategy Creation: Extracted logic into VoucherPaymentStrategy and CashOnDeliveryPaymentStrategy classes.
     - Encapsulation of Constants: Moved payment-specific constants (like VOUCHER_CODE_LENGTH) into their respective strategy classes to improve encapsulation.
     - Composition over Inheritance: Updated Payment.java to use a Map of strategies, allowing it to determine the status dynamically based on the input method without complex conditional logic.
     - Clean up: Removed redundant helper methods from the Payment model to keep it focused strictly on data representation.