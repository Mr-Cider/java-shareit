package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public UserDto getUser(@PathVariable Long id) {}

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {}

    @PatchMapping
    public UserDto updateUser(@RequestHeader ("X-Later-User-Id") Long userId, UserDto userDto) {}


}
