Enrique Navarro Abellán

He desarrollado el proyecto en las siguientes clases:
	- MainP11.java
	- Usuario.java
	- Log.java
	- BaseDatos.java
	- Pintar.java

- MainP11.java
	Hace las veces del "controlador". En ella se ejecutan las funciones más generales y se visualiza el flujo del programa.

- Usuario.java
	A parte de los atributos 'nombre' y 'pass' he incluido 'intentos' y un booleano 'bloqueado'.
	El primero es un 'int' que mantiene en memoria los intentos restantes de cada usuario. El segundo es para distinguir usuarios bloqueados de no bloqueados.

- Log.java
	Contiene una única función que registra en un '.txt' todos los acceso correctos y los incorrectos.  

- Pintar.java
	La he utilizado como un modelo "vista", para alojar las siete funciones que únicamente se encargan de mostrar o recoger algo al usuario. Algunas de ellas devuelven una respuesta del usuario.

- BaseDatos.java
	- Se crea en el main para albergar las rutas de los ficheros utilizados. A parte de un constructor y los setters/getters, contiene las 10 funciones principales del programa y que manipulan directamente la base de datos.


ASPECTOS DESTACADOS:
	- En este enunciado del proyecto especifica que "si el usuario existe pero la contraseña no coincide, se pedirá de nuevo la clave", pero si, como ampliación, se permitiera intentar logarse con otro nombre los intentos erróneos del usuario anterior no se resetearían hasta logarse correctamente en algún momento de la ejecución o en otra ejecución posterior.

	- En el 'Área del Administrador' se permite bloquear/desbloquear a uno o todos los usuarios desbloqueados/bloqueados. El 'admin' es el único usuario que nunca podrá bloquearse de ninguna manera.

	- Para distinguir entre los controles de acceso (en el fichero 'acceso.txt con formato nombre:pass) y los de bloqueo he creado otro fichero 'historial_usuarios.txt', en él se almacena los datos de usuarios en el siguiente formato String_nombre:int_intentos:boolean_bloqueado.
En el registro de usuario se manipulan los dos archivos escribiendo en ellos; en el login solo se consulta el de 'acceso.txt' para hacer las validaciones; en las comprobaciones de bloqueo, de intentos restantes, búsquedas de usuarios,... se consulta y se manipula el fichero 'historial_usuario.txt'. De esta forma se reserva el tratamiento de las contraseñas a solo login.


MÉTODO DE BLOQUEO:
Tiene 3 opciones:
	1. Mostrar Usuarios
	2. Bloquear Usuarios
	3. Desbloquear Usuarios

1. Mostrar Usuarios
	Muestra listado de todos los usuarios:
		USUARIO: nombre		INTENTOS: intentos	BLOQUEADO: true/false

2. Bloquear Usuarios
	No estaba en el enunciado pero lo tengo implementado porque funciona igual que el siguiente punto.

3. Desbloquear Usuarios
	- Muestra listado con los usuarios desbloqueados/bloqueados
	- Pregunta si se quiere bloquear/desbloquear a alguien
	- Ofrece la opción de hacerlo en un usuario o en todos.
	- Si es a todos cambia el estado bloqueado de todo el listado. Si es a un usuario pide su nombre, y ese nombre tiene que exixtir en la base de datos y estar en el estado correcto, de no ser así se notifica por pantalla y se vueve al menú del ÁreaAdministrador. No podrá manipularse el estado admin ni bloquearse de ningún modo.
	- Solo se podrá salir de este área con la opción '4.Volver'.






 