# GarbanzoSpots

> **Proyecto Integrador - Desarrollo de Aplicaciones Móviles**
>
> **Cuatrimestre:** 4°A
> **Fecha de entrega:** 11 de Diciembre

---

## Equipo de Desarrollo

| Nombre Completo | Rol / Tareas Principales | Usuario GitHub |
| :--- | :--- | :--- |
| Santiago Acosta Sotelo | UI Design, Lógica, Navegación | @woood3d |
| Dana Bahena Díaz | UI Design, Retrofit, Pantallas | @lum-i3 |
| Ian Alejandro Rivera Torres | Sensores, Repositorio, Servidor | @IanRT08 |

---

## Descripción del Proyecto

**¿Qué hace la aplicación?**
Nuestra aplicación atiende el deseo de muchos usuarios de descubrir y compartir lugares únicos, divertidos e interesantes cerca de ellos. Funciona como una red social geolocalizada donde los usuarios pueden:

Registrar ubicaciones especiales que han visitado, capturando sus coordenadas GPS exactas
Compartir experiencias visuales mediante galerías de fotos de cada lugar
Explorar un feed público con los lugares que otros usuarios han compartido
Visualizar en un mapa interactivo todos los lugares registrados, diferenciando entre lugares propios (públicos y privados) y de otros usuarios
Gestionar su perfil personal con estadísticas de publicaciones y visitas
Controlar la privacidad de sus publicaciones, decidiendo qué compartir públicamente

La app combina funcionalidades de red social, geolocalización y galería fotográfica para crear una comunidad de exploradores urbanos que comparten sus descubrimientos.

**Objetivo:**
Demostrar la implementación de una arquitectura robusta en Android utilizando servicios web y hardware del dispositivo.

---

## Stack Tecnológico y Características

Este proyecto ha sido desarrollado siguiendo estrictamente los lineamientos de la materia:

* **Lenguaje:** Kotlin 100%.
* **Interfaz de Usuario:** Jetpack Compose.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Conectividad (API REST):** Retrofit.
    * **GET:**
      * Obtiene el feed de publicaciones públicas de todos los usuarios
      * Recupera las publicaciones de un usuario específico (para perfil y "Mis Lugares")
      * Consulta el ranking de lugares más visitados
      * Obtiene los datos del perfil del usuario (nombre, correo, estadísticas)
    * **POST:**
      * Crea nuevas publicaciones con título, descripción, coordenadas GPS, fecha de visita, dirección y privacidad
      * Sube múltiples imágenes mediante Multipart/Form-Data asociadas a cada publicación
      * Registra nuevos usuarios en el sistema (nombre, apellidos, correo, contraseña)
      * Realiza autenticación de usuarios (login) y obtiene token JWT
      * Incrementa el contador de visitas cuando un usuario interactúa con una publicación
    * **UPDATE:**
       * Actualiza los datos de una publicación existente: título, descripción, fecha de visita, dirección y configuración de privacidad
       * Modifica la información del perfil del usuario autenticado
       * Nota: Las coordenadas GPS y las imágenes no son editables por diseño, ya que representan el registro original del lugar 
    * **DELETE:**
       * Elimina publicaciones propias del usuario autenticado
       * Borra las imágenes asociadas del servidor al eliminar una publicación
       * Actualiza automáticamente los contadores de estadísticas del usuario (total de publicaciones)
* **Sensor Integrado:** Sensor GPS del dispositivo movil 
    * *Uso:* Se usa para capturar la ubicación de los lugares que se desean publicar, toma todos los valores del GPS y muestra la ubicación registrada en la pantalla de Mapa
---

## Capturas de Pantalla

A continuación se muestran capturas de pantalla de la app en funcionamiento

| Pantalla de Inicio | Publicaciones | Uso del Sensor | Pantalla de Mapa | Perfil | 
| :---: | :---: | :---: | :---: | :---: |
| ![loginSilverGarbanzo](https://github.com/user-attachments/assets/3a2d6c46-42f4-4fe2-bf8f-c6c6d4e117bc) | ![publicacionesSilverGarbanzo](https://github.com/user-attachments/assets/f6ae9ab4-a162-46b4-96d6-7926e49736fc) | ![usoSensorSilverGarbanzo](https://github.com/user-attachments/assets/d234325b-e4ce-4ee1-b0c5-412344dc4365) | ![mapaSilverGarbanzo](https://github.com/user-attachments/assets/a4aa5565-6df8-4331-b826-fe64a91a01cb) | ![perfilSilverGarbanzo](https://github.com/user-attachments/assets/e7ec77c0-6c7e-434a-838c-27f1d8fb4659) |

---

## Instalación y Releases

El ejecutable firmado (.apk) se encuentra disponible en la sección de **Releases** de este repositorio.

[Liga correctamente tu link de releases en la siguiente sección]

1.  Ve a la sección "Releases" (o haz clic [aquí](link_a_tus_releases)).
2.  Descarga el archivo `.apk` de la última versión.
3.  Instálalo en tu dispositivo Android (asegúrate de permitir la instalación de orígenes desconocidos).
