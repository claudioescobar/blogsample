package br.com.claudioescobar.blogsample.repository;

import br.com.claudioescobar.blogsample.domain.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
