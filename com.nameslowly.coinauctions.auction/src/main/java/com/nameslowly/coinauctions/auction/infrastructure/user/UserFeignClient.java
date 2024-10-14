package com.nameslowly.coinauctions.auction.infrastructure.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-server", fallbackFactory = UserFallbackFactory.class)
@Primary
public interface UserFeignClient extends UserService {

    @GetMapping("/api/internal/users/{username}")
    UserDto getUser(@PathVariable("username") String username);
}
