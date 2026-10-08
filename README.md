# Studi Kasus Java OOP (Clean Architecture + Generics + Exceptions)

| Folder | Aplikasi | Fitur utama |
|---|---|---|
| `catatan-keuangan/` | Pencatat transaksi | Pemasukan/pengeluaran, cari, urutkan, saldo, hapus |
| `buku-tamu/` | Buku tamu digital | Daftarkan, cari (case-insensitive), hapus |
| `inventaris-barang/` | Inventaris | Tambah, ubah stok, cari, urutkan, hapus |
| `jadwal-kegiatan/` | Jadwal harian | Tambah, ubah parsial, cari, urutkan, hapus |
| `kontak-teman/` | Kontak | Tambah, ubah parsial, cari, urutkan, hapus |

## Compile & jalankan satu aplikasi
```bash
cd buku-tamu
javac -d output -sourcepath src src/App.java
java -cp output App
```
Jalankan dengan test case (input dari file):
```bash
java -cp output App < test-cases/TC-01.tc
```

## Jalankan seluruh test case sekaligus
```bash
python run_tests.py              # semua studi kasus (compile + TC-01..TC-04)
python run_tests.py kontak-teman # satu studi kasus
```
Output asli tiap test case tersimpan di `<studi-kasus>/test-cases/TC-0X.output.txt`
(bisa dipakai sebagai bukti/screenshot laporan). Hasil terakhir: **20/20 test case lulus**.

## Struktur layer (tiap studi kasus)
```
src/
├── domain/      entity, exception, repository (IRepository<T, ID> + port spesifik)
├── usecase/     logika bisnis, tanpa I/O, melempar checked domain exception
├── adapter/     InMemoryRepository<T, ID>, repository konkret, presenter
├── framework/   view (menu + try-catch), util/InputUtil
└── App.java     Composition Root
```
Dependency mengarah ke dalam: `domain` tidak mengenal layer lain, `usecase` hanya bergantung ke `domain`.

## Catatan
- Urutan menu "Urutkan" Catatan Keuangan: 1 Jumlah terbesar, 2 Jumlah terkecil, 3 Pemasukan dulu,
  4 Pengeluaran dulu (disesuaikan agar cocok dengan expected output TC-02).
- Pesan error yang tidak ada di soal (mis. hari/waktu/email tidak valid) memakai format yang sama: `[!] ... !`.
