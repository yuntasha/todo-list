package practice.project.todo_list.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

//public class StateSubsetValidator implements ConstraintValidator<NullableStateSubset, Integer> {
//
//    private static final Set<Integer> VALID_STATES = Set.of(0, 1, 2);
//
//    @Override
//    public boolean isValid(Integer value, ConstraintValidatorContext context) {
//        return value == null || VALID_STATES.contains(value);
//    }
//}
