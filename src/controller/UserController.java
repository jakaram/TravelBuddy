package controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dao.implemantation.UserDAO;
import dao.interfaces.IUserDAO;
import database.ConnectionPool;
import dto.User;
import enums.UserTypeEnum;
import utils.Utils;

@ManagedBean(name = "user")
@SessionScoped
public class UserController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User registrationUser = new User();
	private String passwordConfirmation;
	
	public User getRegistrationUser() {
		return registrationUser;
	}

	public void setRegistrationUser(User registrationUser) {
		this.registrationUser = registrationUser;
	}
	
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String registerUser() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		
		if(passwordConfirmation.equals(registrationUser.getPassword())){
			IUserDAO db = new UserDAO();
			Connection conn;
			try {
				conn = ConnectionPool.getConnectionPool().checkOut();
				boolean result = db.doesUserExists(registrationUser, conn);
				if(result == true){
					ctx.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Postoji korisnik sa korisnickim imenom " 
									+ registrationUser.getUsername(),
									"Unesite novo korisnicko ime"));
				}else {
					
					registrationUser.setPassword(Utils.pass2md5(registrationUser.getPassword()));
					registrationUser.setActive(false);
					registrationUser.setType(UserTypeEnum.REGISTERED.ordinal());
					
					db.registerUser(registrationUser, conn);
					
					ctx.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Nalog poslan na odobrenje",
									"Administrator mora potvrditi nalog"));
					
					registrationUser = new User();
					passwordConfirmation = "";
				}
				ConnectionPool.getConnectionPool().checkIn(conn);
			} catch (SQLException e) {
				ctx.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Greška sa bazom",
						"Greška sa bazom"));
				e.printStackTrace();
			}
			
		}else {
			ctx.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Niste korektno potvrdili šifru",
					"Šifre nisu jednake"));
		}

		return null;
	}
}
