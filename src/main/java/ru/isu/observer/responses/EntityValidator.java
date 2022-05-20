package ru.isu.observer.responses;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class EntityValidator {

    public static ResponseEntity<List<EntityError>> validate(Errors errors){
        if(errors.hasErrors()){
            List<EntityError> res = new ArrayList<>();
            for(ObjectError err: errors.getAllErrors()){
                FieldError fieldError = ((FieldError) err);
                res.add(new EntityError(fieldError.getField(), err.getDefaultMessage()));
            }
            return ResponseEntity.badRequest().body(res);
        }
        return ResponseEntity.ok().body(null);
    }

}
