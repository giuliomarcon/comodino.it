package daos;

import main.Product;
import main.ProductGroup;

import javax.servlet.http.Part;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProductDao extends Serializable {
    /**
     *  Restituisce un prodotto specifico di un negozio in particolare
     *
     * @param productID ID di tipo 'int' del prodotto
     * @param shopID ID di tipo 'int' dello shop che vende il prodotto
     * @return Oggetto 'Product' trovato nel database, 'null' se non esiste
     */
    Product getProduct(int productID, int shopID);

    /**
     * Ottiene tutti i prodotti di tipo 'shopproduct' dal database e li istanza in oggetti 'Product'
     * e li aggiunge ad un array.
     *
     * @return Array contenente i prodotti di tipo 'Product'
     */
    ArrayList<Product> allProducts();

    /**
     * Ottiene la lista dei prodotti dal DB in base ai parametri, organizzati in gruppi di prodotti dallo stesso nome
     *
     * @param params Mappa contenente tutti i parametri ottenuti precedentemente dal GET
     * @return Mappa contenente i singoli shopProduct raggruppati come product (ProductGroup in java)
     * @throws SQLException nel caso qualcosa non andasse come previsto nel database
     */
    List<Map.Entry<String, ProductGroup>> getProducts(Map params) throws SQLException;

    /**
     * Controlla la disponibilità di una certa quantità di uno specifico prodotto, venduto da un negozio in particolare
     *
     * @param productID ID di tipo 'int' del prodotto
     * @param shopID ID di tipo 'int' dello shop che vende il prodotto
     * @param quantity Quantità 'int' del prodotto specificato
     * @return 'true' se c'è disponibilità, 'false' altrimenti
     */
    boolean checkAvailability(int productID, int shopID, int quantity);

    /**
     * Aggiorna il database riducendo di una determinata quantità la disponibilità di uno specifico prodotto,
     * venduto da un negozio in particolare
     *
     * @param productID ID di tipo 'int' del prodotto
     * @param shopID ID di tipo 'int' dello shop che vende il prodotto
     * @param quantity Quantità 'int' del prodotto specificato
     * @return 'true' se è stato possibile ridurre la quantità di prodotto, 'false' altrimenti
     */
    boolean reduceAvailability(int productID, int shopID, Integer quantity);

    /**
     * Aggiorna la valutazione globale (indipendentemente dal venditore) di un prodotto
     * @param productID ID di tipo 'int' del prodotto
     * @return 'true' se l'operazione è andata a buon, 'false' altrimenti
     */
    boolean updateProductRating(int productID);

    /**
     * Ottiene tutte le immagini di un prodotto
     *
     * @param productID ID di tipo 'int' del prodotto
     * @return Array di stringhe che contengono le varie immagini in formato base64
     */
    ArrayList<String> getImages(int productID);

    /**
     * Predispone i dati per il menù a tendina, utile per le funzionalità dell'autocompletamento della ricerca
     *
     * @param term Stringa che definisce il termine di ricerca
     * @return Stringa in formato JSON che contiene i dati
     */
    String getAutocompleteProducts(String term);

    /**
     * Controlla se esiste un prodotto con il nome specificato
     *
     * @param shopID ID di tipo 'int' dello shop che vende il prodotto
     * @param productName Stringa che definisce il nome del prodotto
     * @return ID del prodotto riscontrato nel database, '0' altrimenti
     */
    int checkProductStatus(int shopID, String productName);

    /**
     * Azzera la quantità di uno specifico prodotto, venduto da un negozio in particolare
     *
     * @param shopID ID di tipo 'int' dello shop che vende il prodotto
     * @param productID ID di tipo 'int' del prodotto
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean restoreProduct(int shopID, int productID);

    /**
     * Ottiene i prodotti dal nome simile al prodotto cercato dall'utente
     *
     * @param products Array di oggetti "Product" che viene caricata per riferimento
     * @param productName Stringa che definisce il prodotto cercato dall'utente
     */
    void getSimilarProducts(ArrayList<Product> products, String productName);

    /**
     * Aggiunge un prodotto al negozio
     *
     * @param shopID ID di tipo 'int' dello shop che vende il prodotto
     * @param productID ID di tipo 'int' del prodotto
     * @param quantity Quantità 'int' del prodotto
     * @param price Prezzo 'float' del prodotto
     * @param discount Sconto 'float' del prodotto
     * @return @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean addShopProduct(int shopID, int productID, int quantity, float price, float discount);


    /**
     *
     * @param shopID ID di tipo 'int' dello shop che vende il prodotto
     * @param name Stringa che definisce il nome del prodotto
     * @param description Stringa contenente la descrizione del nuovo prodotto
     * @param category Stringa che definisce la categoria del prodotto
     * @param price Prezzo 'float' del prodotto
     * @param discount Sconto 'float' del prodotto
     * @param quantity Quantità 'int' del prodotto
     * @param productPhoto Oggetto di tipo 'Part' contenente l'immagine
     * @return
     */
    boolean addNewProduct(int shopID, String name, String description, String category, float price, float discount, int quantity, Part productPhoto);

    /**
     * Utile per disattivare momentaneamente il commit automatico per gli update molteplici
     *
     * @param b 'True' lo attiva, 'false' lo disattiva
     */
    void setAutoCommit(boolean b);

    /**
     * Annulla i parziali cambiamenti al database
     */
    void rollback();

    /**
     * Aggiunge una foto prodotto al database
     * @param productID ID prodotto per il quale è necessario aggiungere la foto
     * @param productPhoto Foto caricata
     * @return True se il caricamento va a buon fine, false altrimenti.
     */
    boolean addProductPhoto(int productID, Part productPhoto);
}
