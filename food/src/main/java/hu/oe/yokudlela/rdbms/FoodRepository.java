package hu.oe.yokudlela.rdbms;

import hu.oe.yokudlela.food.generated.entity.Food;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FoodRepository extends CrudRepository<Food, UUID> {

    boolean existsByName(String pName);

    Object getById(UUID id);
}