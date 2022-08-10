package com.ll.exam.sbb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private int increaseCnt = 0;

    @RequestMapping("/sbb")
    @ResponseBody // URL 요청에 대한 응답으로 문자열을 리턴하라는 의미이다.
    public String index() {
        return "hello :)";
    }

    @GetMapping("page1")
    @ResponseBody
    public String showPage1() {
        return """
                <form method="POST" action="/page2">
                    <input type="number" name="age" placeholder="나이" />
                    <input type="submit" value="page2로 POST 방식으로 이동" />
                </form>
                """;
    }

    @PostMapping("page2")
    @ResponseBody
    public String showPage2Post(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("page2")
    @ResponseBody
    public String showPage2Get(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, GET 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("plus")
    @ResponseBody
    public int showPlus(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        return a + b;
    }

    @GetMapping("minus")
    @ResponseBody
    public int showMinus(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        return a - b;
    }

    @GetMapping("increase")
    @ResponseBody
    public int showIncrease() {
        ++increaseCnt;
        return increaseCnt;
    }

    @GetMapping("gugudan")
    @ResponseBody
    public String showGugudan(int dan, int limit) {
        String result = "";
        int i = 1;

        while (i <= limit) {
            result += "<h3>" + dan + " * " + i + " = " + (dan * i) + "</h3>";
            i++;
        }

        return result;
    }

    @GetMapping("mbti/{name}")
    @ResponseBody
    public String showMbti(@PathVariable String name) {
        String rs = switch (name) {
            case "홍길동" -> "INFP";
            case "홍길순", "임꺽정" -> "ENFP";
            default -> "모름";
        };
        return rs;
    }

    @GetMapping("plus2")
    @ResponseBody
    public void showPlus2(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));

        resp.getWriter().append(a + b + "");
    }

    @GetMapping("saveSession/{name}/{value}")
    @ResponseBody
    public String saveSession(@PathVariable String name, @PathVariable String value, HttpServletRequest req) {
        HttpSession session = req.getSession();

        session.setAttribute(name, value);

        return "세션변수 %s의 값이 %s(으)로 설정되었습니다.".formatted(name, value);
    }

    @GetMapping("getSession/{name}")
    @ResponseBody
    public String getSession(@PathVariable String name, HttpSession session) {
        String value = (String) session.getAttribute(name);

        return "세션변수 %s의 값이 %s 입니다.".formatted(name, value);
    }

    private List<Article> articles = new ArrayList<>();

    @GetMapping("addArticle")
    @ResponseBody
    public String addArticle(String title, String body) {
        Article article = new Article(title, body);

        articles.add(article);

        return "%d번 게시물이 생성되었습니다.".formatted(article.getId());
    }

    @GetMapping("article/{id}")
    @ResponseBody
    public Article getArticle(@PathVariable int id) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);

        return article;
    }

    @GetMapping("modifyArticle")
    @ResponseBody
    public String modifyArticle(int id, String title, String body) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);
        article.setTitle(title);
        article.setBody(body);

        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }

        return "%d번 게시물이 수정되었습니다.".formatted(id);
    }

    @GetMapping("/deleteArticle/{id}")
    @ResponseBody
    public String deleteArticle(@PathVariable int id) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);

        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }

        articles.remove(article);

        return "%d번 게시물을 삭제하였습니다.".formatted(article.getId());
    }
}

@AllArgsConstructor
@Getter
@Setter
class Article {
    private static int lastId = 0;
    private final int id;
    private String title;
    private String body;

    public Article(String title, String body) {
        this(++lastId, title, body);
    }
}
