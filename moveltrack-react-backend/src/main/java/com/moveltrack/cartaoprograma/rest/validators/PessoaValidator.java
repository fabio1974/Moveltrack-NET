package com.moveltrack.cartaoprograma.rest.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.moveltrack.cartaoprograma.model.Pessoa;


@Component("beforeCreatePessoaValidator")
public class PessoaValidator implements Validator {
 
    @Override
    public boolean supports(Class<?> clazz) {
        return Pessoa.class.equals(clazz);
    }
 
    @Override
    public void validate(Object obj, Errors errors) {
        Pessoa pessoa = (Pessoa) obj;
        if (checkInputString(pessoa.getNome())) {
            errors.rejectValue("nome", "nome.empty", "meuzovo");
        }
    
        if (checkInputString(pessoa.getEmail())) {
            errors.rejectValue("email", "email.empty");
        }
    }
 
    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }

}