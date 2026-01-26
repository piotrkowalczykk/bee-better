package com.kowal.backend.integration;

import com.kowal.backend.acrticle.dto.response.ArticlesResponse;
import com.kowal.backend.acrticle.service.ArticleService;
import com.kowal.backend.customer.controller.CustomerController;
import com.kowal.backend.customer.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private ArticleService articleService;

    @Test
    @WithMockUser(username = "test@mail.com")
    @DisplayName("Should return all articles for authenticated customer")
    void shouldReturnAllArticlesForCustomer() throws Exception {
        ArticlesResponse article1 = new ArticlesResponse();
        article1.setTitle("Title 1");
        article1.setDescription("Desc 1");

        ArticlesResponse article2 = new ArticlesResponse();
        article2.setTitle("Title 2");
        article2.setDescription("Desc 2");

        when(articleService.getAllArticles("test@mail.com"))
                .thenReturn(List.of(article1, article2));

        mockMvc.perform(get("/customer/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].title").value("Title 2"));
    }

    @Test
    @DisplayName("Should return 401 Unauthorized when user is not authenticated")
    void shouldReturn401WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/customer/articles"))
                .andExpect(status().isUnauthorized());
    }
}
