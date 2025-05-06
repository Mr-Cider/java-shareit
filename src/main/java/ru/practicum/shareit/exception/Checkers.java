package ru.practicum.shareit.exception;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Checkers {

    public static void checkErrorValidation(BindingResult bindingResult, Logger log) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            errors.forEach(log::error);
            throw new AnnotationValidatorException(errors);
        }
    }
}
