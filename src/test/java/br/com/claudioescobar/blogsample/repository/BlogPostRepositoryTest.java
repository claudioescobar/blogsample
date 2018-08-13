package br.com.claudioescobar.blogsample.repository;

import br.com.claudioescobar.blogsample.domain.BlogPost;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA1_BUILDER;
import static br.com.claudioescobar.blogsample.TestDataHandler.BLOG_POST_DATA2_BUILDER;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Ignore("NEED TO FIX DETACHED ENTITY EXCEPTION FOR TEST DATA")
public class BlogPostRepositoryTest {

    private static final long NON_EXISTENT_BLOG_POST_ID = 100L;
    private static final String CHANGED_TITLE = "changedTitle";
    private static final String CHANGED_DESCRIPTION = "changedDescription";
    private static final LocalDateTime CHANGED_DATE = LocalDateTime.now();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BlogPostRepository repository;

    @Test
    public void givenTwoBlogPosts_whenFindAll_shouldBringTheTwoBlogPosts() {
        BlogPost blogPost1 = entityManager.persist(BLOG_POST_DATA1_BUILDER.build());
        BlogPost blogPost2 = entityManager.persist(BLOG_POST_DATA2_BUILDER.build());
        entityManager.flush();

        List<BlogPost> expectedBlogPosts = Arrays.asList(blogPost1, blogPost2);

        List<BlogPost> result = repository.findAll();

        assertThat(result, equalTo(expectedBlogPosts));
    }

    @Test
    public void whenFindAll_shouldNotBringBlogPosts() {
        List<BlogPost> result = repository.findAll();
        assertThat(result, hasSize(0));
    }

    @Test
    public void givenANotPersistedBlogPost_whenSave_shouldPersistTheBlogPost() {
        BlogPost result = repository.save(BLOG_POST_DATA1_BUILDER.build());

        BlogPost savedBlogPost = entityManager.find(BlogPost.class, result.getId());
        BlogPost expectedBlogPost = BLOG_POST_DATA1_BUILDER.build();

        assertThat(result, equalTo(savedBlogPost));
        expectedBlogPost.setId(savedBlogPost.getId());
        assertThat(result, equalTo(expectedBlogPost));
    }

//    @Test
//    public void givenAPersistedBlogPost_whenUpdate_shouldPersistTheChanges() {
//        BlogPost blogPost = entityManager.persistAndFlush(BLOG_POST_DATA1_BUILDER.build());
//        blogPost.setTitle(CHANGED_TITLE);
//        blogPost.setDescription(CHANGED_DESCRIPTION);
//        blogPost.setPublishDate(CHANGED_DATE);
//
//        BlogPost expectedBlogPost = BLOG_POST_DATA1_BUILDER.build();
//        expectedBlogPost.setTitle(CHANGED_TITLE);
//        expectedBlogPost.setDescription(CHANGED_DESCRIPTION);
//        expectedBlogPost.setPublishDate(CHANGED_DATE);
//
//        BlogPost result = repository.save(blogPost);
//
//        BlogPost updatedBlogPost = entityManager.find(BlogPost.class, result.getId());
//
//        assertThat(result, equalTo(updatedBlogPost));
//        expectedBlogPost.setId(updatedBlogPost.getId());
//        assertThat(result, equalTo(expectedBlogPost));
//    }

    @Test
    public void givenAnExistentId_whenDeleteById_shouldDeleteWithSuccess() {
        BlogPost blogPostToDelete = entityManager.persist(BLOG_POST_DATA1_BUILDER.build());
        repository.deleteById(blogPostToDelete.getId());

        BlogPost deletedBlogPost = entityManager.find(BlogPost.class, blogPostToDelete.getId());
        assertThat(deletedBlogPost, nullValue());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void givenANoExistentId_WhenDeleteById_ShouldThrowAnException() {
        repository.deleteById(NON_EXISTENT_BLOG_POST_ID);
    }

}