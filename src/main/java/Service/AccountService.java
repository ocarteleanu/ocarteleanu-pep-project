package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;
//Class that sits between the web layer (controller) and the persistence layer (AccountDAO)
public class AccountService {
    private AccountDAO accountDAO;
    //no argument constructor
    public AccountService (){
        accountDAO = new AccountDAO();
    }
    //one argument constructor
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    //method that calls the addAccount method on the accountDAO field
    public Account addAccount (Account account){
       return  this.accountDAO.insertAccount(account) ;
    }
    //method that calls the getAllAccounts method on the accountDAO field
    public List<Account> getAllAccounts(){
       return this.accountDAO.getAllAccounts();
    }
    //method that calls the getAccountByUsername method on the accountDAO field
    public Account getAccountByUsername (String username){
        return this.accountDAO.getAccountByUsername(username);
    }
    
    }

    

