package daos;

import main.Cart;
import main.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.Part;
import java.io.Serializable;

public interface UserDao extends Serializable {
    /**
     * Autentica l'utente se presente in database
     *
     * @param email    email utente
     * @param password password utente
     * @return restituisce l'utente se si è autenticato con successo, 'null' altrimenti
     */
    User authUser(String email, String password);

    /**
     * Modifica la password dell'utente autenticato se presente in database
     *
     * @param user   utente al quale modificare la password
     * @param curPwd password attuale
     * @param newPwd nuova password
     * @return 'true' se la modifica è avvenuta con successo, 'false' altrimenti
     */
    boolean changePwd(User user, String curPwd, String newPwd);

    /**
     * Modifica info personali dell'utente autenticato se presente in database
     *
     * @param user utente al quale modificare le info
     * @return 'true' se la modifica è avvenuta con successo, 'false' altrimenti
     */
    boolean editInfo(User user);

    /**
     * Restituisce l'ID dell'oggetto user fornito in parametro
     *
     * @param user Oggetto User che rappresenta uno specifico utente
     * @return ID intero dell'utente nel caso esiste, 0 altrimenti
     */
    int getShopID(User user);

    /**
     * Cerca nel databese se esiste un carrello per l'utente registrato specificato ed eventualmente lo restituisce
     *
     * @param user Oggetto User che rappresenta uno specifico utente
     * @return Istanza di tipo Cart del carrello estratto dal database, null altrimenti
     */
    Cart getCart(User user);

    /**
     * Diminuisce la quantità di uno specifico articolo nel carrello di un utente registrato,
     * nel caso la quantità arrivi a zero elimina definitivamente l'articolo dal carrello
     *
     * @param user      Oggetto User che rappresenta uno specifico utente
     * @param productID ID di tipo 'int' del prodotto
     * @param shopID    ID di tipo 'int' dello shop che vende il prodotto
     */
    void decreaseCartItem(User user, int productID, int shopID);

    /**
     * Aumenta la quantità di uno specifico articolo nel carrello di un utente registrato
     *
     * @param user      Oggetto di tipo 'User' che rappresenta uno specifico utente
     * @param productID ID di tipo 'int' del prodotto
     * @param shopID    ID di tipo 'int' dello shop che vende il prodotto
     */
    void addCartItem(User user, int productID, int shopID);

    /**
     * Imposta la quantità di uno specifico articolo nel carrello di un utente registrato
     *
     * @param user      Oggetto di tipo 'User' che rappresenta uno specifico utente
     * @param productID ID di tipo 'int' del prodotto
     * @param shopID    ID di tipo 'int' dello shop che vende il prodotto
     * @param quantity  intero  che determina la quantità di prodotto da impostare
     */
    void setCartItem(User user, int productID, int shopID, int quantity);

    /**
     * Elimina definitivamente l'articolo dal carrello
     *
     * @param user      Oggetto User che rappresenta uno specifico utente
     * @param productID ID di tipo 'int' del prodotto
     * @param shopID    ID di tipo 'int' dello shop che vende il prodotto
     */
    void removeCartItem(User user, int productID, int shopID);

    /**
     * Ottiene la foto profilo dell'utente
     *
     * @param user      Oggetto User che rappresenta uno specifico utente
     * @return          Stringa codificata in base 64 che rappresenta l'immagine
     */
    String getUserPhoto(User user);
    /**
     * Registra un utente con nome, cognome, email, password
     *
     * @param firstname Nome dell'utente
     * @param lastname  Cognome dell'utente
     * @param email     Email dell'utente
     * @param password  Password dell'utente
     * @return Numero di tipo 'int' basato sulla seguente casistica: '-3' connessione SMPT fallita,
     * '-2' alcuni campi sono rimasti vuoti, '-1' oppure '0' email già in uso, '1' registrazione effettuata con successo
     */
    int register(String firstname, String lastname, String email, String password);

    /**
     * Accetta la privacy una tantum per l'utente registrato
     *
     * @param user Oggetto di tipo 'User' che rappresenta uno specifico utente
     * @return 'true': operazione andata a buon fine, 'false' altrimenti
     */
    boolean acceptPrivacy(User user);

    /**
     * Restituisce l'utente registrato con l'ID specificato
     *
     * @param userID ID di tipo 'int' dell'utente
     * @return Oggetto di tipo 'User' che rappresenta lo specifico utente desiderato
     */
    User getUser(int userID);

    /**
     * Conferma l'email controllando che ci sia un token "pending" nel database
     *
     * @param token Il codice alfanumerico che contraddistingue il token di conferma
     * @return Restituisce 'false' in caso l'email sia già utilizzata per un altro account, nel caso
     * ci siano problemi con i servizi google, o nel caso non riesca ad eseguire l'UPDATE a livello di database,
     * altrimenti restituisce 'true'
     */
    boolean confirm(String token);

    /**
     * Trasferisce le informazioni sul carrello nei cookie nella versione database persistente per l'utente
     *
     * @param user    Oggetto User che rappresenta uno specifico utente
     * @param cookies Array di 'Cookie' attualmente esistente per l'utente
     * @return -1: cookie vuoto o assente, situazione invariata
     *          0: errore nel parsing del cookie
     *        x>0: quantità di prodotti successivamente integrati
     */
    int cookieToCart(User user, Cookie[] cookies);

    boolean checkIfReviewExists(int userID, int shopID);

    boolean checkEmail(String email);

    boolean updateResetToken(String email, String passwordResetToken);

    boolean resetPassword(String token, String email, String pwda);

    /**
     * Aggiunge foto all'utente
     *
     * @param user          Oggetto User che rappresenta uno specifico utente
     * @param userPhoto     Foto da aggiungere
     * @return              true se ha successo false altrimenti
     */
    boolean addUserPhoto(User user, Part userPhoto);
}
