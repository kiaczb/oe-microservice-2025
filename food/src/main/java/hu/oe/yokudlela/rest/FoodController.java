package hu.oe.yokudlela.rest;

import hu.oe.yokudlela.food.generated.rest.api.DefaultApi;
import hu.oe.yokudlela.food.generated.rest.model.Food;
import hu.oe.yokudlela.food.generated.rest.model.FoodCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class FoodController implements DefaultApi {

    private final List<Food> foods = new ArrayList<>();
    private final List<FoodCategory> categories = new ArrayList<>();

    private int foodIdCounter = 1;
    private int categoryIdCounter = 1;

    // -------------------- INIT --------------------

    public FoodController() {


        FoodCategory soups = new FoodCategory();
        soups.setId(categoryIdCounter++);
        soups.setName("Levesek");

        FoodCategory main = new FoodCategory();
        main.setId(categoryIdCounter++);
        main.setName("Főételek");

        FoodCategory desserts = new FoodCategory();
        desserts.setId(categoryIdCounter++);
        desserts.setName("Desszertek");

        FoodCategory drinks = new FoodCategory();
        drinks.setId(categoryIdCounter++);
        drinks.setName("Italok");

        categories.add(soups);
        categories.add(main);
        categories.add(desserts);
        categories.add(drinks);




        foods.add(createFood("Gulyásleves", soups.getId(), 1200));
        foods.add(createFood("Húsleves", soups.getId(), 1100));
        foods.add(createFood("Halászlé", soups.getId(), 1800));


        foods.add(createFood("Csirkepaprikás", main.getId(), 2200));
        foods.add(createFood("Rántott csirke", main.getId(), 2000));
        foods.add(createFood("Sertéspörkölt", main.getId(), 2400));
        foods.add(createFood("Marhapörkölt", main.getId(), 2800));
        foods.add(createFood("Töltött káposzta", main.getId(), 2300));


        foods.add(createFood("Somlói galuska", desserts.getId(), 1500));
        foods.add(createFood("Palacsinta", desserts.getId(), 900));
        foods.add(createFood("Túrógombóc", desserts.getId(), 1400));
        foods.add(createFood("Csokoládé mousse", desserts.getId(), 1700));


        foods.add(createFood("Ásványvíz", drinks.getId(), 500));
        foods.add(createFood("Kóla", drinks.getId(), 600));
        foods.add(createFood("Narancslé", drinks.getId(), 700));
    }
    private Food createFood(String name, Integer categoryId, Integer price) {
        Food food = new Food();
        food.setId(foodIdCounter++);
        food.setName(name);
        food.setCategoryId(categoryId);
        food.setPrice(price);
        return food;
    }
    

    @Override
    public ResponseEntity<List<FoodCategory>> categoriesGet() {
        return ResponseEntity.ok(categories);
    }

    @Override
    public ResponseEntity<FoodCategory> categoriesPost(FoodCategory category) {
        category.setId(categoryIdCounter++);
        categories.add(category);
        return ResponseEntity.status(201).body(category);
    }

    @Override
    public ResponseEntity<FoodCategory> categoriesIdGet(Integer id) {
        Optional<FoodCategory> category = categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(Integer id) {
        boolean removed = categories.removeIf(c -> c.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



    @Override
    public ResponseEntity<List<Food>> foodsGet() {
        return ResponseEntity.ok(foods);
    }

    @Override
    public ResponseEntity<Food> foodsPost(Food food) {
        food.setId(foodIdCounter++);
        foods.add(food);
        return ResponseEntity.status(201).body(food);
    }

    @Override
    public ResponseEntity<Food> foodsIdGet(Integer id) {
        Optional<Food> food = foods.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();

        return food.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Food> foodsIdPut(Integer id, Food food) {
        Optional<Food> existing = foods.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();

        if (existing.isPresent()) {
            Food updated = existing.get();
            updated.setName(food.getName());
            updated.setCategoryId(food.getCategoryId());
            updated.setPrice(food.getPrice());

            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> foodsIdDelete(Integer id) {
        boolean removed = foods.removeIf(f -> f.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}