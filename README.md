App Veterinaria
Este proyecto es una aplicación móvil (Android) que se conecta a un servicio web desarrollado con Node.js y Express para gestionar información desde una base de datos MySQL.

Para ejecutar este proyecto, necesitas tener instalados los siguientes componentes:

Node.js: Para ejecutar el servidor de la API.
MySQL: La base de datos que almacena la información de las mascotas.
Android Studio: Para compilar y ejecutar la aplicación móvil.

Configuración de la Aplicación Android

Abre el proyecto en Android Studio.
Asegúrate de que la aplicación tenga los permisos de Internet necesarios en el archivo AndroidManifest.xml.
En la clase de tu aplicación que se encarga de la conexión (por ejemplo, Buscar.java), cambia la URL base a la dirección IP de tu computadora (si estás en una red local).


Funcionalidades
La aplicación cuenta con las siguientes funcionalidades principales:
Listar: Muestra todos los registros de la tabla mascotas.
Buscar: Permite buscar una mascota por su ID.
Actualizar: Permite modificar los datos de una mascota existente.
Eliminar: Permite borrar una mascota de la base de datos.
