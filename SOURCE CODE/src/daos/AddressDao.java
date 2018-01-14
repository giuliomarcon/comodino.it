package daos;

import main.Address;
import main.User;

import java.io.Serializable;
import java.util.ArrayList;

public interface AddressDao extends Serializable {

    /**
     * Aggiunge un nuovo indirizzo di spedizione
     *
     * @param userID      ID di tipo int che rappresenta l'utente
     * @param firstname   Stringa contenente il nome del residente
     * @param lastname    Stringa contenente il cognome del residente
     * @param address     Stringa contenente l'indirizzo di spedizione
     * @param city        Stringa contenente la città
     * @param zip         Stringa contenente lo ZIP code
     * @param state       Stringa contenente lo stato
     * @param phonenumber Stringa contenente il numero di telefono
     * @return True se l'operazione è andata a buon fine, False altrimenti
     */
    boolean addAddress(int userID, String firstname, String lastname, String address, String city, String zip, String state, String phonenumber);

    /**
     * Restituisce l'indirizzo di spedizione
     *
     * @param addressID ID di tipo int dell'indirizzo di spedizione
     * @return Oggetto Address contenente l'indirizzo di spedizione
     */
    Address getAddress(int addressID);

    /**
     * Restituisce tutti gli indirizzi di spedizione di un utente
     *
     * @param user Oggetto di tipo User che rappresenta l'utente
     * @return Lista contenente tutti gli indirizzi di spedizione di un utente
     */
    ArrayList<Address> getAllAddresses(User user);

    /**
     * Rimuove un indirizzo di spedizione
     *
     * @param addressID ID di tipo int che rappresenta l'id dell'indirizzo di spedizioni
     * @param userID    ID di tipo int che rappresenta l'utente
     * @return True se l'operazione va a buon fine, False altrimenti
     */
    boolean removeAddress(int addressID, int userID);

    /**
     * Modifica l'indirizzo di spedizione
     *
     * @param user      Oggetto di tipo User che rappresenta l'utente
     * @param addressID ID che rappresenta l'ID del dell'indirizzo
     * @param firstName Stringa contenente il nome del residente
     * @param lastName  Stringa contenente il cognome del residente
     * @param address   Stringa contenente l'indirizzo di spedizione
     * @param city      Stringa contenente la città
     * @param zip       Stringa contenente lo ZIP code
     * @param state     Stringa contenente lo stato
     * @param phone     Stringa contenente il numero di telefono
     * @return True se l'operazione va a buon fine, False altrimenti
     */
    boolean editAddress(User user, String addressID, String firstName, String lastName, String address, String city, String zip, String state, String phone);
}
