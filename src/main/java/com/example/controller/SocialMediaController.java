package com.example.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.entity.*;
import com.example.exception.InvalidInputException;
import com.example.exception.UsernameNotFoundException;
import com.example.exception.UserPasswordMismatchException;
import com.example.exception.UsernameAlreadyTakenException;
import com.example.service.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    // The API processes new 'user' registrations:
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        try {
            Account registeredAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(registeredAccount);
        } catch (UsernameAlreadyTakenException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // The API processes 'user' logins:
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        try {
            Account authAccount = accountService.authenticate(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(authAccount);
        } catch (UserPasswordMismatchException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // The API processes new message creation:
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try{
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.ok(createdMessage);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // The API retrieves all messages:
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // The API is able to retrieve a message by 'ID':
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id){
        try{
            Message message = messageService.getMessageById(id);
            return ResponseEntity.ok(message);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    // The API is able to delete messages identified by their 'IDs':
    @DeleteMapping("/messages/{id}") 
    public ResponseEntity<?> deleteMessageById(@PathVariable int id) {
        try{
            messageService.deleteMessageById(id);
            return ResponseEntity.ok(1);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    // The API is able to update a message text using its 'ID':
    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable int id, @RequestBody Message message) {
        try{
            messageService.updateMessage(id, message.getMessageText());
            return ResponseEntity.ok(1);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // The API retrieves all messages by a particular 'user':
    @GetMapping("/accounts/{id}/messages")
    public ResponseEntity<?> getMessagesByPoster(@PathVariable int id) {
        try{
            List<Message> messages = messageService.getMessagesByPoster(id);
            return ResponseEntity.ok(messages);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } 
    }
}