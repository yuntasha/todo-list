package practice.project.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import practice.project.todo_list.dao.TodoDao;
import practice.project.todo_list.dto.PatchStateDTO;
import practice.project.todo_list.dto.PeriodDto;
import practice.project.todo_list.dto.TodoDetailDto;
import practice.project.todo_list.dto.TodoTitleDto;
import practice.project.todo_list.global.error.code.TodoErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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

    @Override
    public List<TodoTitleDto> getTodoByPeriod(PeriodDto periodDto) {
        if (periodDto.getStart().isAfter(periodDto.getEnd())) {
            throw new BusinessException(TodoErrorCode.WRONG_PERIOD);
        }
        return todoDao.findByPeriod(periodDto);
    }

    private int change(String s, int start, int end) {
        return Integer.parseInt(s.substring(start, end));
    }

    @Override
    public void patchState(PatchStateDTO patchStateDTO) {
        int count = todoDao.patchState(patchStateDTO.getId(), patchStateDTO.getState());
        if (count != 1) throw new BusinessException(TodoErrorCode.TODO_NOT_FOUND);
    }

    @Override
    public List<TodoTitleDto> getDeleteTodo() {
        return todoDao.findDelete();
    }
}
