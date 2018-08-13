package br.com.claudioescobar.blogsample.controller;

import br.com.claudioescobar.blogsample.BlogsampleApplication;
import br.com.claudioescobar.blogsample.domain.BlogPost;
import br.com.claudioescobar.blogsample.repository.BlogPostRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA1_BUILDER;
import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA2_BUILDER;
import static br.com.claudioescobar.blogsample.TestUtil.formatLocalDateTime;
import static br.com.claudioescobar.blogsample.TestUtil.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = BlogsampleApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class BlogPostControllerIntegrationTest {

    private static final long BLOG_POST_UNEXISTENT_ID = 3L;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BlogPostRepository repository;

    @After
    public void after() {
        repository.deleteAllInBatch();
    }

    @Test
    public void whenFindAllBlogPosts_thenReturnAllBlogPosts() throws Exception {
        BlogPost blogPost1 = repository.save(BLOG_POST_DATA1_BUILDER.build());
        BlogPost blogPost2 = repository.save(BLOG_POST_DATA2_BUILDER.build());

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
    }

    @Test
    public void givenTheBlogPost_whenSave_thenReturnTheBlogPost() throws Exception {
        BlogPost blogPost = BLOG_POST_DATA1_BUILDER.build();

        mvc.perform(MockMvcRequestBuilders.post("/api/blog_posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(BLOG_POST_DATA1_BUILDER.build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is(blogPost.getTitle())))
                .andExpect(jsonPath("$.description", is(blogPost.getDescription())))
                .andExpect(jsonPath("$.publishDate", is(formatLocalDateTime(blogPost.getPublishDate()))));
    }

    @Test
    public void givenTheBlogPostAndAnExistentId_whenUpdate_thenReturnTheBlogPost() throws Exception {
        BlogPost savedBlogPost = repository.save(BLOG_POST_DATA1_BUILDER.build());
        BlogPost blogPostToSave = BLOG_POST_DATA2_BUILDER.build();
        BlogPost updatedBlogPostToSave = BLOG_POST_DATA2_BUILDER.id(savedBlogPost.getId()).build();

        mvc.perform(MockMvcRequestBuilders.put("/api/blog_posts/" + savedBlogPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(blogPostToSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedBlogPost.getId().intValue())))
                .andExpect(jsonPath("$.title", is(blogPostToSave.getTitle())))
                .andExpect(jsonPath("$.description", is(blogPostToSave.getDescription())))
                .andExpect(jsonPath("$.publishDate", is(formatLocalDateTime(blogPostToSave.getPublishDate()))));

        assertThat(repository.findById(savedBlogPost.getId()), equalTo(Optional.of(updatedBlogPostToSave)));
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
        BlogPost savedBlogPost = repository.save(BLOG_POST_DATA1_BUILDER.build());

        mvc.perform(MockMvcRequestBuilders.delete("/api/blog_posts/" + savedBlogPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(BLOG_POST_DATA1_BUILDER.build())))
                .andExpect(status().isOk());

        assertThat(repository.findById(savedBlogPost.getId()), equalTo(Optional.ofNullable(null)));
    }

    @Test
    public void givenNonExistentBlogPostId_whenDelete_thenReturnTheStatus404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/blog_posts/" + BLOG_POST_UNEXISTENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(BLOG_POST_DATA1_BUILDER.build())))
                .andExpect(status().isNotFound());
    }

}
