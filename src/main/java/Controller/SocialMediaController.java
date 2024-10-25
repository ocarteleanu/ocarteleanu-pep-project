package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getMesagesByAccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //handler for user registration
    private void postAccountHandler(Context context){
        Account account = context.bodyAsClass(Account.class);
        List<String> usernames = new ArrayList<>();
        accountService = new AccountService();
        List<Account> accounts = accountService.getAllAccounts();
        for(int i = 0; i < accounts.size(); i ++){
            usernames.add(accounts.get(i).username);
        }
        if(account.getPassword().length() < 4 || account.getUsername().isBlank() ||
        usernames.contains(account.getUsername())){
            context.status(400);
        }
        else {
            account = accountService.addAccount(account);
            if(account != null){
            context.json(account);
            }
        }
    }
    //handler for user login 
    private void loginAccountHandler(Context context){
        Account account = context.bodyAsClass(Account.class);
        accountService = new AccountService();
        String usernameInput = account.getUsername();
        String passwordInput = account.getPassword();
        Account accountInDB =accountService.getAccountByUsername(usernameInput);
        if(accountInDB != null){
            if(passwordInput.equals(accountInDB.getPassword())){
                context.json(accountInDB);
            }
            else {
                context.status(401);
            }
        }
        else{
            context.status(401);
        }
        }
        //handler for posting a new message 
    private void postMessageHandler(Context context){
        Message message = context.bodyAsClass(Message.class);
        messageService = new MessageService();
        accountService = new AccountService();
        String messageText = message.getMessage_text();
        int userId = message.getPosted_by();
        List<Integer> idsOfAccounts = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        accounts = accountService.getAllAccounts();
        for(int i = 0; i < accounts.size(); i++){
            idsOfAccounts.add(accounts.get(i).account_id);
        }
        if(messageText.isBlank() || messageText.length() > 255 || !idsOfAccounts.contains(userId)){
            context.status(400);
        }
        else {
            message = messageService.addMessage(message);
            if(message != null){
                context.json(message);
            }
        }
    }
    //handler for retrieving all the messages
    private void getAllMessagesHandler(Context context){
        messageService = new MessageService();
        List<Message> allMessages = messageService.retrieveAllMessages();
        context.json(allMessages);

    }
    //handler for retrieving mesages by message id
    private void getMessageByIdHandler(Context context){
        Message message = new Message();
        messageService = new MessageService();
        List<Message> allMessages = messageService.retrieveAllMessages();
        List<Integer> messageIds = new ArrayList<>();
        for(int i = 0; i < allMessages.size(); i++){
            messageIds.add(allMessages.get(i).getMessage_id());
        }
        int messageId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        message = messageService.getMessageById(messageId);

        if(!messageIds.contains(messageId)){
            context.result("");
        }
        else {
            if (!message.toString().isEmpty()){
                context.json(message);
            }
        }
    }
    //handler for deleting messages by message id
        private void deleteMessageByIdHandler(Context context){
            Message message = new Message();
            messageService = new MessageService();
            List<Message> allMessages = messageService.retrieveAllMessages();
            List<Integer> messageIds = new ArrayList<>();
            for(int i = 0; i < allMessages.size(); i++){
                messageIds.add(allMessages.get(i).getMessage_id());
            }
            int messageId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
            message = messageService.deleteMessageById(messageId);
    
            if(!messageIds.contains(messageId)){
                context.result("");
            }
            else {
                context.json(message);
        }
        
    }
    //handler for updating mesages
    private void updateMessageTextHandler(Context context){
        Message message = context.bodyAsClass(Message.class);
        messageService = new MessageService();
        String text = message.getMessage_text();
        int messageId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        List<Message> allMessages = messageService.retrieveAllMessages();
        List<Integer> messageIds = new ArrayList<>();
        for(int i = 0; i < allMessages.size(); i++){
            messageIds.add(allMessages.get(i).getMessage_id());
        }
        if(messageIds.contains(messageId) && !text.isBlank() && text.length() <= 255){
            message = messageService.updateMessageText(messageId, text);
            if(message != new Message()){
                context.json(message);
            }
        }
        else {
            context.status(400);
        }
    }
    //handler for retrieving messages by account id
    private void getMesagesByAccountIdHandler(Context context){
        accountService = new AccountService();
        messageService = new MessageService();
        List <Account> accounts = accountService.getAllAccounts();
        int accountId = Integer.parseInt(Objects.requireNonNull(context.pathParam("account_id")));
        List<Integer> accountIds = new ArrayList<>();
        for(int i = 0; i < accounts.size(); i++){
            accountIds.add(accounts.get(i).getAccount_id());
        }
        if(accountIds.contains(accountId)){
            context.json(messageService.getMessagesByUser(accountId));
        }
        else{
            context.result((new ArrayList<Message>()).toString());
        }
    }
}