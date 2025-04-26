package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.*;
import com.example.exception.InvalidInputException;
import com.example.exception.UsernameNotFoundException;
import com.example.repository.*;

@Service
public class MessageService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        // Validating whether 'user posted_by' exists:
        Optional<Account> accountOptional = accountRepository.findById(message.getPostedBy());
        if (!accountOptional.isPresent()){
            throw new UsernameNotFoundException("User with that id does not exist");
        }
        // Validating whether the message text is valid:
        if(message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new InvalidInputException("Invalid message text");
        }
        return messageRepository.save(message);
    }

    // The API retrieves all messages:
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // The API is able to retrieve a message by 'ID':
    public Message getMessageById(int id) {
        // Validates whether the message exists:
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            return messageOptional.get();
        } else {
            throw new InvalidInputException("Invalid message ID");
        }
    }

    // The API is able to delete messages identified by their 'IDs':
    public void deleteMessageById(int id) {
        // Validating whether the message exists:
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            messageRepository.delete(messageOptional.get());
        } else {
            throw new InvalidInputException("Invalid message ID");
        }
    }

    // The API is able to update a message text using its 'ID':
    public Message updateMessage(int messageId, String messageText) {
        System.out.println("String input is: " + messageText);
        // Validating whether the message exists:
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            // Validating whether 'message_text' is valid:
            if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
                throw new InvalidInputException("Invalid message text");
            }
            message.setMessageText(messageText);
            return messageRepository.save(message);
        } else {
            throw new InvalidInputException("Invalid message ID");
        }
    }

    // The API retrieves all messages by a particular 'user':
    public List<Message> getMessagesByPoster(int id) {
        // Validates whether the account exists:
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            return messageRepository.findByPostedBy(id);
        }
        else {
            throw new InvalidInputException("Invalid user id");
        }
    }
}