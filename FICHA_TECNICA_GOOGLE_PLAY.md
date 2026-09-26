# Ficha técnica — Google Play · Barrioteca Acalencá

> Documento de referencia para subir la versión definitiva a Google Play Console.
> Repo: `barrioteca-android-app-v2` · Generado: 2026-09-26.

---

## 1. Datos básicos

| Campo | Valor |
|---|---|
| Nombre de la app | **Barrioteca Acalencá** |
| Application ID / package | `com.lebeche.barrioteca` |
| versionCode (última) | `47` — **auto-incrementado por CI en cada push a `main`** |
| versionName (última) | `3.2.9` |
| minSdk / targetSdk / compileSdk | 28 / 36 / 36 |
| Tipo | App **nativa** (Kotlin + Jetpack Compose, Material 3) |
| Categoría sugerida | Libros y referencias (alternativa: Estilo de vida) |
| Etiquetas (tags) | biblioteca, lectura, barrio, autogestión, libros |
| Precio | Gratuita (sin compras in-app, sin anuncios) |
| Firma | `signing.keystore` (alias `my-key-alias`) — misma firma que la versión actual publicada |
| Formato de subida | **Android App Bundle (`.aab`)** → `app/build/outputs/bundle/release/app-release.aab` |

> ⚠️ **Regla del proyecto**: cada compilación sube el número de versión. El CI lo hace
> automáticamente (`chore(release): bump version [skip ci]`), así que el `versionCode`/`versionName`
> exactos cambian con cada push. Consulta el valor vigente en `app/build.gradle`.

---

## 2. Assets para la ficha de Play

| Asset | Especificación de Play | Archivo |
|---|---|---|
| **Icono** | 512×512 px, PNG 32 bits (con alfa) | `play-store/icono-512.png` |
| **Feature graphic** | 1024×500 px, PNG/JPG | `play-store/feature-graphic-1024x500.png` |
| Capturas de pantalla | min 320 px, máx 3840 px (móvil 16:9 o 9:16) | ⏳ pendiente — capturar desde el emulador/dispositivo |

> El icono definitivo (silueta monocroma sobre fondo blanco, sin texto) también está respaldado en:
> - `G:\GITHUB\keystore-backup\barrioteca-android-app-v2\icono-launcher\`
> - `G:\GITHUB\LOGOS\Barrioteca\`

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

Todo funciona sobre un servidor propio y autogestionado (software libre SLiMS),
sin depender de servicios externos: tus datos no se comparten con terceros.

La Barrioteca es un proyecto de la asociación Lebeche. ¡Hazte socia y participa!
```

---

## 4. Privacidad y acceso

| Campo | Valor |
|---|---|
| URL de política de privacidad | `https://www.corrientelebeche.es/barrioteca/privacidad.html` |
| Correo de contacto | `monderas@corrientelebeche.es` |
| Web | `https://www.corrientelebeche.es/barrioteca/` |

### 4.1 Acceso para la revisión de Google

- Login **sin contraseña**: solo se introduce un **ID de socia** (o se escanea el carné).
- **ID de socia de prueba**: `JCL327` (socia real de pruebas).
- No hay cuenta/password; el revisor puede usar `JCL327` directamente en la pantalla de login.

---

## 5. Data Safety (declaración sugerida)

| Sección | Respuesta sugerida |
|---|---|
| ¿La app recopila datos? | **Sí** (solo un dato mínimo) |
| Dato recopilado | **Identificadores → ID de usuario** (el número de carné de socia) |
| Finalidad | Funcionalidad de la app (préstamos/devoluciones de la biblioteca) |
| ¿Se comparte con terceros? | **No** |
| Cifrado en tránsito | **Sí** (HTTPS) |
| Eliminación de datos | El usuario puede solicitar el borrado (contactar con la asociación) |

> Nota: la app **no** pide permisos de ubicación, contactos, micrófono, etc. Solo usa
> **Cámara** (para escanear códigos, opcional) y **Notificaciones** (avisos de vencimientos).

---

## 6. Checklist previo al envío

- [ ] Compilar y descargar el `.aab` firmado de la última CI (release `build-N`).
- [ ] Verificar que `versionCode`/`versionName` son mayores que los publicados.
- [ ] Subir icono (`play-store/icono-512.png`) y feature graphic.
- [ ] Capturar y subir **capturas de pantalla** (login, catálogo, detalle, dashboard).
- [ ] Rellenar **content rating** (cuestionario IARC): contenido general, sin violencia/sexo/apuestas.
- [ ] Rellenar **Data Safety** (sección 5).
- [ ] Confirmar **política de privacidad** accesible (URL de la sección 4).
- [ ] Indicar **acceso de revisión** (`JCL327`).
- [ ] Publicar como **actualización** de la app existente (mismo `applicationId` + misma firma).

---

## 7. Enlaces

- Repo: `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2`
- Releases (APK+AAB): `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2/releases`
- GitHub Packages (Maven, APK): `https://github.com/jesuscastilla/acalenca-barrioteca-app-android-v2/packages`
- Política de privacidad: `https://www.corrientelebeche.es/barrioteca/privacidad.html`
- Contexto general del proyecto: `G:\GITHUB\CONTEXT.md`
