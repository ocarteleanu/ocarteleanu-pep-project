package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;
//class to hadle connection to the message table in the database
public class MessageDAO {
    //method to handle insert statements into the message table
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "insert into message(posted_by, message_text, time_posted_epoch) values (?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int)pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(),
                 message.getMessage_text(), message.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    //message to handle select queries from the mesage table
    public List<Message> getAllMessages (){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> allMessages = new ArrayList<>();
        Message message = new Message();
        String sql = "select * from message;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                message = new Message(rs.getInt(1), rs.getInt(2),
                rs.getString(3), rs.getLong(4));
                allMessages.add(message);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return allMessages;
    }
    //method to handle sql queries from the message table with where clause
    public Message getMesageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from message where message_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Message(rs.getInt(1), rs.getInt(2), 
                rs.getString(3), rs.getLong(4));
            }
            else {
                return new Message();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Message();
    }
//method to handle sql delete statements from the message table 
    public Message deleteMessageById(int id){
        List<Message> messages = getAllMessages();
        Message theMessage = new Message();
        for(int i = 0; i < messages.size(); i ++){
            if(messages.get(i).getMessage_id() == id){
                theMessage = messages.get(i);
            }
        }        
        Connection connection = ConnectionUtil.getConnection();
        String sql = "delete from message where message_id = ?;";
        int numberOfMessagesDeleted  = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            numberOfMessagesDeleted = preparedStatement.executeUpdate();
            if(numberOfMessagesDeleted > 0){
                return theMessage;
            }
            else{
                return new Message();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Message();
    }
    //method to handle sql update statements to the message table
    public Message updateMessage(int messageId, String messageText){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "update message set message_text = ? where message_id = ?;";
        Message message = getMesageById(messageId);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);
            if(preparedStatement.executeUpdate() > 0){
                if(!message.equals(new Message())){
                    return new Message(messageId, message.getPosted_by(), 
                    messageText, message.getTime_posted_epoch());
                }
                else{
                    return new Message();
                }
            }
            else{
                return new Message();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Message();
    }
    //method to handle sql select queries from the message table 
    public List<Message> getMessagesByAccountId(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messagesOfUser = new ArrayList<>();
        String sql = "select * from message where posted_by = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                messagesOfUser.add(new Message(rs.getInt(1), rs.getInt(2), 
                rs.getString(3), rs.getLong(4)));
            }
            return messagesOfUser;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return messagesOfUser;
    }
  }
