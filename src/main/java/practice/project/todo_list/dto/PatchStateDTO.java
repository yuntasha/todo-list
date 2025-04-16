package practice.project.todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.project.todo_list.web.dto.PatchRequestDTO;

@Getter
@AllArgsConstructor
@Builder
public class PatchStateDTO {
    private int id;
    private int state;

    public static PatchStateDTO of(int id, PatchRequestDTO patchRequestDTO) {
        return PatchStateDTO.builder()
                .id(id)
                .state(patchRequestDTO.getState())
                .build();
    }
}
