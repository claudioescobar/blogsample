package br.com.claudioescobar.blogsample.service;

import br.com.claudioescobar.blogsample.domain.BlogPost;
import br.com.claudioescobar.blogsample.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Override
    public List<BlogPost> findAll() {
        return blogPostRepository.findAll();
    }

    @Override
    public BlogPost save(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }

    @Override
    public BlogPost update(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }

    @Override
    public void delete(Long id) {
        blogPostRepository.deleteById(id);
    }

}
