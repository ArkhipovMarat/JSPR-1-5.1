package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class PostService {
  private final PostRepository repository;

  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public ConcurrentMap<Long,Post> all() {
    ConcurrentMap<Long,Post> resultMap = repository.all().entrySet().stream()
            .filter(value -> value.getValue().isAlive())
            .collect(Collectors.toConcurrentMap(value -> value.getKey(), value -> value.getValue()));
    return resultMap;
  }

  public Post getById(long id) {
    Post result = repository.getById(id).orElseThrow(NotFoundException::new);
    if (!result.isAlive()) throw new NotFoundException();
    return result;
  }

  public Post save(Post post) {
    Post result = repository.save(post);
    if (!result.isAlive()) throw new NotFoundException();
    return result;
  }

  public void removeById(long id) {
    if (!repository.removeById(id)) throw new NotFoundException();
  }
}

