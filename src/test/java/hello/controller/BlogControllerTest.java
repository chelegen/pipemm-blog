package hello.controller;

import hello.entity.User;
import hello.service.AuthService;
import hello.service.BlogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BlogControllerTest {
    private MockMvc mvc;
    @Mock
    AuthService authService;
    @Mock
    BlogService blogService;

    @BeforeEach
    void setMvc() {
        mvc = MockMvcBuilders.standaloneSetup(new BlogController(blogService, authService)).build();
    }

    @Test
    void requireLoginBeforeProceeding() throws Exception {
        mvc.perform(post("/blog"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("登录后才能操作")));
    }

    @Test
    void invalidRequestIfTitleIsEmpty() throws Exception {
        Mockito.when(authService.getCurrentUser()).thenReturn(Optional.of(new User(1, "mockUser", "")));
        mvc.perform(post("/blog"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("标题错误")));
    }   // authService 空指针异常。。。。。。
}