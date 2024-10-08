package com.nameslowly.coinauctions.auth.infrastructure.security;

import com.nameslowly.coinauctions.auth.application.dto.UserDto;
import com.nameslowly.coinauctions.auth.application.mapper.UserMapper;
import com.nameslowly.coinauctions.auth.domain.model.User;
import com.nameslowly.coinauctions.auth.domain.repository.UserRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "UserDetailsServiceImpl 실행")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserMapper userMapper;

    /**
     * redis 에서 캐싱하여 User 정보를 가져오거나 존재하지 않는다면 RDBMS에서 가져와 redis에 저장
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // redis 에서 캐싱해온 user 정보
        UserDto userDTO = (UserDto) redisTemplate.opsForValue().get(username);
        log.info("캐시된 User = {}", userDTO);

        // 캐시 데이터가 없으면 DB 조회후 캐싱
        if (userDTO == null) {
            User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

            userDTO = userMapper.userToUserDto(user);

            redisTemplate.opsForValue().set(username, userDTO, 1, TimeUnit.HOURS);
        }

        // UserDetailsImpl 을 반환해 authentication 객체를 SecurityContext에 저장
        return new UserDetailsImpl(userMapper.userDtoToUser(userDTO));
    }


    /**
     * 권한 업데이트시 캐시 삭제 (UserDetailService 에서 처리하여 캐시 관리 책임을 집중화시킴)
     */
    public void removeUserFromCache(String username) {
        redisTemplate.delete(username);
    }
}
