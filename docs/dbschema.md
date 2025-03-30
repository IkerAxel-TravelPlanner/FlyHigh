# FlyHigh App - Diseño de la Base de Datos

Este documento describe el esquema de la base de datos utilizado en la aplicación **FlyHigh**. Detalla las tablas, sus columnas, las relaciones entre ellas y cómo se utilizan en la lógica de la aplicación.

---

## 📌 Esquema de la Base de Datos

La aplicación **FlyHigh** utiliza una base de datos **Room** para almacenar los datos localmente en el dispositivo del usuario. La base de datos consta de las siguientes tablas:

### 🛫 1. Tabla `TripEntity`

**Propósito:** Almacena información sobre cada viaje creado por el usuario.

| Columna       | Tipo de Dato | Restricciones                     | Descripción                                   |
|--------------|-------------|----------------------------------|-----------------------------------------------|
| `id`        | `INTEGER`   | Clave Primaria, Autogenerado     | Identificador único para el viaje.            |
| `title`     | `TEXT`      | No Nulo                          | Título o nombre del viaje.                    |
| `destination` | `TEXT`    | No Nulo                          | Destino del viaje.                            |
| `startDate` | `DATE`      | No Nulo                          | Fecha de inicio del viaje.                    |
| `endDate`   | `DATE`      | No Nulo                          | Fecha de finalización del viaje.              |
| `description` | `TEXT`    | Nulo Permitido                   | Una breve descripción o notas sobre el viaje. |
| `imageUrl`  | `TEXT`      | Nulo Permitido                   | URL o ruta a una imagen asociada con el viaje.|

### 📍 2. Tabla `ItineraryItemEntity`

**Propósito:** Almacena información sobre cada elemento del itinerario asociado a un viaje.

| Columna       | Tipo de Dato | Restricciones                                      | Descripción                                   |
|--------------|-------------|--------------------------------------------------|-----------------------------------------------|
| `id`        | `INTEGER`   | Clave Primaria, Autogenerado                     | Identificador único del elemento del itinerario. |
| `tripId`    | `INTEGER`   | Clave Foránea que referencia a `TripEntity.id` (`CASCADE`) | ID del viaje al que pertenece este elemento. |
| `name`      | `TEXT`      | No Nulo                                          | Nombre o título del elemento del itinerario. |
| `description` | `TEXT`    | Nulo Permitido                                   | Breve descripción del elemento.              |
| `location`  | `TEXT`      | Nulo Permitido                                   | Ubicación asociada con el elemento.          |
| `date`      | `DATE`      | No Nulo                                          | Fecha del evento.                            |
| `startTime` | `DATE`      | Nulo Permitido                                   | Hora de inicio (si aplica).                  |
| `endTime`   | `DATE`      | Nulo Permitido                                   | Hora de finalización (si aplica).            |
| `type`      | `TEXT`      | Nulo Permitido                                   | Tipo de evento (ej. "Vuelo", "Hotel").      |

### 👤 3. Tabla `UserEntity`

**Propósito:** Almacena información sobre cada usuario.

| Columna | Tipo de Dato | Restricciones                | Descripción                    |
|---------|-------------|----------------------------|--------------------------------|
| `id`    | `INTEGER`   | Clave Primaria, Autogenerado | Identificador único del usuario |
| `name`  | `TEXT`      | No Nulo                     | Nombre del usuario             |
| `email` | `TEXT`      | No Nulo                     | Dirección de correo del usuario |

---

## 🔗 Relaciones

- **Uno a Muchos:** Un `TripEntity` puede tener múltiples `ItineraryItemEntity`, pero cada `ItineraryItemEntity` pertenece a un solo `TripEntity`.
- **Clave Foránea:** La columna `tripId` en `ItineraryItemEntity` referencia `id` en `TripEntity` con `ON DELETE CASCADE`, eliminando automáticamente los elementos asociados cuando se borra un viaje.

---

## 🎯 Objetos de Acceso a Datos (DAOs)

### `TripDao`

**Métodos:**
- `getAllTrips()`: Devuelve un `Flow<List<TripEntity>>` con todos los viajes.
- `getTripById(tripId: Long)`: Devuelve un `Flow<TripEntity?>` con el viaje que coincide con el id.
- `insertTrip(trip: TripEntity)`: Inserta un nuevo viaje.
- `updateTrip(trip: TripEntity)`: Actualiza un viaje existente.
- `deleteTripById(tripId: Long)`: Elimina un viaje por su ID.

### `ItineraryItemDao`

**Métodos:**
- `getAllItineraryItems()`: Devuelve un `Flow<List<ItineraryItemEntity>>`.
- `getItineraryItemById(itineraryId: Long)`: Devuelve un `Flow<ItineraryItemEntity?>`.
- `insertItineraryItem(itineraryItem: ItineraryItemEntity)`: Inserta un nuevo elemento.
- `updateItineraryItem(itineraryItem: ItineraryItemEntity)`: Actualiza un elemento.
- `getItinerariesByTripId(tripId: Long)`: Obtiene todos los elementos de un viaje.
- `deleteItineraryItemById(itineraryId: Long)`: Elimina un elemento por ID.

---

## 🔄 Convertidores de Tipos

Para manejar datos complejos, la aplicación utiliza **Convertidores de Tipos**:

- **`UserConverter`**: Convierte `UserEntity` a cadena y viceversa.
- **`DateConverter`**: Convierte `Date` en `Long`.
- **`MapConverter`**: Convierte `Map` en cadena.
- **`ImageListConverter`**: Convierte `List<String>` en cadena.
- **`ItineraryItemListConverter`**: Convierte `List<ItineraryItemEntity>` en cadena.
- **`AIRecommendationsListConverter`**: Convierte `List<AIRecommendationEntity>` en cadena.

---

## 🛠 Uso

### 📌 Añadir un Nuevo Viaje
```kotlin
val newTrip = TripEntity(
   title = "Mi Viaje", 
   destination = "París", 
   startDate = Date(), 
   endDate = Date(), 
   description = "Un viaje a París", 
   imageUrl = null
)
tripDao.insertTrip(newTrip)
```

### ✏️ Actualizar un Elemento de Itinerario
```kotlin
val itineraryItemFlow: Flow<ItineraryItemEntity?> = itineraryItemDao.getItineraryItemById(itineraryId)
itineraryItemFlow.collect { existingItem ->
   existingItem?.let {
      val updatedItem = it.copy(name = "Nuevo nombre")
      itineraryItemDao.updateItineraryItem(updatedItem)
   }
}
```

---

Este documento describe la estructura principal y las interacciones dentro de la aplicación **FlyHigh**, asegurando una arquitectura bien definida para su desarrollo futuro. 🚀
