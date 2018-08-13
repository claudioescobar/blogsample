package br.com.claudioescobar.blogsample.service;

import br.com.claudioescobar.blogsample.domain.BlogPost;

import java.util.List;

public interface BlogPostService {

    List<BlogPost> findAll();

    BlogPost save(BlogPost blogPost);

    BlogPost update(BlogPost blogPost);

    void delete(Long id);

}
