package utils;

import com.google.gson.Gson;
import com.sun.mail.smtp.SMTPAddressFailedException;
import daos.ShopDao;
import daos.impl.ShopDaoImpl;
import main.PhysicalShop;
import utils.json.GoogleLocationJson;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

public class Utils {
    /**
     * dato il blob nel db ritorna stringa da piazzare nel campo <img src="qui"> nell'html
     *
     * @param blob  blob dal database
     * @return      stringa già codificata come immagine o placeholder
     */
    public static String getStringfromBlob(Blob blob){
        try {
            String imgDataBase64 = new String(Base64.getEncoder().encode(blob.getBytes(1, (int) blob.length())));
            if(imgDataBase64.isEmpty()){
                imgDataBase64 = "/ImageNotFound.png";
            }
            else {
                imgDataBase64 = "data:image/gif;base64," + imgDataBase64;
            }
            blob.free();
            return imgDataBase64;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "/ImageNotFound.png";
    }
    public static String getNDecPrice(float p,int n){
        return String.format("%."+n+"f",p);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String sendVerificationEmail(String firstname, String lastname, String email) {

        String easy = RandomString.digits + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
        RandomString tokenGenerator = new RandomString(23, new SecureRandom(), easy);

        String verificationToken = tokenGenerator.nextString();

        final String google_username = "abbdevs@gmail.com";
        final String google_password = "&el0nMuschi3tt0%";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(google_username, google_password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@comodino.it"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
                    //InternetAddress.parse("abbdevs@gmail.com"));
            message.setSubject("Verifica il tuo account su Comodino.it");
            message.setText("Comodino.it\n\n" +

                            "Hey, " + firstname + ", grazie per esserti registrato su Comodino!\n"+
                            "Clicca il seguente link per attivare il tuo account:\n\n" +

                            "http://localhost:8080/emailConfirm?token="+verificationToken+"\n\n" +

                            "Lo Staff di Comodino.it\n" +
                            "http://localhost:8080/");

            Transport.send(message);

            //System.out.println("[INFO] Email di conferma inviata con successo a '"+email+"'");
            System.out.println("[INFO] Link di verifica http://localhost:8080/emailConfirm?token="+verificationToken);

        }
        catch (SendFailedException e){
            return "invalid";
        }
        catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return null;
        }

        return verificationToken;
    }

    public static PhysicalShop updateGPSCoords(PhysicalShop shop) {
        try{
            //https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
            String sURL = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(shop.getAddress(), "UTF-8") + ",+" +
                URLEncoder.encode(shop.getCity(), "UTF-8") + ",+" +
                URLEncoder.encode(shop.getZip(),"UTF-8") + "&key=";
            String apikey = "AIzaSyDNMIz_QgiWP6ayg3icP3ZmLXt6OE_Qync";
            // Connect to the URL using java's native library
            System.out.println("[GOOGLE GEOCODING URL] "+sURL+apikey);

            String jsonString = new Scanner(new URL(sURL+apikey).openStream(), "UTF-8").useDelimiter("\\A").next();
            Gson gson = new Gson();
            GoogleLocationJson mapsJson = gson.fromJson(jsonString, GoogleLocationJson.class);

            if(mapsJson.getResults().size() > 0){
                shop.setLatitude( Float.parseFloat(mapsJson.getResults().get(0).getGeometry().getLocation().getLat().toString()));
                shop.setLongitude( Float.parseFloat(mapsJson.getResults().get(0).getGeometry().getLocation().getLng().toString()));
                System.out.println("[GEOCODING LAT-LNG] "+shop.getLatitude()+" - "+shop.getLongitude());
                return shop;
            }
            return shop;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return shop;
    }

    public static ArrayList<Float> updateGPSCoords(String address, String city, String zip) {
        try{
            ArrayList<Float> latlong = new ArrayList<>();
            //https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
            String sURL = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    URLEncoder.encode(address, "UTF-8") + ",+" +
                    URLEncoder.encode(city, "UTF-8") + ",+" +
                    URLEncoder.encode(zip,"UTF-8") + "&key=";
            String apikey = "AIzaSyDNMIz_QgiWP6ayg3icP3ZmLXt6OE_Qync";
            // Connect to the URL using java's native library
            System.out.println("[GOOGLE GEOCODING URL] "+sURL+apikey);

            String jsonString = new Scanner(new URL(sURL+apikey).openStream(), "UTF-8").useDelimiter("\\A").next();
            Gson gson = new Gson();
            GoogleLocationJson mapsJson = gson.fromJson(jsonString, GoogleLocationJson.class);

            if(mapsJson.getResults().size() > 0){
                latlong.add(Float.parseFloat(mapsJson.getResults().get(0).getGeometry().getLocation().getLat().toString()));
                latlong.add(Float.parseFloat(mapsJson.getResults().get(0).getGeometry().getLocation().getLng().toString()));

                return latlong;
            }
            return null;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static String sendResetEmail(String email) {

        String easy = RandomString.digits + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
        RandomString tokenGenerator = new RandomString(23, new SecureRandom(), easy);

        String verificationToken = tokenGenerator.nextString();

        final String google_username = "abbdevs@gmail.com";
        final String google_password = "&el0nMuschi3tt0%";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(google_username, google_password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("passwordreset@comodino.it"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            //InternetAddress.parse("abbdevs@gmail.com"));
            message.setSubject("Ripristino password su Comodino.it");
            message.setText("Comodino.it\n\n" +

                    "Clicca il seguente link per ripristinare la password:\n"+

                    "http://localhost:8080/index.jsp?token="+verificationToken+"&email="+email+"\n" +

                    "Se non avessi richiesto il ripristino, ignora semplicemente questa mail.\n\n"+

                    "Lo Staff di Comodino.it\n" +
                    "http://localhost:8080/");

            Transport.send(message);

            //System.out.println("[INFO] Email di conferma inviata con successo a '"+email+"'");
            System.out.println("[INFO] Token di verifica http://localhost:8080/index.jsp?token="+verificationToken+"&email="+email);

        }
        catch (SendFailedException e){
            return "invalid";
        }
        catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return null;
        }

        return verificationToken;
    }
}


