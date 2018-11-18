package dao.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

import dto.User;


public interface IUserDAO {
	public void registerUser(User user, Connection conn) throws SQLException;
	public boolean doesUserExists(User user, Connection conn) throws SQLException;
}
