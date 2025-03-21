# Travel Planner - Diseño del Sistema
![project_estructure_mermaid.png](project_estructure_mermaid.png)
## Resumen
La aplicación **Travel Planner** está diseñada para ayudar a los usuarios a planificar viajes, gestionar itinerarios y colaborar con otros. Incluye funciones como recomendaciones de viajes, autenticación y servicios de mapas.

## Diagrama de Clases
El sistema sigue un enfoque **orientado a objetos**, con clases clave que representan usuarios, viajes, elementos del itinerario y entidades de soporte adicionales.

### Componentes Principales:

### 1. **Usuario (User)**
- Representa un usuario registrado.
- Almacena detalles personales y una lista de viajes.
- Puede actualizar su perfil, registrarse y eliminar su cuenta.

### 2. **Autenticación (Authentication)**
- Administra el inicio y cierre de sesión del usuario, así como la recuperación de contraseña.
- Soporta autenticación de dos factores.

### 3. **Preferencias (Preferences)**
- Almacena configuraciones específicas del usuario, como idioma, tema y preferencias de notificaciones.

### 4. **Viaje (Trip)**
- Representa un viaje planificado.
- Almacena detalles del viaje, elementos del itinerario e imágenes subidas.
- Permite compartir viajes con colaboradores.

### 5. **Recomendaciones de IA (AIRecommendations)**
- Proporciona actividades recomendadas y viajes sugeridos basados en el historial del usuario.

### 6. **Elemento del Itinerario (ItineraryItem)**
- Representa un evento o actividad dentro de un viaje.
- Incluye detalles como título, hora, ubicación y descripción.

### 7. **Imagen (Image)**
- Almacena imágenes relacionadas con los viajes.

### 8. **Mapa (Map)**
- Proporciona servicios basados en ubicación, incluyendo navegación y lugares cercanos.

## Relaciones:
- **Usuario** tiene preferencias y gestiona la autenticación.
- **Usuario** posee múltiples viajes.
- **Viaje** contiene elementos de itinerario y almacena imágenes.
- **Recomendaciones de IA** sugiere actividades y viajes.
- **Mapa** ayuda a mostrar ubicaciones y direcciones.

## Decisiones Arquitectónicas

### 1. **Arquitectura Orientada a Objetos (OOP)**
Se eligió un diseño basado en clases para modularizar el sistema y representar las entidades principales de manera clara y reutilizable. Cada clase encapsula datos y funcionalidades específicas, facilitando la escalabilidad y el mantenimiento.

### 2. **Modularidad y Separación de Responsabilidades**
Cada clase se centra en una única responsabilidad, lo que permite modificar o mejorar funcionalidades sin afectar otras partes del sistema. Esto sigue el principio SOLID de diseño de software.

### 3. **Escalabilidad y Extensibilidad**
El diseño permite agregar nuevas funcionalidades en el futuro, como un sistema de presupuesto o integración con servicios externos (API de mapas, clima, etc.).

### 4. **Recomendaciones de IA**
La inclusión de inteligencia artificial para sugerir actividades y viajes proporciona una experiencia personalizada basada en el historial del usuario y preferencias pasadas.

### 5. **Uso de Listas para Relacionar Entidades**
Se utilizan listas para modelar relaciones de uno a muchos, por ejemplo, un usuario con múltiples viajes o un viaje con múltiples elementos de itinerario. Esto simplifica la gestión de datos en la aplicación.

### 6. **Posibilidad de Integración con Bases de Datos**
El sistema está diseñado de manera que cada entidad pueda mapearse fácilmente a una base de datos relacional o NoSQL en el futuro.

## Estructura del Proyecto en Android Studio

**Estructura MVVM + Modular**
- `Model`: Maneja los datos y la lógica de negocio (ej. clases como `User`, `Trip`).
- `View`: Maneja la UI (archivos XML y Activities/Fragments en Kotlin o Java).
- `ViewModel`: Maneja la lógica de presentación y sobrevive a cambios de configuración.
- Se separa el proyecto en diferentes módulos (app, core, features).
- Facilita la escalabilidad y permite la reutilización de código.

## Mejoras Futuras
- **Gestión de Presupuesto**: Agregar seguimiento financiero a los viajes.
- **Colaboradores**: Permitir que varios usuarios editen viajes.
- **Modo Offline**: Habilitar acceso a los viajes sin conexión a internet.

Este documento describe la estructura principal y las interacciones dentro de la aplicación **Travel Planner**, asegurando una arquitectura bien definida para su desarrollo futuro.

