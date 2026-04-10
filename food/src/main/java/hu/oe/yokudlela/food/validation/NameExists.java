package hu.oe.yokudlela.food.validation;

import hu.oe.yokudlela.rdbms.FoodCategoryRepository;
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

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            return true;
        }

        boolean exists = categoryRepository.existsByName(value);
        log.info("Checking if name '{}' exists: {}", value, exists);

        return !exists;
    }
}