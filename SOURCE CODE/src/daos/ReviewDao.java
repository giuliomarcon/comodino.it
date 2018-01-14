package daos;

import main.*;

import java.io.Serializable;
import java.util.ArrayList;

public interface ReviewDao extends Serializable {

    /**
     * Crea la recensione del prodotto
     *
     * @param title       Stringa che contiene il titolo della recensione
     * @param description Stringa che contiene la descrizione della recensione
     * @param rating      Int che contiene il numero di stelle della recensione, da 1 a 5
     * @param productID   ID di tipo int del prodotto da recensire
     * @param userID      ID di tipo int dell'utente che sta scrivendo la recensione
     * @return Se l'operazione va a buon fine restituisce l'ID della recensione, altrimenti restituisce 0
     */
    int createProductReview(String title, String description, int rating, int productID, int userID);

    /**
     * Crea la risposta alla recensione
     *
     * @param description Stringa che contiene la risposta alla recensione
     * @param reviewID    ID della recensione che sto rispondendo
     * @return Ritorna un intero de l'operazione va a buon fine, 0 altrimenti
     */
    int createProductReviewReply(String description, int reviewID);

    /**
     * Controlla se una recensione ha già una risposta
     *
     * @param reviewID ID di tipo int della recensione che devo controllare
     * @return Ritorna 1 se la recensione ha già una risposta, 0 altrimenti
     */
    int isReviewReplied(int reviewID);

    /**
     * Restituisce la risposta della recensione dato l'ID della recensione
     *
     * @param reviewID ID di tipo int della recensione che ha la risposta
     * @return oggetto ReviewReply che contiene la risposta alla recensione
     */
    ReviewReply getProductReviewReply(int reviewID);

    /**
     * Restituisce tutte le recensioni di un determinato prodotto in ordine cronologico
     *
     * @param productID ID di tipo int del prodotto da cui devo ottenere le recensioni
     * @return Lista di tutte le recensioni del prodotto in ordine cronologico
     */
    ArrayList<ProductReview> getProductReviews(int productID);

    /**
     * Crea la recensione di un venditore
     *
     * @param title       Stringa che contiene il titolo della recensione
     * @param description Stringa che contiene la descrizione della recensione
     * @param rating      Int che contiene il numero di stelle della recensione, da 1 a 5
     * @param shopID      ID di tipo int del venditore da recensire
     * @param userID      ID di tipo int dell'utente che sta scrivendo la recensione
     * @return Se l'operazione va a buon fine restituisce l'ID della recensione, altrimenti restituisce 0
     */
    int createShopReview(String title, String description, int rating, int shopID, int userID);

    /**
     * Restituisce tutte le recensioni di un determinato venditore in ordine cronologico
     *
     * @param shopID ID di tipo int del venditore da cui devo ottenere le recensioni
     * @return Lista di tutte le recensioni del venditore in ordine cronologico
     */
    ArrayList<ShopReview> getShopReviews(int shopID);

    /**
     * Restituisce tutte le recensioni dei prodotti venduti dal venditore in ordine cronologico
     *
     * @param shopID ID di tipo int del venditore
     * @return Lista di tutte le recensioni dei prodotti venduti dal venditore in ordine cronologico
     */
    ArrayList<ProductReview> getVendorProductReviews(int shopID);

    /**
     * Restituisce l'utente che ha scritto la recensione
     *
     * @param userID ID di tipo int dell'utente che ha scritto la recensione
     * @return Oggetto User dell'utente che ha scritto la recensione
     */
    User getReviewAuthor(int userID);

    /**
     * Restituisce l'oggetto recensito
     *
     * @param productID ID di tipo int dell'oggetto recensito
     * @param shopID    ID di tipo int dello shop
     * @return Oggetto Product con l'oggetto recensito
     */
    Product getReviewProduct(int productID, int shopID);

    /**
     * Restituisce la Review sapendo l'utente
     *
     * @param user      Oggetto utente
     * @param productID ID di tipo int del prodotto
     * @return Oggetto ProductReview contenente la recensione del prodotto
     */
    ProductReview getProductReviewByUser(User user, int productID);

}