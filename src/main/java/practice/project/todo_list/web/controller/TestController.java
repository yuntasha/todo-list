package practice.project.todo_list.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import practice.project.todo_list.global.response.SuccessResponseDto;
import practice.project.todo_list.service.TodoService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TodoService todoService;

    @GetMapping
    public SuccessResponseDto<?> testMissingParameter(@RequestParam(name = "content") int content) {
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException();
        }
        return SuccessResponseDto.success();
    }
}
