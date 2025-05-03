package practice.project.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.project.todo_list.dao.TodoDao;
import practice.project.todo_list.dto.*;
import practice.project.todo_list.global.error.code.TodoErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.web.dto.PostRequestDto;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;
    private final int ALIVE_STATE = 0;
    private final int DELETE_STATE = 1;

    @Override
    public TodoDetailDto getTodoDetail(int id) {
        return TodoDetailDto.from(
                todoDao.findById(id)
                .orElseThrow(() -> new BusinessException(TodoErrorCode.TODO_NOT_FOUND))
        );
    }

    @Override
    public List<TodoTitleDTO> getTodo() {
        return todoDao.findAll().stream().map(TodoTitleDTO::from).toList();
    }

    @Override
    public int postTodo(PostRequestDto postRequestDto) {
        return todoDao.postTodo(postRequestDto.toEntity());
    }

    @Override
    public int deleteTodo(int id) {
        if (todoDao.setDeleteStateById(id, DELETE_STATE, ALIVE_STATE) == 0) {
            throw new BusinessException(TodoErrorCode.TODO_NOT_FOUND);
        }
        return id;
    }

    @Override
    public List<TodoTitleDTO> getTodoByPeriod(PeriodDto periodDto) {
        if (periodDto.getStart().isAfter(periodDto.getEnd())) {
            throw new BusinessException(TodoErrorCode.WRONG_PERIOD);
        }
        return todoDao.findByPeriod(periodDto.getStart(), periodDto.getEnd()).stream().map(TodoTitleDTO::from).toList();
    }

    @Override
    public void patchState(PatchStateDTO patchStateDTO) {
        int count = todoDao.patchState(patchStateDTO.getId(), patchStateDTO.getState());
        if (count != 1) throw new BusinessException(TodoErrorCode.TODO_NOT_FOUND);
    }

    @Override
    public List<TodoDeleteTitleDto> getDeleteTodo() {
        return todoDao.findDelete().stream().map(TodoDeleteTitleDto::from).toList();
    }

    @Override
    public int restoreTodo(int id) {
        if (todoDao.setDeleteStateById(id, ALIVE_STATE, DELETE_STATE) == 0) {
            throw new BusinessException(TodoErrorCode.TODO_NOT_FOUND);
        }
        return id;
    }

    @Override
    public void updateTodo(TodoUpdateDto todoUpdateDto) {
        if (todoDao.updateTodo(todoUpdateDto.toEntity()) == 0) {
            throw new BusinessException(TodoErrorCode.TODO_NOT_FOUND);
        }
    }

    @Override
    public int deleteTodoInTrash() {
        return todoDao.deleteBefore(LocalDate.now().minusMonths(1L));
    }

    @Override
    public TodoPageOffsetOutDTO getPageTodo(TodoPageOffsetInDTO todoPageOffsetInDTO) {
        int todoCount = todoDao.countTodo();

        if (todoPageOffsetInDTO.haveState()) {
            List<TodoTitleDTO> todoList = todoDao
                    .offsetPagingByState(todoPageOffsetInDTO.getState(), todoPageOffsetInDTO.getSize(), todoPageOffsetInDTO.getSqlOffset())
                    .stream()
                    .map(TodoTitleDTO::from)
                    .toList();
            return TodoPageOffsetOutDTO.of(todoList,todoPageOffsetInDTO,todoCount);
        } else {
            List<TodoTitleDTO> todoList = todoDao
                    .offsetPaging(todoPageOffsetInDTO.getSize(), todoPageOffsetInDTO.getSqlOffset())
                    .stream()
                    .map(TodoTitleDTO::from)
                    .toList();
            return TodoPageOffsetOutDTO.of(todoList,todoPageOffsetInDTO,todoCount);
        }
    }
}
