package com.example.service;

import com.example.entity.Account;
import com.example.exception.InvalidInputException;
import com.example.exception.UserPasswordMismatchException;
import com.example.exception.UsernameAlreadyTakenException;
import com.example.repository.AccountRepository;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // The API processes new 'user' registrations:
    public Account registerAccount(Account account) {
        // Validating whether an account with a desired 'username' exists: 
        Optional<Account> accountOptional = accountRepository.findByUsername(account.getUsername());
        if(accountOptional.isPresent()){ 
            throw new UsernameAlreadyTakenException("Username is already taken");
        }
        // Validating whether the 'username' is valid:
        if(account.getUsername() == null || account.getUsername().isBlank()){
            throw new InvalidInputException("Invalid username");
        }
        // Validating whether the 'password' is valid:
        if(account.getPassword() == null || account.getPassword().isBlank() || account.getPassword().length() < 4){
            throw new InvalidInputException("Invalid password");
        }
        return accountRepository.save(account);
    }

    // The API processes 'user' logins:
    public Account authenticate(String username, String password) {
        // Validating whether the account exists:
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if(accountOptional.isPresent()){ 
            Account existing = accountOptional.get();
            // Validating whether the 'passwords' match or not:
            if(!existing.getPassword().equals(password)) {
                throw new UserPasswordMismatchException("Username and password do not match");
            } 
            return existing;   
        } else {
            throw new UserPasswordMismatchException("Username not found");
        }
    }
}