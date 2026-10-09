# Barrioteca Acalenca — App Android nativa v2

App Android **100% nativa** (Kotlin + Jetpack Compose) de la Barrioteca Acalenca.
Sustituye a la carcasa WebView (`barrioteca-android-app`) reutilizando el mismo
backend: el proxy `api-proxy.php` que expone la API de SLiMS.

## Estado

✅ **Disponible en producción** (Google Play). Publicada como **actualización** de la
app existente (`applicationId com.lebeche.barrioteca`, misma firma `my-key-alias`).

Cada push a `main` dispara el CI (`release.yml`), que:
1. Auto-incrementa `versionCode` y el parche de `versionName`.
2. Compila y firma el APK y el AAB.
3. Publica el release en GitHub Releases y el APK en GitHub Packages.

> Actualmente: `versionCode 57` / `versionName 3.2.19` (el CI lo incrementa en cada build).

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
- **Firebase Cloud Messaging (FCM)** preparado para recibir notificaciones push
  enviadas desde el backend o la consola de Firebase.

### Stack
Kotlin + Jetpack Compose (Material 3) · OkHttp + corrutinas · Coil (portadas) ·
CameraX + ML Kit Barcode Scanning · WorkManager · Firebase Cloud Messaging.

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
  + `versionCode`/`versionName` auto-incrementados por el CI en cada push (actualmente `57`/`3.2.19`)
  y firma con el mismo `signing.keystore` (`my-key-alias`).
  El keystore, `keystore.properties` y `app/google-services.json` ya están copiados localmente (NO versionados en Git).

## Últimos cambios

- **Botón "Atrás" corregido**: ya no cierra/envía a segundo plano la app. Al pulsar Atrás en una
  sección del menú vuelve a la sección principal (Inicio/Entrar) y, dentro del escáner, vuelve a la
  pantalla anterior (login o préstamo/devolución) mediante `BackHandler`.
- Icono del launcher sin la ampliación (`<inset -20dp>` eliminado): se veía recortado/ampliado.
- Tema alineado con la carta de color de Lebeche (azul `#3B758B` como primario, `#A9D9ED` container, `#26373E` on-container y ámbar `#E8A33D` como acento).
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

## Disponibilidad

- **Google Play**: publicada como *Barrioteca Acalencá* (`com.lebeche.barrioteca`).
- **Releases (APK + AAB)**: https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2/releases
- **GitHub Packages (APK)**: https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2/packages
- **Política de privacidad**: https://www.corrientelebeche.es/barrioteca/privacidad.html
- **Ficha técnica Play**: `FICHA_TECNICA_GOOGLE_PLAY.md`

## Repositorio

- `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2`

> Contexto general del proyecto en `g:\GITHUB\CONTEXT.md`.
