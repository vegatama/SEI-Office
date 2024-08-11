# SEI OFFICE Mobile Apps v2
## Instalasi
1. Clone repository ini
2. Ubah environment variables di .env/dev.json dan .env/prod.json
## Run
### Android Studio
Anda bisa menggunakan run configuration yang telah disediakan di direktori `.run`
### Command Line
Cukup jalankan perintah berikut:
#### Development Mode
```bash
flutter run --dart-define-from-file=.env/dev.json --web-browser-flag "--disable-web-security"
```
#### Production Mode
```bash
flutter run --dart-define-from-file=.env/prod.json --web-browser-flag "--disable-web-security"
```
## Build
### Android
```bash
flutter build apk --dart-define-from-file=.env/prod.json --release
```
### iOS
```bash
flutter build ios --dart-define-from-file=.env/prod.json --release
```