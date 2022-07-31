package com.musseukpeople.woorimap.auth.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.common.exception.ErrorResponse;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class AuthControllerTest extends AcceptanceTest {

    @BeforeEach
    void setUp() throws Exception {
        회원가입(new SignupRequest("woorimap@gmail.com", "!Hwan123", "hwan"));
    }

    @DisplayName("로그인 성공")
    @Test
    void signIn_success() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        SignInRequest signInRequest = new SignInRequest(email, password);

        // when
        MockHttpServletResponse response = 로그인(signInRequest);

        // then
        TokenResponse tokenResponse = getResponseObject(response, TokenResponse.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(tokenResponse.getAccessToken()).isNotBlank(),
            () -> assertThat(tokenResponse.getRefreshToken()).isNotBlank(),
            () -> assertThat(tokenResponse.getMember().getEmail()).isEqualTo("woorimap@gmail.com"),
            () -> assertThat(tokenResponse.getMember().getNickName()).isEqualTo("hwan")
        );
    }

    @DisplayName("저장되지 않은 이메일로 인한 로그인 실패")
    @Test
    void signIn_notFoundEmail_fail() throws Exception {
        // given
        String email = "invalid@gmail.com";
        String password = "!Hwan123";

        // when
        MockHttpServletResponse response = 로그인(new SignInRequest(email, password));

        // then
        로그인_실패(response);
    }

    @DisplayName("비밀번호 불일치로 인한 로그인 실패")
    @Test
    void signIn_invalidPassword_fail() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "invalidPassword";

        // when
        MockHttpServletResponse response = 로그인(new SignInRequest(email, password));

        // then
        로그인_실패(response);
    }

    @DisplayName("로그아웃 성공")
    @Test
    void logout_success() throws Exception {
        // given
        String email = "woorimap@gmail.com";
        String password = "!Hwan123";
        String accessToken = 로그인_토큰(new SignInRequest(email, password));

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/signout")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 로그인_실패(MockHttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = getErrorResponse(response);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
            () -> assertThat(errorResponse.getMessage()).isEqualTo("이메일 또는 비밀번호가 일치하지 않습니다."),
            () -> assertThat(errorResponse.getCode()).isEqualTo("U001")
        );
    }
}