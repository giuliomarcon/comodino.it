package daos;

import main.User;

import java.io.Serializable;

public interface PaymentDao extends Serializable {

    /**
     * Gestisce il pagamento dei prodotti
     *
     * @param user       Oggetto di tipo User che rappresenta l'utente
     * @param cardHolder Nome del proprietario della carta utilizzata per il pagamento
     * @param cardNumber Numero della carta utilizzata per il pagamento
     * @param expiryDate Data di scadenza della carta utilizzata per il pagamento
     * @param cvv        CCV della carta utilizzata per il pagamento
     * @return ID di tipo int della transazione se l'operazione è andata a buon fine, 0 altrimenti
     */
    int createPayment(User user, String cardHolder, String cardNumber, String expiryDate, int cvv);
}
