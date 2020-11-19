package ru.netology.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PostRepository {
    private static AtomicInteger postId = new AtomicInteger(0);
    ConcurrentMap<Long, Post> postRepository;

    public PostRepository() {
        this.postRepository = new ConcurrentHashMap<>();
    }

    public ConcurrentMap<Long, Post> all() {
        return postRepository;
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postRepository.get(id));
    }

    public Post save(Post post) {
        long id = post.getId();

        if (id == 0) {
            newPost(id, post);
            return post;
        }

        Optional.ofNullable(postRepository.get(id))
                .ifPresentOrElse(oldPost -> {if (oldPost.isAlive()) replacePost(id, post);},
                        () -> newPost(id, post));
        return post;
    }

    public boolean removeById(long id) {
        if (postRepository.containsKey(id)) {
            postRepository.get(id).setAlive(false);
            return true;
        }
        return false;
    }

    public void newPost(long id, Post post) {
        id = postId.incrementAndGet();
        post.setId(id);
        post.setAlive(true);
        postRepository.put(id, post);
    }

    public void replacePost(long id, Post post) {
        post.setAlive(true);
        postRepository.replace(id, post);
    }
}
