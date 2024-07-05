package com.ccettour.literalura.principal;

import com.ccettour.literalura.model.*;
import com.ccettour.literalura.repository.LiteraluraRepository;
import com.ccettour.literalura.service.ConsumirAPI;
import com.ccettour.literalura.service.ConvertirDatos;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner sc = new Scanner(System.in);
    private ConsumirAPI consumirAPI = new ConsumirAPI();
    private ConvertirDatos conversor = new ConvertirDatos();
    private List<LibroData> libroData = new ArrayList<>();
    private LiteraluraRepository repositorio;

    List<LibroData> libros;
    List<Autor> autores;

    public Principal(LiteraluraRepository repository) {
        this.repositorio = repository;
    }


    public void showMenu(){
        var opcion = -1;
        while(opcion!=0){
            var menu = """
                    \n********** MENU **********
                    1 - Consultar libro
                    2 - Listar libros consultados
                    3 - Listar autores
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                    6 - Listar top 10 libros más descargados
                    7 - Ver estadísticas
                    
                    0 - Salir
                    """;

            try{
                System.out.println(menu);
                opcion = sc.nextInt();

            }catch (InputMismatchException e){
                System.out.println("Ingrese solamente el número correspondiente a la opción deseada\n");
            }
            sc.nextLine();


            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibrosBuscados();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresPorAnio();
                    break;
                case 5:
                    listarPorIdioma();
                    break;
                case 6:
                    top10Descargas();
                    break;
                case 7:
                    verEstadisticas();
                    break;
                case 0:
                    System.out.println("Gracias por utilizar la aplicación!");
                    break;
                default:
                    System.out.println("Opción inválida.\n");
                    break;
            }
        }
    }

    private List<LibroData> getLibros(String url){
        var json = consumirAPI.obtenerDatos(url);
        var data = conversor.obtenerDatos(json, ApiData.class);
        return data.resultado();
    }

    private void buscarLibro() {
        System.out.println("Ingrese el nombre del libro");
        var titulo = sc.nextLine();

        libros = getLibros(URL_BASE+"?search="+titulo.replace(" ","+"));
        Optional<LibroData> libroBuscado = libros.stream()
                .filter(l -> l.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .findFirst();

        //Evalúa si se encontró algún resultado en la API
        if(libroBuscado.isPresent()){

            Libro libro = libroBuscado.stream().map(l -> new Libro(l)).findFirst().get();

            var libroEnDB = repositorio.libroPorTitulo(libroBuscado.get().titulo());

            //Evalúa si el libro ya está registrado en la base de datos
            if(libroEnDB.isPresent()){
                System.out.println("El libro ya existe en la base de datos");

            }else{

                Autor autorLibroBuscado;

                //Si el libro no tiene autor lo establece como desconocido, sino toma el primer autor.
                if(libroBuscado.get().autores().isEmpty()){
                    autorLibroBuscado = new Autor("Desconocido",null,null);
                } else {
                    autorLibroBuscado = libroBuscado.get().autores().stream().map(Autor::new).findFirst().get();
                }

                Autor autor;
                var autorEnDB = repositorio.findByNombreContainsIgnoreCase(autorLibroBuscado.getNombre());

                //Si encuentra el autor en la base de datos toma ese, sino le asigna el nuevo
                if(autorEnDB.isPresent()){
                    autor=autorEnDB.get();
                } else{
                    autor = autorLibroBuscado;
                }

                autor.setLibros(libro);

                repositorio.save(autor);

                System.out.println("Libro encontrado:");
                System.out.println(libro);
                }

        } else {
            System.out.println("No se encontró ningún libro con ese título");
        }
    }

    private void listarLibrosBuscados(){
        List<Libro> libros = repositorio.listarLibros();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutores() {
        autores = repositorio.findAll();

        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void listarAutoresPorAnio() {

        try{
            System.out.println("Ingrese un año para ver los autores que estaban vivos");
            int anio = sc.nextInt();
            sc.nextLine();

            if(anio> LocalDate.now().getYear()){
                System.out.println("Ingrese un año válido");
            } else {
                autores = repositorio.listarAutoresVivosEnAnio(anio);

                if(autores.isEmpty()){
                    System.out.println("No tenemos registrados autores que estuvieran vivos en el año "+anio);
                } else {
                    autores.stream()
                            .sorted(Comparator.comparing(Autor::getAnioNacimiento))
                            .forEach(System.out::println);
                }
            }

        } catch (InputMismatchException e){
            System.out.println("Ingrese un año válido");
        }

    }

    private void listarPorIdioma() {
        var menu = """
                Ingrese la opción correspondiente al idioma por el que desea buscar:
                en - INGLÉS
                es - ESPAÑOL
                it - ITALIANO
                pt - PORTUGUÉS
                fr - FRANCÉS
                """;
        System.out.println(menu);
        try{
            var sigla = sc.nextLine();
            var idioma = Idioma.fromSigla(sigla);
            List<Libro> libros = repositorio.librosPorIdioma(idioma);

            if(libros.isEmpty()){
                System.out.println("Aún no tenemos registrado ningún libro con ese idioma");
            } else {
                var cantidad = repositorio.cantidadLibrosPorIdioma(idioma);
                System.out.println("Cantidad de libros registrados en el idioma " + idioma +": " + cantidad);
                libros.stream()
                        .sorted(Comparator.comparing(Libro::getTitulo))
                        .forEach(System.out::println);
            }

        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            System.out.println("Ingrese una opción válida. Por ejemplo: fr");
        }
    }

    private void top10Descargas() {
        System.out.println("Top 10 libros más descargados");
        libros = getLibros(URL_BASE);
        libros.stream()
            .sorted(Comparator.comparing(LibroData::cantidadDeDescargas).reversed())
            .limit(10)
            .map(l -> l.titulo().toUpperCase())
            .forEach(System.out::println);
    }

    private void verEstadisticas() {
        libros = getLibros(URL_BASE);
        DoubleSummaryStatistics estadisticas = libros.stream()
            .filter(d -> d.cantidadDeDescargas()>0)
            .collect(Collectors.summarizingDouble(LibroData::cantidadDeDescargas));
        System.out.println("Cantidad media de descargas: " + estadisticas.getAverage());
        System.out.println("Cantidad maxima de descargas: " + estadisticas.getMax());
        System.out.println("Cantidad minima de descargas: " + estadisticas.getMin());
        System.out.println("Cantidad de registros evaluados para obtener estos datos: " + estadisticas.getCount());
    }
}