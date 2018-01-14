package daos;

import main.Order;
import main.User;

import java.io.Serializable;
import java.util.ArrayList;

public interface OrderDao extends Serializable {

    /**
     * Restituisce tutti gli ordini di un determinato utente
     *
     * @param user Oggetto User che rappresenta l'utente di cui voglio vedere gli ordini
     * @return Lista contentente tutti gli ordini di un determinato utente
     */
    ArrayList<Order> getAllOrders(User user);

    /**
     * Imposta l'indirizzo a cui devo spedire l'ordine
     *
     * @param user          Oggetto User che rappresenta l'utente
     * @param address       Stringa contenente l'indirizzo di spedizione
     * @param ritironegozio Lista dei prodotti che posso ritirare in negozio
     * @return True se l'operazione è andata a buon fine, False altrimenti
     */
    boolean setOrderAddresses(User user, String address, ArrayList<String> ritironegozio);

    /**
     * Crea un nuovo ordine
     *
     * @param user      Oggetto User che rappresenta l'utente
     * @param paymentID ID di tipo int che rappresenta la carta utilizzata dall'utente
     * @return ID dell'oridine se l'operazione è andata a buon fine, 0 altrimenti
     */
    int createOrder(User user, int paymentID);

    /**
     * Rimuove tutti i prodotti dal carrello
     *
     * @param user Oggetto User che rappresenta l'utente
     * @return True se l'operazione è andata a buon fine, False altrimenti
     */
    boolean cleanCart(User user);

    /**
     * Imposta l'ordine a 'Completato'
     *
     * @param userID    ID di tipo int che rappresenta l'utente
     * @param orderID   ID che rappresenta l'ordine
     * @param productID ID che rappresenta il prodotto
     * @param shopID    ID che rappresenta lo shop
     * @return True se l'operazione è andata a buon fine, False altrimenti
     */
    boolean finishOrderProd(int userID, String orderID, String productID, String shopID);
}
