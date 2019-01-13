package Communication.Servlet.Base;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import WebProject.Servlet.ServletBase;

/*
 * Tipicamente questo tipo di servlet serve per la comunicazione con l'App ed il suo ciclio di vita è
 * - Init
 * - Business
 * - Response
 * - Destroy
 * Quindi non serve la gestione delle sessioni (se non per l'interrogazione DB) perchè una chiamata nasce e muore in un ciclo
 * In sostanza è una ServletBase PARTICOLARE
 */

@WebServlet("/ServletCommunication")
public class ServletCommunication extends ServletBase {
	private static final long serialVersionUID = 1L;
       
    public ServletCommunication() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response; 
		session = request.getSession();
		// TODO
		// Introdurre logica per login e check
		SetUserParams();
		CheckUser();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO
		// C'è da verificare tramite app se sono necessarie chiamate POST...per ora userò le GET!
		doGet(request, response);
	}

}
