package br.com.claudioescobar.blogsample.controller;

import br.com.claudioescobar.blogsample.domain.BlogPost;
import br.com.claudioescobar.blogsample.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/blog_posts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping
    public List<BlogPost> findAll() {
        return blogPostService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogPost save(@RequestBody BlogPost blogPost) {
        return blogPostService.save(blogPost);
    }

    @PutMapping("/{id}")
    public BlogPost update(@PathVariable("id") Long id, @RequestBody BlogPost blogPost) {
        blogPost.setId(id);
        return blogPostService.update(blogPost);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        blogPostService.delete(id);
    }

}
