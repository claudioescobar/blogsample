package br.com.claudioescobar.blogsample.controller;

import br.com.claudioescobar.blogsample.domain.BlogPost;
import br.com.claudioescobar.blogsample.service.BlogPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA1_BUILDER;
import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA2_BUILDER;
import static br.com.claudioescobar.blogsample.TestUtil.formatLocalDateTime;
import static br.com.claudioescobar.blogsample.TestUtil.toJson;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogPostController.class)
public class BlogPostControllerTest {

    private static final long BLOG_POST_ID_1 = 1L;
    private static final long BLOG_POST_ID_2 = 2L;
    private static final long BLOG_POST_UNEXISTENT_ID = 3L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogPostService service;

    @Test
    public void whenFindAllBlogPosts_thenReturnAllBlogPosts() throws Exception {
        BlogPost blogPost1 = BLOG_POST_DATA1_BUILDER.id(BLOG_POST_ID_1).build();
        BlogPost blogPost2 = BLOG_POST_DATA2_BUILDER.id(BLOG_POST_ID_2).build();
        List<BlogPost> blogPosts = Arrays.asList(blogPost1, blogPost2);

        given(service.findAll()).willReturn(blogPosts);

        mvc.perform(MockMvcRequestBuilders.get("/api/blog_posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
        BlogPost savedBlogPost = BLOG_POST_DATA1_BUILDER.id(BLOG_POST_ID_1).build();
        BlogPost blogPostToSave = BLOG_POST_DATA1_BUILDER.build();

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
        BlogPost blogPost = BLOG_POST_DATA1_BUILDER.id(BLOG_POST_ID_1).build();

        given(service.update(blogPost)).willReturn(blogPost);

        mvc.perform(MockMvcRequestBuilders.put("/api/blog_posts/" + BLOG_POST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(BLOG_POST_DATA1_BUILDER.build())))
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
//        BlogPost blogPost = BLOG_POST_DATA1_BUILDER.id(BLOG_POST_UNEXISTENT_ID).build();
//
//        given(service.update(blogPost)).willReturn(blogPost);
//
//        mvc.perform(MockMvcRequestBuilders.put("/api/blog_posts/" + BLOG_POST_UNEXISTENT_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(toJson(BLOG_POST_DATA1_BUILDER.build())))
//                .andExpect(status().isNotFound());
//
//        verify(service, times(1)).update(blogPost);
//        verifyNoMoreInteractions(service);
//    }

    @Test
    public void givenTheBlogPostId_whenDelete_thenReturnTheStatus200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/blog_posts/" + BLOG_POST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(BLOG_POST_DATA1_BUILDER.build())))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(BLOG_POST_ID_1);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void givenNonExistentBlogPostId_whenDelete_thenReturnTheStatus404() throws Exception {
        doThrow(new EmptyResultDataAccessException(0)).when(service).delete(BLOG_POST_ID_1);

        mvc.perform(MockMvcRequestBuilders.delete("/api/blog_posts/" + BLOG_POST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(BLOG_POST_DATA1_BUILDER.build())))
                .andExpect(status().isNotFound());

        verify(service, times(1)).delete(BLOG_POST_ID_1);
        verifyNoMoreInteractions(service);
    }

}