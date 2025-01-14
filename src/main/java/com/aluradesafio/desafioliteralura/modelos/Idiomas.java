package com.aluradesafio.desafioliteralura.modelos;

public enum Idiomas {
        ESPAÑOL("es", "Español"),
        INGLES("en", "Inglés"),
        ITALIANO("it", "Italiano"),
        FRANCES("fr", "Francés"),
        PORTUGUES("pt", "Portugués");

        private String idioma;
        private String tipoDeIdioma;

        Idiomas(String idioma, String tipoDeIdioma){
        this.idioma = idioma;
        this.tipoDeIdioma = tipoDeIdioma;
    }

        public static Idiomas fromString(String text) {
        for (Idiomas idiomas : Idiomas.values()) {
            if (idiomas.idioma.equalsIgnoreCase(text.trim())){
                return idiomas;
            }
        }
        throw  new IllegalArgumentException("Ningun idioma encontrado " + text);
    }

    public static Idiomas fromTipodeIdioma(String text) {
            for (Idiomas idiomas : Idiomas.values()) {
                if (idiomas.tipoDeIdioma.equalsIgnoreCase(text.trim())){
                    return idiomas;
                }
            }
            throw new IllegalArgumentException("Ningun idioma encontrado" + text);
    }
}
