package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import java.util.Map;

@Service
@Validated
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    public RestTemplate getRestTemplate() {
        return super.rest;
    }

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUser(long userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        return get("/{userId}", userId, parameters);
    }

    public ResponseEntity<Object> createUser(@Valid @RequestBody NewUserDto newUserDto) {
        return post("", newUserDto);
    }

    public ResponseEntity<Object> updateUser(long userId, @Valid @RequestBody UpdateUserDto updateUserDto) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        return patch("/{userId}", userId, parameters, updateUserDto);
    }

    public ResponseEntity<Void> deleteUser(long userId) {
        ResponseEntity<Object> response = delete("/{userId}", userId,
                Map.of("userId", userId));
        return ResponseEntity.status(response.getStatusCode()).build();
    }
}
