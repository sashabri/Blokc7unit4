package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.Config;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private static final String GET = "GET";
  private static final String POST = "POST";
  private static final String DELETE = "DELETE";
  private static final String PATH_WITH_OUT_ID = "/api/posts";
  private static final String PATH_WITH_ID = "/api/posts/\\d+";
  private PostController controller;

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext(Config.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(GET) && path.equals(PATH_WITH_OUT_ID)) {
        controller.all(resp);
        return;
      }
      if (method.equals(GET) && path.matches(PATH_WITH_ID)) {
        // easy way
        final var id = fillId(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST) && path.equals(PATH_WITH_OUT_ID)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE) && path.matches(PATH_WITH_ID)) {
        // easy way
        final var id = fillId(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
  private Long fillId(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/")));
  }
}

