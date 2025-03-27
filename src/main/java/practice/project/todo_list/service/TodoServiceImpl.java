package practice.project.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import practice.project.todo_list.dao.TodoDao;
import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.global.error.code.ErrorCode;
import practice.project.todo_list.global.error.code.TodoErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;

    @Override
    public TodoDetailDto getTodoDetail(int id) {
        return todoDao.findById(id).orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));
    }

    @Override
    public List<TodoTitleDto> getTodo() {
        return todoDao.findAll();
    }

    @Override
    public int postTodo(PostRequestDto postRequestDto) {
        return todoDao.postTodo(postRequestDto);
    }

    @Override
    public int deleteTodo(int id) {
        todoDao.findById(id).orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND));
        return todoDao.setDeleteStateByid(id);
    }
}
