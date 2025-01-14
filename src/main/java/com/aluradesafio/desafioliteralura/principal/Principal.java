package com.aluradesafio.desafioliteralura.principal;

import com.aluradesafio.desafioliteralura.dto.Datos;
import com.aluradesafio.desafioliteralura.dto.DatosAutor;
import com.aluradesafio.desafioliteralura.dto.DatosLibros;
import com.aluradesafio.desafioliteralura.modelos.*;
import com.aluradesafio.desafioliteralura.repository.AutorRepository;
import com.aluradesafio.desafioliteralura.repository.LibroRepository;
import com.aluradesafio.desafioliteralura.service.ConsumoApi;
import com.aluradesafio.desafioliteralura.service.ConvierteDatos;
import org.hibernate.engine.jdbc.Size;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


    public void menuOpciones() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                        ****Bienvenidos a LiterAlura*****
                            ****** Menu ******
                    1 - Buscar libro por titulo
                    2 - Lista de todos los libros
                    3 - Lista de los autores
                    4 - Lista autores vivos en deteminado año
                    5 - Lista libros por idioma
                    6 - Buscar autor por Nombre
                    
                    0 - Salir
                    
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listaLibrosGuardados();
                    break;
                case 3:
                    listaDeAutoresRegistrados();
                    break;
                case 4:
                    listaAutoresVivosPorFecha();
                    break;
                case 5:
                    filtrarLibrosPorIdioma();
                    break;
                case 6:
                    buscarAutorPorNombre();
                    break;
                case 0:
                    System.out.println("Finalizando la aplicación....");
                    break;
                default:
                    System.out.println("Opción no es valida, escoje una entre el menu");
            }
        }
    }
    private Datos getDatos(){
        System.out.println("Escribe el titulo del libro que desea buscar...");
        var tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "%20"));
        Datos datosBuscados = conversor.obtenerDatos(json, Datos.class);
        System.out.println(json);
        return datosBuscados;
    }

    private void buscarLibroPorTitulo(){
        Datos datos = getDatos();

            if (datos.resultados().isEmpty()){
                System.out.println("No se encontro libro con el titulo que proporcionaste.");
                return;
            }

            var datosLibro = datos.resultados().get(0);
            String tituloLibro = datosLibro.titulo();
                if (libroRepository.existsByTitulo(tituloLibro)){
                    System.out.println("El libro con el titulo " + tituloLibro + " ya existe en la base del sistema.");
                    return;
                }
                Libro libro = new Libro(datosLibro);
                System.out.println("Libro encontrado " + tituloLibro);
                List<Autor> autores = datosLibro.autor().stream()
                        .map(datosAutor -> {
                            Autor autor = new Autor(datosAutor);
                            autor.setLibros(libro);
                            return autor;
                        })
                        .toList();
                libro.setDatosAutor(autores);
                libroRepository.save(libro);
        System.out.printf("""
                ----------Libro Guardado----------
                Titulo: %s
                Autor: %s
                Idioma: %s
                Descargas: %s
                ----------------
                """,
                tituloLibro,
                datosLibro.autor().stream().map(a-> a.nombre()).collect(Collectors.joining(", ")),
                datosLibro.idioma().get(0),
                datosLibro.descargas());
    }

    private void listaLibrosGuardados(){
        List<Libro> libros = libroRepository.findAll();
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(l-> System.out.printf("""
                    ------LIBRO------
                Titulo: %s
                Autor: %s
                Idioma: %s
                Descargas: %s
                -----------------
                """,
                        l.getTitulo(),
                        l.getDatosAutor().stream().map(autor -> autor.getNombre()).collect(Collectors.joining(". ")),
                        l.getIdiomas(),
                        l.getDescargas()));
    }

    private void listaDeAutoresRegistrados(){
        List<Autor> listaAutores = autorRepository.findAll();
        listaAutores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(a -> System.out.printf("""
                        ******* Autor *******
                        Autor:  %s
                        Fecha de nacimiento: %s
                        Fecha de fallecimiento:  %s
                        *****************************
                        """,
                        a.getNombre(),
                        a.getFechaDeNacimiento(),
                        a.getFechaDeMuerte()));
    }

    private void listaAutoresVivosPorFecha(){

        System.out.println("Ingrese el año:");
        if(teclado.hasNext()) {
            var fecha = teclado.nextInt();
            System.out.println("Año ingresado: " + fecha);

            List<Autor> autor = autorRepository.fechaDeNacimientoyMuerte(fecha);
            if (autor.isEmpty()){
                System.out.println("Autor no encontrado");
            } else {
                autor.stream()
                        .forEach(a -> System.out.printf("""
                                        //////// Autor \\\\\\\\\\\\
                                        
                                        Autor:  %s
                                        Fecha de nacimiento: %s
                                        Fecha de fallecimiento:  %s
                                        Libros: %s
                                        """,
                                a.getNombre(),
                                a.getFechaDeNacimiento(),
                                a.getFechaDeMuerte(),
                                a.getLibros().getTitulo()));
            }
        }
    }

    private void buscarLibrosPorIdioma(Idiomas idioma, String lenguaje) {
    List<Libro> idomaDeLoslibros = libroRepository.findByIdiomas(Idiomas.valueOf(idioma.name()));
    if (idomaDeLoslibros.isEmpty()) {
        System.out.println("No se encontraron libros por el idioma: " + lenguaje);
    } else {
        System.out.println("Libros del idioma " + lenguaje);
        System.out.println("Libro registrados en el mismo idioma: " + idomaDeLoslibros.stream().count());
        idomaDeLoslibros.forEach(l -> System.out.printf("""
                ::::::::::: Libro :::::::::::
                Titulo: %s
                Autor: %s
                Idioma: %s
                Descargas: %s
                ---------------------------
                """,
                l.getTitulo(),
                l.getDatosAutor().stream().map(a -> a.getNombre()).collect(Collectors.joining(",")),
                l.getIdiomas(),
                l.getDescargas()));
        }
    }

    private void filtrarLibrosPorIdioma() {
        var opcion = -1;
        while (opcion != 0)
        {
            var menu = """
                        Selecciona el idioma del libro que deseas buscar:
                        1. Español
                        2. Inglés
                        3. Italiano
                        4. Francés
                        5. Portugués
                    
                        0. Salir
                    """;

            System.out.println(menu);
            if (teclado.hasNextInt()) {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1 -> buscarLibrosPorIdioma(Idiomas.ESPAÑOL, "Español");
                    case 2 -> buscarLibrosPorIdioma(Idiomas.INGLES, "Inglés");
                    case 3 -> buscarLibrosPorIdioma(Idiomas.ITALIANO, "Italiano");
                    case 4 -> buscarLibrosPorIdioma(Idiomas.FRANCES, "Francés");
                    case 5 -> buscarLibrosPorIdioma(Idiomas.PORTUGUES, "Portugués");
                    case 0 -> System.out.println("Saliendo del filtro de idiomas....");
                    default -> System.out.println("Opncion no valida, Por favor elije opcion dentro del menu");
                }
            } else {
                System.out.println("Entrada invalida. Por favor ingrese un numero...");
                teclado.nextLine();
            }
        }
    }

    private void buscarAutorPorNombre(){
        System.out.println("Escribe el Nombre del autor que deseas buscar:");
        var nombreAutor = teclado.nextLine();
        Optional<Autor> autor = autorRepository.bucarAutorPorNombre(nombreAutor);

        if (autor.isPresent()){
            Autor a = autor.get();
            System.out.printf("""
                    Autor encontrado es:
                    [[[[[[[[[[ Autor ]]]]]]]]]]
                    Autor: %s
                    Fecha de Nacimiento: %s
                    Libros: %s
                    
                   =======================
                    """,
                    a.getNombre(),
                    a.getFechaDeNacimiento(),
                    a.getLibros().getTitulo());
        } else {
            System.out.printf("No tenemos registro del autor: " + nombreAutor );
        }

    }

}











