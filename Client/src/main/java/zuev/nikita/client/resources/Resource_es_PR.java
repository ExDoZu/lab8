package zuev.nikita.client.resources;

import java.util.ListResourceBundle;

public class Resource_es_PR extends ListResourceBundle {

    private static final Object[][] contents = {
            {"login", "Acceso"},
            {"password", "Clave"},
            {"table", "Mesa"},
            {"key", "Llave"},
            {"signIn", "Entrar"},
            {"signUp", "Registro"},
            {"authorization", "Autorización"},
            {"languageChanging", "Cambiar idioma"},
            {"author", "Autor"},
            {"name", "Nombre"},
            {"coordinates", "Coordenadas"},
            {"creationDate", "fecha de creación"},
            {"annualTurnover", "Volumen de negocios anual"},
            {"address", "Dirección"},
            {"organizationType", "Tipo de organización"},
            {"TRUST", "Confianza"},
            {"PUBLIC", "público"},
            {"COMMERCIAL", "un comercial"},
            {"show", "Espectáculo"},
            {"script", "Guion"},
            {"serverNotAvailable", "El servidor no esta disponible"},
            {"wrongLoginOrPassword", "Usuario o contraseña incorrectos"},
            {"userAlreadyExists", "Este usuario ya existe"},
            {"notChooseElement", "Elemento no seleccionado"},
            {"notYourElement", "Este no es tu artículo."},
            {"adding", "Apéndice"},
            {"save", "Ahorrar"},
            {"newElement", "Nuevo elemento"},
            {"integer", "Entero"},
            {"float", "Fraccionario"},
            {"insert", "Agregar"},
            {"edit", "Cambio"},
            {"remove", "Borrar"},
            {"clear", "Claro"},
            {"removeLower", "Quitar elemento menos que dado"},
            {"removeGreaterKey", "Eliminar elementos con una clave más grande"},
            {"filterGTPA", "Filtrar por direcciones mayores a las dadas"},
            {"sortAsc", "Sort asc es"},
            {"sortDesc", "Sort desc es"},
            {"emptyFields", "Fields are empty es"}

    };
    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
