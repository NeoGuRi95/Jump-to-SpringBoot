package com.ll.exam.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private int increaseCnt = 0;

    @RequestMapping("/sbb")
    @ResponseBody // URL 요청에 대한 응답으로 문자열을 리턴하라는 의미이다.
    public String index() {
        return "hello :)";
    }

    @GetMapping("/page1")
    @ResponseBody
    public String showPage1() {
        return """
                <form method="POST" action="/page2">
                    <input type="number" name="age" placeholder="나이" />
                    <input type="submit" value="page2로 POST 방식으로 이동" />
                </form>
                """;
    }

    @PostMapping("/page2")
    @ResponseBody
    public String showPage2Post(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/page2")
    @ResponseBody
    public String showPage2Get(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, GET 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/plus")
    @ResponseBody
    public int showPlus(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        return a + b;
    }

    @GetMapping("/minus")
    @ResponseBody
    public int showMinus(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        return a - b;
    }

    @GetMapping("/increase")
    @ResponseBody
    public int showIncrease() {
        ++increaseCnt;
        return increaseCnt;
    }
}
