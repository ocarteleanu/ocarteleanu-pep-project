package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;
//class to manage connection to the account table in the database
public class AccountDAO {
    /**
     * @param account
     * @return
     */
    //method to handle sql insert statements into the account table 
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "insert into account(username, password) values (?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_account_id = (int)pkeyResultSet.getLong(1);
                    return new Account(generated_account_id, account.getUsername(), account.getPassword());
                }
        } catch(SQLIntegrityConstraintViolationException ec){
            System.out.println(ec.getMessage());
        }
            catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        return null;
    }
    //method to handle select queries from the account table
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> allAccounts = new ArrayList<>();
        String sql = "select * from account;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                 rs.getString("username"), rs.getString("password"));
                allAccounts.add(account);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return allAccounts;
    }
    //method to handle select queries from the account table with where clause 
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from account where username = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), 
                rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
