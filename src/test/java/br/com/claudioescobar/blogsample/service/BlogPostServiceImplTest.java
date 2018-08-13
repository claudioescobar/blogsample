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

import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA1_BUILDER;
import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA2_BUILDER;
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
        List<BlogPost> expectedBlogPosts = Arrays.asList(BLOG_POST_DATA1_BUILDER.build(), BLOG_POST_DATA2_BUILDER.build());
        when(blogPostRepository.findAll()).thenReturn(expectedBlogPosts);

        List<BlogPost> result = blogPostService.findAll();

        assertThat(result, equalTo(expectedBlogPosts));
    }

    @Test
    public void shouldSave() {
        BlogPost blogPostToSave = BLOG_POST_DATA1_BUILDER.build();
        BlogPost expectedBlogPost = BLOG_POST_DATA1_BUILDER.id(1L).build();
        when(blogPostRepository.save(blogPostToSave)).thenReturn(expectedBlogPost);

        BlogPost result = blogPostService.save(blogPostToSave);

        assertThat(result, equalTo(expectedBlogPost));
    }

    @Test
    public void shouldUpdate() {
        BlogPost blogPostToUpdate = BLOG_POST_DATA1_BUILDER.id(1L).build();
        BlogPost expectedBlogPost = BLOG_POST_DATA1_BUILDER.id(1L).build();
        when(blogPostRepository.save(blogPostToUpdate)).thenReturn(expectedBlogPost);

        BlogPost result = blogPostService.update(blogPostToUpdate);

        assertThat(result, equalTo(expectedBlogPost));
    }

    @Test
    public void shouldDeleteById() {
        BlogPost blogPostToUpdate = BLOG_POST_DATA1_BUILDER.id(1L).build();
        BlogPost expectedBlogPost = BLOG_POST_DATA1_BUILDER.id(1L).build();
        when(blogPostRepository.save(blogPostToUpdate)).thenReturn(expectedBlogPost);

        BlogPost result = blogPostService.update(blogPostToUpdate);

        assertThat(result, equalTo(expectedBlogPost));
    }

}