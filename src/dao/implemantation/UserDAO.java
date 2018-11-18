package dao.implemantation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import dao.interfaces.IUserDAO;
import dto.User;


public class UserDAO implements IUserDAO{
	private static final String SQL_INSERT =
	        "INSERT INTO user ("
	        + "Type, Firstname, Lastname, Email, Username, Password, Birthday, "
	        + "IsActive, RegistrationDate"
	        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_EXISTS = "SELECT Type, Firstname, Lastname, Email, Username, Password, Birthday, IsActive, RegistrationDate "
            + "FROM user WHERE Username = ?";
	
	@Override
	public void registerUser(User user, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(SQL_INSERT);
            ps.setInt(1, user.getType());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastname());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getPassword());
            ps.setDate(7, new java.sql.Date(user.getBirthday().getTime()));
            ps.setBoolean(8, user.isActive());
            ps.setDate(9, new java.sql.Date(new java.util.Date().getTime()));
            
            ps.executeUpdate();
        }finally {
            close(ps);
        }
    }
	
	@Override
	public boolean doesUserExists(User user, Connection conn) throws SQLException {
		PreparedStatement ps = null;
    	ResultSet rs = null;
        try {
            ps = conn.prepareStatement(SQL_EXISTS);
            ps.setString(1, user.getUsername());
            rs = ps.executeQuery();
            ArrayList<User> result = getResults(rs);
            if(result.size() > 0){
            	return true;
            }else {
            	return false;
            }
        }finally {
            close(ps);
            close(rs);
        }
	}
	
	protected ArrayList<User> getResults(ResultSet rs) throws SQLException {
		ArrayList<User> results = new ArrayList<User>();
        while (rs.next()) {
        	User user = new User();
        	user.setType(rs.getInt("Type"));
        	user.setFirstname(rs.getString("Firstname"));
        	user.setLastname(rs.getString("Lastname"));
        	user.setEmail(rs.getString("Email"));
        	user.setUsername(rs.getString("Username"));
        	user.setPassword(rs.getString("Password"));
        	user.setBirthday(rs.getDate("Birthday"));
        	user.setActive(rs.getBoolean("IsActive"));
        	user.setRegistrationDate(rs.getDate("RegistrationDate"));
        	
            results.add(user);
        }
        return results;
    }
	
	protected void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }catch(SQLException e){}
        }
    }

    protected void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }catch(SQLException e){}
        }
    }
}
