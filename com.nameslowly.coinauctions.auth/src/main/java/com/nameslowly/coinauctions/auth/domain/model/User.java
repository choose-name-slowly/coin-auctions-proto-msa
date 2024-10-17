package com.nameslowly.coinauctions.auth.domain.model;

import com.nameslowly.coinauctions.common.shared.BaseEntity;
import com.nameslowly.coinauctions.common.shared.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "p_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)  // This will store the enum name as a string in the database
    @Column(name = "role")
    private UserRole role;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    // 유저 생성 메서드
    public static User create(String username, String password, UserRole role
       ) {

        User user = new User();
        user.username = username;
        user.password = password;
        user.role = role;

        return user;
    }

    // 회원정보 수정 메서드

    // 비밀번호 업데이트 메서드
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    // 회원권한 변경 메서드
    public void updateRole(UserRole role) {
        this.role = role;
    }

}