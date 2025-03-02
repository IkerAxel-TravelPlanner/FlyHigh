# 🛠 Contribuir al Proyecto Travel Planner

¡Gracias por tu interés en contribuir a **Travel Planner**! 🎉  
Este documento explica cómo puedes colaborar con el desarrollo de la aplicación.

---

## 📌 Requisitos Técnicos

Antes de empezar, asegúrate de cumplir con los siguientes requisitos:

- **Lenguaje:** Kotlin (para la lógica y UI)
- **Arquitectura:** MVVM + Modular (NavGraph.kt ya implementado)
- **Android Studio:** Versión más reciente recomendada
- **Dependencias:** Se gestionan con Gradle
- **Base de Datos:** Room (si se implementa en el futuro)
- **Versionado:** Git + GitHub

---

## 🚀 Cómo Contribuir

### 1⃣ Configurar el Proyecto
1. **Fork** este repositorio en GitHub.
2. Clona tu fork en tu máquina local:
   ```sh
   git clone https://github.com/tu-usuario/travel-planner.git
   ```
3. Abre el proyecto en **Android Studio**.
4. Asegúrate de instalar todas las dependencias necesarias con:
   ```sh
   ./gradlew build
   ```

### 2⃣ Crear una Nueva Funcionalidad o Solucionar un Bug
1. Crea una rama para tu cambio:
   ```sh
   git checkout -b feature-nombre-funcionalidad
   ```
2. Implementa los cambios siguiendo la arquitectura **MVVM**.
3. Realiza pruebas para asegurar que todo funciona correctamente.
4. **Confirma** los cambios:
   ```sh
   git commit -m "Agrega [descripción de la funcionalidad]"
   ```
5. **Sube** tu rama al repositorio:
   ```sh
   git push origin feature-nombre-funcionalidad
   ```
6. Abre un **Pull Request** en GitHub explicando qué hiciste y por qué.

---

## 🐞 Reportar Errores
Si encuentras un error, abre un **Issue** con:
- Una descripción clara del problema.
- Pasos para reproducirlo.
- Si es posible, logs o capturas de pantalla.

---

## 💡 Sugerencias y Mejoras
Si tienes ideas para mejorar la aplicación, abre un **Issue** con la etiqueta `enhancement` y explica tu propuesta.

---

## 💜 Estándares de Código
- Usa **nombres descriptivos** para variables y funciones.
- Sigue la guía de estilo oficial de **Kotlin para Android**.
- Documenta tu código con **KDoc** si es necesario.
- Formatea el código antes de subirlo:
  ```sh
  ./gradlew ktlintFormat
  ```

---

## ✅ Revisión y Aprobación
1. Tu PR será revisado por los mantenedores.
2. Si todo está bien, se aprobará y se hará **merge**.
3. Si se necesitan cambios, recibirás comentarios en el PR.

---

🎉 ¡Gracias por contribuir a **Travel Planner**!    