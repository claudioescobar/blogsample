package br.com.claudioescobar.blogsample;

import br.com.claudioescobar.blogsample.domain.BlogPost;

import static br.com.claudioescobar.blogsample.TestUtil.parseToLocalDateTime;

public class TestDataHandler {

    public static BlogPost.BlogPostBuilder blogData1() {
        return BlogPost.builder()
                .title("Title 1")
                .description("Description 1")
                .publishDate(parseToLocalDateTime("2018-07-01T10:25:22"));
    }

    public static BlogPost.BlogPostBuilder blogData2() {
        return BlogPost.builder()
                .title("Title 2")
                .description("Description 2")
                .publishDate(parseToLocalDateTime("2018-07-01T11:25:22"));
    }

}
