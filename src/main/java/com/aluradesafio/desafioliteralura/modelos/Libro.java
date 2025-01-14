package com.aluradesafio.desafioliteralura.modelos;

import com.aluradesafio.desafioliteralura.dto.DatosLibros;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private Double descargas;
    @OneToMany(mappedBy = "libros", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Autor> datosAutor;
    @Enumerated(EnumType.STRING)
    private Idiomas idiomas;

    public Libro(){}



    public Libro(DatosLibros datosLibros){
        this.titulo = datosLibros.titulo();
        this.descargas = datosLibros.descargas();
        this.datosAutor = datosLibros.autor().stream()
                .map(d-> new Autor(d))
                .collect(Collectors.toList());
        this.idiomas = Idiomas.fromString(datosLibros.idioma().get(0).trim());
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getDescargas() {
        return descargas;
    }

    public void setDescargas(Double descargas) {
        this.descargas = descargas;
    }

    public List<Autor> getDatosAutor() {
        return datosAutor;
    }

    public void setDatosAutor(List<Autor> datosAutor) {
        this.datosAutor = datosAutor;
    }

    public Idiomas getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Idiomas idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        return " ******Libro******" +
                ", Titulo='" + titulo + '\'' +
                ", Descargas=" + descargas +
                ", Autor=" + datosAutor +
                ", Idioma=" + idiomas +
                '}';
    }
}
