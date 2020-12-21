package club.neters.blogspring.controller;
import club.neters.blogspring.annotation.Authorize;
import club.neters.blogspring.bean.Blog;
import club.neters.blogspring.utils.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Authorize
    @GetMapping()
    public Object getBlogList() {
        return ResponseUtil.response(new Blog(1,"hello"));
    }
}
