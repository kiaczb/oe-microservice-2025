package hu.oe.yokudlela.food.validation;

import hu.oe.yokudlela.rdbms.FoodCategoryRepository;
import hu.oe.yokudlela.rdbms.FoodRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NameExistsValidator.class)
public @interface NameExists {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

@Slf4j
@Component
class NameExistsValidator implements ConstraintValidator<NameExists, String> {

    @Autowired
    private FoodCategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;

        // Megnézzük kategóriák között ÉS ételek között is
        boolean existsInCategory = categoryRepository.existsByName(value);
        boolean existsInFood = foodRepository.existsByName(value);

        log.info("Checking name '{}' - Category exists: {}, Food exists: {}", value, existsInCategory, existsInFood);

        // Ha bármelyikben létezik, akkor érvénytelen (false)
        return !existsInCategory && !existsInFood;
    }
}