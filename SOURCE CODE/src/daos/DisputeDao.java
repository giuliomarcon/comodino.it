package daos;

import main.Dispute;

import java.io.Serializable;
import java.util.ArrayList;

public interface DisputeDao extends Serializable {
    /**
     * Crea una disputa.
     * @param orderID ID ordine per cui si richiede la disputa
     * @param productID ID prodotto per il quale si richiede la disputa
     * @param shopID ID shop destinatario della disputa
     * @param title Titolo della disputa
     * @param description Descrizione della disputa
     * @return True se la disputa viene creata con successo, False altrimenti
     */
    boolean createDispute(int orderID, int productID, int shopID, String title, String description);

    /**
     * Recupera tutte le dispute
     * @return ArrayList contenente tutte le dispute.
     */
    ArrayList<Dispute> allDisputes();

    /**
     * Aggiorna stato disputa
     * @param orderID ID ordine interessato dalla disputa
     * @param productID ID prodotto interessato dalla disputa
     * @param shopID ID shop destinatario della disputa
     * @param action Nuovo stato della disputa
     * @return True se l'aggiornamento va a vuon fine, False altrimenti
     */
    boolean updateDispute(int orderID, int productID, int shopID, int action);

    /**
     * Recupera una disputa dato l'utente
     * @param orderID ID ordine interessato dalla disputa
     * @param productID ID prodotto interessato dalla disputa
     * @param shopID ID shop destinatario della disputa
     * @return Disputa corrispondente ai parametri
     */
    Dispute getDisputeByUser(int orderID, int productID, int shopID);

    /**
     * Ottiene tutte le dispute di uno shop
     * @param shopID ID negozio per il quale recuperare la lista dispute
     * @return ArrayList contenente le dispute
     */
    ArrayList<Dispute> getDisputeByShop(int shopID);
}
