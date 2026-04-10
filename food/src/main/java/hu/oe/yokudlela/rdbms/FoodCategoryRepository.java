package hu.oe.yokudlela.rdbms;

import hu.oe.yokudlela.food.generated.entity.FoodCategory;
import org.springframework.data.repository.CrudRepository;

public interface FoodCategoryRepository extends CrudRepository<FoodCategory, Long> {

    boolean existsByName(String pName);

    Object getById(Long id);
}