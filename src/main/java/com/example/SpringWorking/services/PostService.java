package com.example.SpringWorking.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.SpringWorking.models.Post;
import com.example.SpringWorking.repositories.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postrepo;

    public Optional<Post> getById(Long id){
        return postrepo.findById(id);
    }

    public List<Post> findAll(){
        return postrepo.findAll();
    }

    // Overloaded methid to enable pagination.
    public Page<Post> findAll(int offset,int pageSize,String field){
        return postrepo.findAll(PageRequest.of(offset, pageSize).withSort(Direction.ASC, field));

        
    }

    public void delete(Post post){
        postrepo.delete(post);
    }

    public Post save(Post post){
        if(post.getId() == null){
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postrepo.save(post);
    }
    
}
