package servlet.user.vendor;

import daos.ReviewDao;
import daos.impl.ReviewDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Permette al venditore di rispondere a una recensione data da un utente.
 */
@WebServlet(name = "OpenReviewReplyServlet", urlPatterns = {"/restricted/vendor/openreviewreply"})
public class OpenReviewReplyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        System.out.println("[INFO] OpenReviewReply Servlet: Entered");
        if (request.getParameter("description") == null || request.getParameter("reviewID") == null) {
            response.sendRedirect("/index.jsp?error=Parametri Mancanti");
            return;
        }
        String description = request.getParameter("description");
        int reviewID = Integer.parseInt(request.getParameter("reviewID"));

        System.out.println("\tReply review: " + reviewID +
                "\n\tDescription: " + description
        );

        ReviewDao reviewDao = new ReviewDaoImpl();
        int newReviewReply = reviewDao.createProductReviewReply(description, reviewID);
        if (newReviewReply == 0) {    // la creazione della risposte è fallita
            response.sendRedirect("/index.jsp?error=Errore Creazione Recensione");
            return;
        }
        System.out.println("[INFO] OpenReviewReply Servlet: Product review reply created.");
        response.sendRedirect("reviews.jsp?success=Risposta creata!");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp?error=Percorso invalido");
    }
}
