# Barrioteca Acalenca — App Android nativa v2

App Android **100% nativa** (Kotlin + Jetpack Compose) de la Barrioteca Acalenca.
Sustituye a la carcasa WebView (`barrioteca-android-app`) reutilizando el mismo
backend: el proxy `api-proxy.php` que expone la API de SLiMS.

## Estado

Implementada en una primera versión funcional.

### Funcionalidades
- **Login de socia sin contraseña**: entrada manual del ID o **escaneo del carné
  (QR / código de barras)** con CameraX + ML Kit.
- **Dashboard** con "Mis préstamos" (lista y fechas de vencimiento).
- **Catálogo** completo con búsqueda (título, autora, ISBN, ejemplar) y detalle
  con sinopsis + botón "Pedir este libro".
- **Préstamo / devolución** por escaneo de código de barras del libro o entrada
  manual, con historial local.
- **Sincronización periódica con SLiMS** (WorkManager, cada 15 min) y
  **notificaciones locales** de cambios: caducidad de la membresía, préstamos
  nuevos, devoluciones, préstamos próximos a vencer y libros nuevos añadidos.

### Stack
Kotlin + Jetpack Compose (Material 3) · OkHttp + corrutinas · Coil (portadas) ·
CameraX + ML Kit Barcode Scanning · WorkManager.

## Compilar

```powershell
cd G:\GITHUB\barrioteca-android-app-v2
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:ANDROID_HOME='C:\Users\jesus\AppData\Local\Android\Sdk'
.\gradlew.bat :app:assembleDebug        # APK de desarrollo
.\gradlew.bat :app:assembleRelease     # APK firmado (requiere keystore.properties)
```

Salidas:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

## Decisiones pendientes

- `applicationId`: de momento se usa `com.lebeche.barrioteca` + `versionCode 34`
  (para poder publicar como actualización en Google Play). Pendiente de confirmar.
- Firma: falta copiar `signing.keystore` / `keystore.properties` a esta carpeta
  para compilar el release firmado.
- Repositorio remoto en GitHub: pendiente de crear.

> Contexto general del proyecto en `g:\GITHUB\CONTEXT.md`.
