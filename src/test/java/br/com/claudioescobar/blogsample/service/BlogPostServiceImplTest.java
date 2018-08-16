package br.com.claudioescobar.blogsample.service;

import br.com.claudioescobar.blogsample.domain.BlogPost;
import br.com.claudioescobar.blogsample.repository.BlogPostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static br.com.claudioescobar.blogsample.TestDataHandler.blogData1;
import static br.com.claudioescobar.blogsample.TestDataHandler.blogData2;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BlogPostServiceImplTest {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public BlogPostService blogPostService() {
            return new BlogPostServiceImpl();
        }
    }

    @Autowired
    private BlogPostService blogPostService;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @Test
    public void shouldFindAll() {
        List<BlogPost> expectedBlogPosts = Arrays.asList(blogData1().build(), blogData2().build());
        when(blogPostRepository.findAll()).thenReturn(expectedBlogPosts);

        List<BlogPost> result = blogPostService.findAll();

        assertThat(result, equalTo(expectedBlogPosts));
    }

    @Test
    public void shouldSave() {
        BlogPost blogPostToSave = blogData1().build();
        BlogPost expectedBlogPost = blogData1().id(1L).build();
        when(blogPostRepository.save(blogPostToSave)).thenReturn(expectedBlogPost);

        BlogPost result = blogPostService.save(blogPostToSave);

        assertThat(result, equalTo(expectedBlogPost));
    }

    @Test
    public void shouldUpdate() {
        BlogPost blogPostToUpdate = blogData1().id(1L).build();
        BlogPost expectedBlogPost = blogData1().id(1L).build();
        when(blogPostRepository.save(blogPostToUpdate)).thenReturn(expectedBlogPost);

        BlogPost result = blogPostService.update(blogPostToUpdate);

        assertThat(result, equalTo(expectedBlogPost));
    }

    @Test
    public void shouldDeleteById() {
        BlogPost blogPostToUpdate = blogData1().id(1L).build();
        BlogPost expectedBlogPost = blogData1().id(1L).build();
        when(blogPostRepository.save(blogPostToUpdate)).thenReturn(expectedBlogPost);

        BlogPost result = blogPostService.update(blogPostToUpdate);

        assertThat(result, equalTo(expectedBlogPost));
    }

}