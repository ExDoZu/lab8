package zuev.nikita.client.resources;

import java.util.ListResourceBundle;

public class Resource_fr extends ListResourceBundle {

    private static final Object[][] contents = {
            {"login", "Connexion"},
            {"password", "Mot de passe"},
            {"table", "Table"},
            {"key", "Clé"},
            {"signIn", "Entrer"},
            {"signUp", "S'inscrire"},
            {"authorization", "Autorisation"},
            {"languageChanging", "Changer de langue"},
            {"author", "Auteur"},
            {"name", "Nom"},
            {"coordinates", "Coordonnées"},
            {"creationDate", "date de création"},
            {"annualTurnover", "Chiffre d'affaires annuel"},
            {"address", "Adresse"},
            {"organizationType", "Type d'Organisation"},
            {"TRUST", "Confiance"},
            {"PUBLIC", "Publique"},
            {"COMMERCIAL", "un commercial"},
            {"show", "Spectacle"},
            {"script", "Scénario"},
            {"serverNotAvailable", "Le serveur n'est pas disponible"},
            {"wrongLoginOrPassword", "Identifiant ou mot de passe incorrect"},
            {"userAlreadyExists", "Cet utilisateur existe déjà"},
            {"notChooseElement", "Article non sélectionné"},
            {"notYourElement", "Ce n'est pas votre article."},
            {"adding", "Addenda"},
            {"save", "sauvegarder"},
            {"newElement", "Nouvel élément"},
            {"integer", "Ensemble"},
            {"float", "Fractionnaire"},
            {"insert", "Ajouter"},
            {"edit", "Changer"},
            {"remove", "Effacer"},
            {"clear", "Dégager"},
            {"removeLower", "Supprimer l'élément inférieur à celui indiqué"},
            {"removeGreaterKey", "Supprimer les éléments avec une clé plus grande"},
            {"filterGTPA", "Filtrer par adresses supérieures à celles données"},
            {"sortAsc", "Sort asc fr"},
            {"sortDesc", "Sort desc fr"},
            {"emptyFields", "Fields are empty fr"}
    };
    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
