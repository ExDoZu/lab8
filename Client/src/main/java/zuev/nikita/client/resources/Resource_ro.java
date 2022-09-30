package zuev.nikita.client.resources;

import java.util.ListResourceBundle;

public class Resource_ro extends ListResourceBundle {

    private static final Object[][] contents = {
            {"login", "Autentificare"},
            {"password", "Parola"},
            {"table", "Masa"},
            {"key", "Cheie"},
            {"signIn", "A intra"},
            {"signUp", "Inregistreaza-te"},
            {"authorization", "Autorizare"},
            {"languageChanging", "Schimbați limba"},
            {"author", "Autor"},
            {"name", "Nume"},
            {"coordinates", "Coordonatele"},
            {"creationDate", "data creării"},
            {"annualTurnover", "Cifră de afaceri anuală"},
            {"address", "Abordare"},
            {"organizationType", "Tipul organizației"},
            {"TRUST", "Încredere"},
            {"PUBLIC", "public"},
            {"COMMERCIAL", "o reclamă"},
            {"show", "Spectacol"},
            {"script", "Scenariul"},
            {"serverNotAvailable", "Serverul nu este disponibil"},
            {"wrongLoginOrPassword", "Parola sau username gresit"},
            {"userAlreadyExists", "Acest utilizator există deja"},
            {"notChooseElement", "Element neselectat"},
            {"notYourElement", "Acesta nu este articolul dvs."},
            {"adding", "Addendum"},
            {"save", "Salvați"},
            {"newElement", "Element nou"},
            {"integer", "Întreg"},
            {"float", "Fracționat"},
            {"insert", "Adăuga"},
            {"edit", "Schimbare"},
            {"remove", "Șterge"},
            {"clear", "clar"},
            {"removeLower", "Eliminați elementul mai puțin decât este indicat"},
            {"removeGreaterKey", "Eliminați elementele cu o cheie mai mare"},
            {"filterGTPA", "Filtrați după adrese mai mari decât cele date"},
            {"sortAsc", "Sort asc ro"},
            {"sortDesc", "Sort desc ro"},
            {"emptyFields", "Fields are empty ro"}
    };
    @Override
    protected Object[][] getContents() {
        return contents;
    }
}