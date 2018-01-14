package daos;

import main.*;

import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public interface ShopDao extends Serializable {

    /**
     *  Restituisce il negozio specificato dall'ID con le relative informazioni
     *
     * @param shopID ID di tipo 'int' del negozio
     * @return Oggetto di tipo 'Shop'
     */
    Shop getShop(int shopID);

    /**
     * Ottiene tutti i prodotti di uno specifico negozio
     *
     * @param shopID ID di tipo 'int' del negozio
     * @return Mappa contenente entry formate da nome del prodotto e oggetto 'ProductGroup'
     */
    HashMap<String, ProductGroup> getShopProducts(String shopID);

    /**
     * Conta quanti negozi vendono questo prodotto
     *
     * @param productID ID di tipo 'int' del prodotto
     * @return Int che contiene il numero dei negozi che vendono questo prodotto
     */
    int hasOtherShops(int productID);

    /**
     * Ottiene i vari negozi fisici che vendono uno specifico prodotto
     *
     * @param productID ID di tipo 'int' del prodotto
     * @return ArrayList che contiene i vari negozi di tipo 'Shop'
     */
    ArrayList<Shop> getPhysicalShopsByProduct (int productID);

    /**
     * Ottiene i prodotti del venditore specificato che hanno una disponibilità inferiore ad una certa soglia (20)
     *
     * @param shopID ID di tipo 'int' del negozio
     * @return ArrayList che contiene i vari prodotti di tipo 'Product'
     */
    ArrayList<Product> obtainExpiringProducts (int shopID);

    /**
     * Ottiene i prodotti del venditore specificato
     *
     * @param shopID ID di tipo 'int' del negozio
     * @return ArrayList che contiene i vari prodotti di tipo 'Product'
     */
    ArrayList<Product> obtainProducts(int shopID);

    /**
     * Modifica le informazioni di base di un negozio fisico
     *
     * @param shop Istanza 'PhysicalShop' della superclasse 'Shop' del negozio fisico
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean editPhysicalInfo(PhysicalShop shop);

    /**
     * Modifica le informazioni di base di un negozio
     *
     * @param shop Istanza 'Shop' del negozio
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean editInfo(Shop shop);

    /**
     * Crea un nuovo shop per un utente registrato
     *
     * @param user Oggetto User che rappresenta uno specifico utente
     * @param shopName Nome del negozio (stringa)
     * @param shopDescription Descrizione del negozio (stringa)
     * @param shopWebsite URL del sito web del negozio (stringa)
     * @return '0' in caso di fallimento, altrimenti restituisce l'ID dello shop creato con successo
     */
    int createNewShop(User user, String shopName, String shopDescription, String shopWebsite);

    /**
     *
     * @param user Oggetto User che rappresenta uno specifico utente
     * @param shopName Nome del negozio (stringa)
     * @param shopDescription Descrizione del negozio (stringa)
     * @param shopWebsite URL del sito web del negozio (stringa)
     * @param shopAddress Indirizzo del negozio fisico (stringa)
     * @param shopCity Città del negozio fisico (stringa)
     * @param shopState Stato del negozio fisico (stringa)
     * @param shopZIP ZIP del negozio fisico (stringa)
     * @param shopOpeningHours Orario di apertura del negozio fisico (stringa)
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean createNewPhysicalShop(User user, String shopName, String shopDescription, String shopWebsite, String shopAddress, String shopCity, String shopState, String shopZIP, String shopOpeningHours, Float shopLatitude, Float shopLongitude);

    /**
     * Modifica un prodotto specifico di un negozio in particolare
     *
     * @param product Oggetto 'Product' del prodotto da modificare
     * @param shopID ID di tipo 'int' del negozio
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean editShopProduct(Product product, int shopID);

    /**
     * Permette l'aggiunta dello shop fisico in un secondo momento dopo la creazione del negozio online
     *
     * @param shopID ID di tipo 'int' del negozio
     * @param shop Istanza 'PhysicalShop' della superclasse 'Shop' del negozio fisico
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean addPhysicalShop(int shopID, PhysicalShop shop);

    /**
     * Aggiunge una foto al negozio
     *
     * @param shopID ID di tipo 'int' del negozio
     * @param fileName Oggetto di tipo 'Part' contenente l'immagine
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean addShopPhoto(int shopID, Part fileName);

    /**
     * Ottiene le immagini dello shop
     *
     * @param shopID ID di tipo 'int' del negozio
     * @return Array di stringhe che contengono le varie immagini in formato base64
     */
    ArrayList<String> getImages(int shopID);

    /**
     * Forza il ricalcolo della valutazione di uno shop
     *
     * @param shopID ID di tipo 'int' del negozio
     * @return 'true' se l'operazione è andata a buon fine, 'false' altrimenti
     */
    boolean updateShopRating(int shopID);

    /**
     * Aggiorna latitudine e longitudine di un determinato negozio
     *
     * @param shopID ID di tipo 'int' del negozio
     * @param latitude 'float' che specifica la latitudine
     * @param longitude 'float' che specifica la longitudine
     */
    void updateLatLong(int shopID, float latitude, float longitude);

    /**
     * Conteggia i report negativi di un determinato negozio
     *
     * @param shopID shopID ID di tipo 'int' del negozio
     * @return Il numero intero di reports
     */
    int countBadReports(int shopID);
}
