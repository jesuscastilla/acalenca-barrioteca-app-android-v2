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
.\gradlew.bat :app:testDebugUnitTest   # Tests unitarios JVM
```

Salidas:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

## Decisiones ya tomadas

- Publicación como **actualización** de la app actual: `applicationId com.lebeche.barrioteca`
  + `versionCode 43` (auto-incrementado por el CI en cada push) y firma con el mismo
  `signing.keystore` (`my-key-alias`).
  El keystore y `keystore.properties` ya están copiados localmente (NO versionados en Git).

## Últimos cambios (v3.2.5, Code 43)

- Sinopsis del catálogo **bajo demanda** (`action=book-detail&id=`) al abrir el detalle de un libro.
- Refresco automático de catálogo y préstamos tras prestar/devolver (`RefreshSignal`).
- Contacto por correo a prueba de fallos + `<queries>` mailto en el manifest.
- Manifest: `dataExtractionRules`/`fullBackupContent` (excluyen la sesión de socia de backups) y `enableOnBackInvokedCallback`.
- Tests unitarios JVM (`ParsersTest`, `DateUtilsTest`) y CI con auto-bump de versión + publicación del APK en GitHub Packages.

## Cambios anteriores (v3.0.0, Code 35)

- Modificado el diseño del icono (`ic_launcher.xml` con `<inset>`) para que se muestre centrado y sin letras.
- Añadida lógica en `MainActivity.kt` (Google Play In-App Updates) para forzar la actualización de la app si hay una nueva versión (Inmediata).
- Limpiadas las `SharedPreferences` al detectarse una nueva actualización para que fuerce a las usuarias a re-iniciar sesión.
- Solucionado el problema con la carga de portadas (`Parsers.kt`) en el catálogo prefijando la URL base de la imagen.

## Repositorio

- `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2`

> Contexto general del proyecto en `g:\GITHUB\CONTEXT.md`.
