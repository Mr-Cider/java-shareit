package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.shareit.exception.Checkers;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping
    public UserDto getUser(@RequestParam int id) {

    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserDto newUser, BindingResult bindingResult, WebRequest request) {
        Checkers.checkErrorValidation(bindingResult, log);
        return userService.createUser(newUser);
    }

    @PatchMapping
    public UserDto updateUser(@RequestHeader ("X-Later-User-Id") Long userId, UpdateUserDto updateUser, BindingResult bindingResult, WebRequest request) {
        Checkers.checkErrorValidation(bindingResult, log);
        return userService.updateUser(updateUser);
    }
}
