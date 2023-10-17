package ru.netology.repository;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepository {
  private static final String NOT_FOUND_MESSAGE = "Такого поста не существует.";
  private AtomicLong counter = new AtomicLong(1);
  private Map<Long, Post> listPosts = new ConcurrentHashMap<>();
  public List<Post> all() {
    return new ArrayList<>(listPosts.values());
  }

  public Optional<Post> getById(Long id) {
      return Optional.of(listPosts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long id = counter.getAndIncrement();
      post.setId(id);
      listPosts.put(id, post);
      return post;
    } else {
      if (listPosts.containsKey(post.getId())) {
        listPosts.put(post.getId(), post);
        return post;
      }
      throw new NotFoundException(NOT_FOUND_MESSAGE);
    }
  }

  public void removeById(Long id) {
    listPosts.remove(id);
  }
}
