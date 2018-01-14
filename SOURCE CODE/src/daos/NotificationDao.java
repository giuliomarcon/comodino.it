package daos;

import main.Notification;
import main.User;

import java.io.Serializable;
import java.util.ArrayList;

public interface NotificationDao extends Serializable {

    /**
     * Crea la notifica di una nuova recensione al prodotto
     *
     * @param reviewID ID di tipo int della recensione
     * @param title    Stringa che contiene il titolo della recensione
     * @param rating   Int che rappresenta il numero di stelle che l'utente ha dato al prodotto
     * @return True se l'operazione è andata a buon fine, False altrimenti
     */
    boolean createProductReviewNotification(int reviewID, String title, int rating);

    /**
     * Crea la notifica di una nuova disputa
     *
     * @param title     Stringa che contiene il titolo della disputa
     * @param orderID   ID di tipo int dell'ordine
     * @param productID ID di tipo int del prodotto comprato
     * @param shopID    ID di tipo int che rappresenta lo shop
     * @return True se l'operazione è andata a buon fine, False altrimenti
     */
    boolean createDisputeNotification(String title, int orderID, int productID, int shopID);

    /**
     * Restuisce le notifiche del venditore
     *
     * @param user Oggetto di tipo User che rappresenta il venditore
     * @return Lista che contiene tutte le notifiche del venditore
     */
    ArrayList<Notification> getVendorNotifications(User user);

    /**
     * Restuisce le notifiche dell'admin
     *
     * @return Lista che contiene tutte le notifiche dell'admin
     */
    ArrayList<Notification> getAdminNotifications();

    /**
     * Ottiene le notifiche delle recensioni dei prodotti
     *
     * @param shopID ID di tipo int che rappresenta il negozio
     * @return Lista contenente le notifiche delle recensioni dei prodotti
     */
    ArrayList<Notification> getProductReviewNotifications(int shopID);

    /**
     * Ottiene le notifiche delle recensioni del negozio
     *
     * @param shopID ID di tipo int che rappresenta il negozio
     * @return Lista contenente le notifiche delle recensioni del negozio
     */
    ArrayList<Notification> getShopReviewNotifications(int shopID);

    /**
     * Ottiene le notifiche delle dispute del negozio
     *
     * @param shopID ID di tipo int che rappresenta il negozio
     * @return Lista contenente le notifiche delle recensioni del negozio
     */
    ArrayList<Notification> getDisputeNotifications(int shopID);

    /**
     * Aggiorna le notifiche quando sono state lette
     *
     * @param user Oggetto di tipo User che rappresenta l'utente
     * @return True se l'operazione è andata a buon fine, False altrimenti
     */
    boolean readNotifications(User user);
}