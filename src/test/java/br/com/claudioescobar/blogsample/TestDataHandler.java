package br.com.claudioescobar.blogsample;

import br.com.claudioescobar.blogsample.domain.BlogPost;

import static br.com.claudioescobar.blogsample.TestUtil.parseToLocalDateTime;

public interface TestDataHandler {

    BlogPost.BlogPostBuilder BLOG_POST_DATA1_BUILDER = BlogPost.builder()
            .title("Title 1")
            .description("Description 1")
            .publishDate(parseToLocalDateTime("2018-07-01T10:25:22"));

    BlogPost.BlogPostBuilder BLOG_POST_DATA2_BUILDER = BlogPost.builder()
            .title("Title 2")
            .description("Description 2")
            .publishDate(parseToLocalDateTime("2018-07-01T11:25:22"));

}
