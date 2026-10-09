# Ficha técnica — Google Play · Barrioteca Acalencá

> Documento de referencia para subir / actualizar la app en **Google Play Console**.
> Repo: `barrioteca-android-app-v2` · Última revisión: **2026-10-09**.

---

## 0. Resumen (todo en un vistazo)

| Campo | Valor |
|---|---|
| Nombre | **Barrioteca Acalencá** |
| Package / Application ID | `com.lebeche.barrioteca` |
| Tipo | App **nativa** (Kotlin + Jetpack Compose, Material 3) |
| Estado | ✅ **En producción** (publicada como actualización) |
| Precio | Gratuita · sin anuncios · sin compras in-app |
| Firma | `signing.keystore` — alias `my-key-alias` |
| Formato de subida | Android App Bundle (`.aab`) |
| Política de privacidad | `https://www.corrientelebeche.es/barrioteca/privacidad.html` |

---

## 1. Datos básicos

| Campo | Valor |
|---|---|
| Nombre de la app | **Barrioteca Acalencá** |
| Application ID / package | `com.lebeche.barrioteca` |
| versionCode (última) | `58` — **auto-incrementado por el CI** en cada push a `main` |
| versionName (última) | `3.2.20` — auto-incrementado (parche) |
| minSdk / targetSdk / compileSdk | 28 / 37 / 37 |
| AGP / Gradle / Kotlin | 9.4.1 / 9.8.0 / 2.2.10 (Kotlin integrado en AGP) |
| Tipo | App **nativa** (Kotlin + Jetpack Compose, Material 3) |
| Categoría sugerida | **Libros y referencias** (alternativa: Estilo de vida) |
| Etiquetas (tags) | biblioteca, lectura, barrio, autogestión, libros |
| Precio | Gratuita (sin compras in-app, sin anuncios) |
| Firma | `signing.keystore` (alias `my-key-alias`) — misma firma que la versión publicada |
| Formato de subida | **Android App Bundle (`.aab`)** → `app/build/outputs/bundle/release/app-release.aab` |

> ⚠️ **Regla del proyecto**: cada compilación sube el número de versión. El CI lo hace
> automáticamente (`chore(release): bump version [skip ci]`), así que `versionCode`/`versionName`
> cambian con cada push. Consulta el valor vigente en `app/build.gradle` o en el último release de GitHub.

---

## 2. Assets para la ficha de Play

| Asset | Especificación de Play | Archivo |
|---|---|---|
| **Icono** | 512×512 px, PNG 32 bits (con alfa) | `play-store/icono-512.png` |
| **Feature graphic** | 1024×500 px, PNG/JPG | `play-store/feature-graphic-1024x500.png` |
| **Captura 1 — Login** | 1080×2400 px (móvil 9:16) | `play-store/captura-1-login.png` |
| **Captura 2 — Catálogo** | 1080×2400 px | `play-store/captura-2-catalogo.png` |
| **Captura 3 — Detalle de libro** | 1080×2400 px | `play-store/captura-3-detalle-libro.png` |
| **Captura 4 — Dashboard** | 1080×2400 px | `play-store/captura-4-dashboard.png` |
| **Captura 5 — Escáner** | 1080×2400 px | `play-store/captura-5-escanear.png` |
| **Captura 6 — Ajustes** | 1080×2400 px | `play-store/captura-6-ajustes.png` |
| **Captura 7 — Proyecto** | 1080×2400 px | `play-store/captura-7-proyecto.png` |

> El icono definitivo (silueta monocroma sobre fondo blanco, sin texto) también está respaldado en:
> - `G:\GITHUB\keystore-backup\barrioteca-android-app-v2\icono-launcher\`
> - `G:\GITHUB\LOGOS\Barrioteca\` (versiones transparente y con fondo blanco, más los `mipmap-*`).

---

## 3. Textos de la ficha

### 3.1 Título (hasta 30 caracteres)

```
Barrioteca Acalencá
```

### 3.2 Descripción breve (hasta 80 caracteres)

```
Biblioteca vecinal autogestionada: catálogo, préstamos y devoluciones desde el móvil.
```

### 3.3 Descripción completa (hasta 4 000 caracteres)

```
La Barrioteca Acalencá es la biblioteca vecinal autogestionada de la asociación
cultural Lebeche, en Salobreña (Granada). Un espacio comunitario para fomentar
la lectura y el intercambio cultural entre las vecinas y los vecinos.

Con esta app puedes:

• Consultar el catálogo completo de la biblioteca y buscar por título, autora,
  ISBN o ejemplar.
• Ver la portada y la sinopsis de cada libro.
• Identificarte como socia con solo tu número de carné (sin contraseña) o
  escaneando el código QR o de barras de tu carné.
• Ver tus préstamos activos y sus fechas de vencimiento.
• Pedir un libro y registrar la devolución escaneando el código del ejemplar.
• Recibir avisos cuando un préstamo está a punto de vencer o cuando llegan
  novedades al catálogo.

Los datos de la biblioteca viven en un servidor propio y autogestionado (software
libre SLiMS) y no se comparten con terceros. Las notificaciones push se entregan
a través de Firebase (Google) únicamente para avisarte de vencimientos y novedades.

La Barrioteca es un proyecto de la asociación Lebeche. ¡Hazte socia y participa!
```

---

## 4. Privacidad, seguridad y acceso

### 4.1 Enlaces y contacto

| Campo | Valor |
|---|---|
| URL de política de privacidad | `https://www.corrientelebeche.es/barrioteca/privacidad.html` |
| Correo de contacto | `monderas@corrientelebeche.es` |
| Web de la app | `https://www.corrientelebeche.es/barrioteca/` |
| Web de la asociación | `https://www.corrientelebeche.es` |

### 4.2 Acceso para la revisión de Google

- Login **sin contraseña**: solo se introduce un **ID de socia** (o se escanea el carné).
- **ID de socia de prueba**: `JCL327` (socia real de pruebas).
- No hay cuenta/password; el revisor puede usar `JCL327` directamente en la pantalla de login.

---

## 5. Data Safety (declaración para Play Console)

> La app **recopila datos mínimos** y **no los comparte con terceros**. Todo viaja cifrado (HTTPS)
> a un servidor propio de la asociación.

| Pregunta de Play | Respuesta |
|---|---|
| ¿La app recopila o comparte datos? | **Sí** (datos mínimos, ver 5.1) |
| ¿Se comparte con terceros? | **No** |
| ¿Cifrado en tránsito? | **Sí** (HTTPS/TLS) |
| ¿El usuario puede solicitar el borrado? | **Sí** (contactando con la asociación; en el móvil, borrando los datos de la app) |

### 5.1 Datos recopilados

| Tipo de dato | Dato concreto | Finalidad | ¿Se comparte? |
|---|---|---|---|
| Identificadores → **ID de usuario** | Número de carné de socia | Funcionalidad de la app (préstamos/devoluciones) | No |
| Identificadores → **ID de dispositivo u otros ID** | Token de registro de Firebase (FCM) para notificaciones push | Funcionalidad de la app (avisos) | Solo con Firebase (infraestructura de push, proveedor de servicios) |

### 5.2 Notas

- La app **no** pide permisos de ubicación, contactos, micrófono ni almacenamiento. Solo usa:
  - **Cámara** (escanear códigos, opcional): la imagen se procesa en el dispositivo y **no se guarda ni se sube**.
  - **Notificaciones** (avisos de vencimientos y novedades, locales y push).
- Los datos de la biblioteca se guardan en el **NAS propio de la asociación** (Salobreña). En el dispositivo
  solo se conservan la sesión de socia, el historial de operaciones y las preferencias (SharedPreferences).
- Coherencia: conviene que la política de privacidad pública (`privacidad.html`) mencione también las
  **notificaciones push (FCM)** para que coincida con esta declaración.

---

## 6. Permisos de la app

| Permiso | Por qué |
|---|---|
| `INTERNET` | Conectar con la API de la biblioteca (SLiMS) |
| `ACCESS_NETWORK_STATE` | Comprobar si hay conexión antes de sincronizar |
| `CAMERA` | Escanear códigos de barras / QR (libros y carnés) |
| `POST_NOTIFICATIONS` | Avisos locales y push (FCM) |

> No se solicitan permisos de ubicación, contactos, micrófono ni almacenamiento.

---

## 7. Content rating (cuestionario IARC)

| Pregunta | Respuesta |
|---|---|
| Contenido | General: sin violencia, sexo, drogas ni apuestas |
| Clasificación esperada | **Todas las edades** (PEGI 3 / ESRB Everyone) |

---

## 8. Checklist previo al envío

- [x] Compilar y descargar el `.aab` firmado del último release de CI (release `build-N`).
- [x] Verificar que `versionCode`/`versionName` son mayores que los publicados.
- [x] Subir icono (`play-store/icono-512.png`) y feature graphic.
- [x] Capturar y subir **capturas de pantalla** (login, catálogo, detalle, dashboard, escáner, ajustes, proyecto).
- [ ] Rellenar **content rating** (cuestionario IARC) — sección 7.
- [ ] Rellenar **Data Safety** — sección 5.
- [ ] Confirmar **política de privacidad** accesible — sección 4.
- [ ] Indicar **acceso de revisión** (`JCL327`).
- [ ] Publicar como **actualización** de la app existente (mismo `applicationId` + misma firma).

---

## 9. Enlaces útiles

- Repo: `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2`
- Releases (APK+AAB): `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2/releases`
- GitHub Packages (Maven, APK): `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2/packages`
- Política de privacidad: `https://www.corrientelebeche.es/barrioteca/privacidad.html`
- Contexto general del proyecto: `G:\GITHUB\CONTEXT.md`
