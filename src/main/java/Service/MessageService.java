package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;
//Class that sits between the web layer (controller) and the  persistence layer (MessageDAO).
public class MessageService {
    private MessageDAO messageDAO;
    //no arguments constructor
    public MessageService () {
        messageDAO = new MessageDAO();
    }
    //One argument constructor
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    //method that calls the insertMessage method on the messageDAO field
     public Message addMessage (Message message){
        return this.messageDAO.insertMessage(message);
     }
     //method that calls the getAllMessages method on the messageDAO field
     public List<Message> retrieveAllMessages(){
        return this.messageDAO.getAllMessages();
     }
     //method that calls the getMessageById method on the messageDAO field
     public Message getMessageById(int id){
        if(this.messageDAO.getMesageById(id) != null){
         return this.messageDAO.getMesageById(id);
        }
        else{
         return new Message();
        }
     }
     //method that calls the deleteMessageById method on the messageDAO field
     public Message deleteMessageById(int id){
         return this.messageDAO.deleteMessageById(id);
     }
     //method that calls the updateMessage method on the messageDAO field
     public Message updateMessageText(int id, String text){
        return this.messageDAO.updateMessage(id, text);
     }
     //method that calls the getMessageAccountId method on the messageDAO field
     public List<Message> getMessagesByUser(int accountId){
         return this.messageDAO.getMessagesByAccountId(accountId);
     }
}
