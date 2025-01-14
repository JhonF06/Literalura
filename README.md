Challenge LiterAlura

Programa de consola, que permite interactuar al usuario y realizar busqueda de libro conectado con la Api de Gutendex  que cuenta con una base de datos de libros extensa 

FUNCIONALIDADES

1. Buscar libro por titulo
   -Tiene la funcion de conectar directamente con la Api e interatuar con el usuario solicitandole el titulo de un libro  para iniciar su busqueda
   -tambien tiene funcion de guardar la informacion en una base de datos con PostgreSQL, creando 2 tablas independientes relacionadas en Libro y actores para
    funciones que se realizaran en el avance de la app

2. Lista de libros guardados
   = Funcion de mostrar los libros que se han guardado en la base de datos, mostrando informacion como Titulo, autor, idioma y total de descargas

3. Lista de autores registrados
   - Funcion de mostrar los datos de los autores que estan guardados en la base de datos

4. Lista de autores vivos por año
   - Primera funcion es interactuar con el usuario solicitandoles un año para inicar su busqueda
   - funcion que realiza la compracion en el año brindado por el usuario y filtrar lo solicitado con la base de datos

5. Filtrar libros por idioma
   - Permite al usuario realizar un filtro por idioma donde arroja el menu.
     Selecciona el idioma del libro que deseas buscar:
                        1. Español
                        2. Inglés
                        3. Italiano
                        4. Francés
                        5. Portugués
   - Una vez el usuario escoja el idioma, su funcion es realizar el filtro en la base de datos y mostrar los resultado con el idioma escojido

6. Buscar autor por nombre
   - Funcion que permite realizar la busqueda de libros guardados en la base de datos solo por el nombre del autor
            
