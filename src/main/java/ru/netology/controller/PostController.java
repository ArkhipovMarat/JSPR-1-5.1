package ru.netology.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/posts")
public class PostController {
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ConcurrentMap<Long, Post> all() throws IOException {
    return service.all();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public Post getById(@PathVariable long id) throws IOException {
    return service.getById(id);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ResponseEntity<Post> save(@RequestBody Post post, UriComponentsBuilder componentsBuilder) throws IOException {
    Post result = service.save(post);
    var uri = componentsBuilder.path("/api/posts" + result.getId()).build().toUri();
    return ResponseEntity.created(uri).body(result);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  public void removeById(@PathVariable long id) throws IOException {
    service.removeById(id);
  }
}
