# Prototype KitaBisa HomeWidget Company Capstone
Bangkit Academy led by Google, GoTo & Traveloka 2023 Half 2

Team Members :
* (MD) A694BSY1980 - Ade Rocky Saputra
* (MD) A626BSY2493 - Abdul Azis
* (MD) A694BSY2345 - Azzar Rizky
* (MD) A694BSY1982 - Muhammad Anugrah Hakiki

## Main Purpose
Assalamu'alaikum...

Repository ini dibuat sebagai pendukung dari proposal company capstone BANGKIT 2023 Batch 2 dari KitaBisa.com . Semoga dengan Prototype sederhana yang kami buat, dapat menjadi pertimbangan agar kami dapat mendukung pengembangan widget pada Aplikasi KitaBisa.com 

## Prototype Screenshots
Berikut beberapa activity dalam konsep mengembangkan Widget ini


![Group 2](https://github.com/anhaki/KitaBisa-Homewidget-Prototype/assets/90712252/496094f0-9ea7-4ab0-bd67-9ebe55e85f62)

## Features
#### 1. Prayer Schedule Widget
-Work Flow :
- [x] CTA Detail Donasi Otomatis
* Pending Intent & GetActivity Donasi Otomatis melalui Widget.

- [x] CTA Isi Saldo
* Pending Intent & GetActivity Isi Saldo melalui Widget.

- [x] Sisa Saldo Kantong Donasi
* Untuk Kasus ini dikarenakan dibutuhkannya API dalam Memanggil Nominal sisa Kantong Donasi, Jadi Tim kami menampilkan Nilai yang ada pada berkas Activity dan XML.

- [x] Get API sholat sesuai dengan location User menggunakan [API Sholat Banghasan](https://fathimah.docs.apiary.io/)
terdapat beberapa langkah dalam implementasi fitur Jadwal Sholat berdasarkan lokasi user diantaranya :
1. Get Location User 
* Digunakan pada SettingsActicity untuk menyimpan lokasi Kota User
2. SharedPref 
* Digunakan untuk menyimpan preference kota user
3. Get ID kota user 
* Menggunakan Response dari endpoint
-https://api.banghasan.com/sholat/format/json/kota/nama/{nama kota}
4. Get API Sholat 5 waktu dari ID kota user
* Menggunakan Response dari endpoint
-https://api.banghasan.com/sholat/format/json/jadwal/kota/{ID kota}/tanggal/{getDate}

- [x] Menampilkan Kalender Masehi

- [x] Menampilkan Kalender Hijriyah

### 2. Donation History Widget
Work Flow :
- [x] CTA Detail Donasi Otomatis
* Pending Intent & GetActivity Donasi Otomatis melalui Widget.

- [x] CTA Isi Saldo
* Pending Intent & GetActivity Isi Saldo melalui Widget.


- [x] Sisa Saldo Kantong Donasi & Riwayat Donasi
* Sama seprti pada Kasus widget sebelumnya. Dikarenakan dibutuhkannya API dalam Memanggil  Data nominal sisa Kantong Donasi dan Data Riwayat Donasi, Jadi Tim kami menampilkan Nilai yang ada pada berkas Activity dan XML.



