package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  private static final String NOT_FOUND_MESSAGE = "Такого поста не существует.";
  private AtomicLong counter = new AtomicLong(1);
  private Map<Long, Post> listPosts = new ConcurrentHashMap<>();
  public List<Post> all() {
    return (List<Post>) listPosts.values();
  }

  public Optional<Post> getById(long id) {
      return Optional.of(listPosts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      listPosts.put(counter.longValue(), post);
      counter.getAndIncrement();
      return post;
    } else {
      if (listPosts.containsKey(post.getId())) {
        listPosts.put(post.getId(), post);
        return post;
      }
      throw new NotFoundException(NOT_FOUND_MESSAGE);
    }
  }

  public void removeById(long id) {
    listPosts.remove(id);
  }
}
