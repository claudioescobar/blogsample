package br.com.claudioescobar.blogsample.controller;

import br.com.claudioescobar.blogsample.domain.BlogPost;
import br.com.claudioescobar.blogsample.service.BlogPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static br.com.claudioescobar.blogsample.TestDataHandler.blogData1;
import static br.com.claudioescobar.blogsample.TestDataHandler.blogData2;
import static br.com.claudioescobar.blogsample.TestUtil.formatLocalDateTime;
import static br.com.claudioescobar.blogsample.TestUtil.toJson;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogPostController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class BlogPostControllerTest {

    private static final long BLOG_POST_ID_1 = 1L;
    private static final long BLOG_POST_ID_2 = 2L;
    private static final long BLOG_POST_UNEXISTENT_ID = 3L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogPostService service;

    @TestConfiguration
    static class CustomizationConfiguration implements RestDocsMockMvcConfigurationCustomizer {

        @Override
        public void customize(MockMvcRestDocumentationConfigurer configurer) {
            configurer.operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint());
        }

        @Bean
        public RestDocumentationResultHandler restDocumentation() {
            return MockMvcRestDocumentation.document("{method-name}");
        }
    }

    @Test
    public void whenFindAllBlogPosts_thenReturnAllBlogPosts() throws Exception {
        BlogPost blogPost1 = blogData1().id(BLOG_POST_ID_1).build();
        BlogPost blogPost2 = blogData2().id(BLOG_POST_ID_2).build();
        List<BlogPost> blogPosts = Arrays.asList(blogPost1, blogPost2);

        given(service.findAll()).willReturn(blogPosts);

        mvc.perform(MockMvcRequestBuilders.get("/api/blog_posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("blog_posts"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(blogPost1.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(blogPost1.getTitle())))
                .andExpect(jsonPath("$[0].description", is(blogPost1.getDescription())))
                .andExpect(jsonPath("$[0].publishDate", is(formatLocalDateTime(blogPost1.getPublishDate()))))
                .andExpect(jsonPath("$[1].id", is(blogPost2.getId().intValue())))
                .andExpect(jsonPath("$[1].title", is(blogPost2.getTitle())))
                .andExpect(jsonPath("$[1].description", is(blogPost2.getDescription())))
                .andExpect(jsonPath("$[1].publishDate", is(formatLocalDateTime(blogPost2.getPublishDate()))));

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void givenTheBlogPost_whenSave_thenReturnTheBlogPost() throws Exception {
        BlogPost savedBlogPost = blogData1().id(BLOG_POST_ID_1).build();
        BlogPost blogPostToSave = blogData1().build();

        given(service.save(blogPostToSave)).willReturn(savedBlogPost);

        mvc.perform(MockMvcRequestBuilders.post("/api/blog_posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(blogPostToSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(savedBlogPost.getId().intValue())))
                .andExpect(jsonPath("$.title", is(savedBlogPost.getTitle())))
                .andExpect(jsonPath("$.description", is(savedBlogPost.getDescription())))
                .andExpect(jsonPath("$.publishDate", is(formatLocalDateTime(savedBlogPost.getPublishDate()))));

        verify(service, times(1)).save(blogPostToSave);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void givenTheBlogPostAndAnExistentId_whenUpdate_thenReturnTheBlogPost() throws Exception {
        BlogPost blogPost = blogData1().id(BLOG_POST_ID_1).build();

        given(service.update(blogPost)).willReturn(blogPost);

        mvc.perform(MockMvcRequestBuilders.put("/api/blog_posts/" + BLOG_POST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(blogData1().build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(blogPost.getId().intValue())))
                .andExpect(jsonPath("$.title", is(blogPost.getTitle())))
                .andExpect(jsonPath("$.description", is(blogPost.getDescription())))
                .andExpect(jsonPath("$.publishDate", is(formatLocalDateTime(blogPost.getPublishDate()))));

        verify(service, times(1)).update(blogPost);
        verifyNoMoreInteractions(service);
    }

//    @Test
//    public void givenTheBlogPostAndAnNonExistentId_whenUpdate_thenShouldReturnA404NotFound() throws Exception {
//        BlogPost blogPost = blogData1().id(BLOG_POST_UNEXISTENT_ID).build();
//
//        given(service.update(blogPost)).willReturn(blogPost);
//
//        mvc.perform(MockMvcRequestBuilders.put("/api/blog_posts/" + BLOG_POST_UNEXISTENT_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(toJson(blogData1().build())))
//                .andExpect(status().isNotFound());
//
//        verify(service, times(1)).update(blogPost);
//        verifyNoMoreInteractions(service);
//    }

    @Test
    public void givenTheBlogPostId_whenDelete_thenReturnTheStatus200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/blog_posts/" + BLOG_POST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(blogData1().build())))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(BLOG_POST_ID_1);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void givenNonExistentBlogPostId_whenDelete_thenReturnTheStatus404() throws Exception {
        doThrow(new EmptyResultDataAccessException(0)).when(service).delete(BLOG_POST_ID_1);

        mvc.perform(MockMvcRequestBuilders.delete("/api/blog_posts/" + BLOG_POST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(blogData1().build())))
                .andExpect(status().isNotFound());

        verify(service, times(1)).delete(BLOG_POST_ID_1);
        verifyNoMoreInteractions(service);
    }

}