# Project Etereum 🛰️

**Optimización de Archivos para Redes Radio de Combate (CNR)**

**Etereum** es una aplicación táctica para dispositivos Android (móviles y tablets) diseñada para unidades de combate. Su función es optimizar el tamaño de los archivos antes de su transmisión a través de medios radio estrechos, garantizando la eliminación de metadatos sensibles.

## 🚀 Capacidades Principales

### Fase 1: Inteligencia de Imagen
- **Reducción Táctica:** Recorte, cambio de resolución y formato (WebP/JPG) para minimizar el impacto en el ancho de banda.
- **Sanitización Forense:** Eliminación de metadatos EXIF/GPS para evitar el rastreo del emisor.
- **Comparador de Calidad:** Visualización side-by-side (Original vs. Procesada) con indicador de peso en tiempo real.
- **Gestión de Misión:** Almacenamiento organizado en carpetas seguras dentro del sandbox de la app.

### Fase 2: Transmutación de Documentos
- Procesamiento de PDF, Word y Excel para conversión a texto plano.
- Algoritmos de empaquetado para permitir la reversibilidad del documento.

## 🛠️ Stack Tecnológico
- **Lenguaje:** Kotlin 100%.
- **UI:** Jetpack Compose (Modern Declarative UI).
- **Arquitectura:** Clean Architecture con patrón MVI (Model-View-Intent).
- **Concurrencia:** Kotlin Coroutines & Flow.

## 📂 Estructura del Proyecto (Clean Architecture)
- `/app`: Capa de UI (Compose), ViewModels y DI (Dependency Injection).
- `/domain`: Capa de negocio pura (Interfaces de servicios, Modelos de datos y Use Cases).
- `/data`: Implementación de interfaces (Repisitorios, Procesamiento de archivos, SharedPreferences).

## 🛡️ Principios de Desarrollo
Aunque el proyecto es actualmente exclusivo para Android, se desarrolla bajo el principio de **Inversión de Dependencias**. Toda la lógica de procesamiento está abstraída tras interfaces en la capa de `domain`, facilitando migraciones futuras a otros sistemas o integraciones con librerías nativas (C++/Rust).

---
*Uso exclusivo para personal autorizado.*