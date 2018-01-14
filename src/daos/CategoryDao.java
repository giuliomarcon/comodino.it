package daos;

import main.Category;

import java.io.Serializable;
import java.util.ArrayList;

public interface CategoryDao extends Serializable {
    /**
     * Funzione che recupera la lista categorie
     * @return ArrayList contenente la lista di categorie
     */
    ArrayList<Category> getCategories();
}
