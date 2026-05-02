package hu.oe.yokudlela.rest;

import hu.oe.yokudlela.food.generated.rest.api.DefaultApi;
import hu.oe.yokudlela.food.generated.rest.model.*;
import hu.oe.yokudlela.food.generated.entity.Food;
import hu.oe.yokudlela.food.generated.entity.FoodCategory;
import hu.oe.yokudlela.rdbms.FoodCategoryRepository;
import hu.oe.yokudlela.rdbms.FoodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class FoodController implements DefaultApi {

    private final FoodRepository foodRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final ModelMapper modelMapper;

    public FoodController(FoodRepository foodRepository, FoodCategoryRepository foodCategoryRepository, ModelMapper modelMapper) {
        this.foodRepository = foodRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.modelMapper = modelMapper;
    }

    // ==========================================
    //            KATEGÓRIA VÉGPONTOK
    // ==========================================

    @Override
    public ResponseEntity<List<FoodCategoryResponse>> categoriesGet() {
        List<FoodCategoryResponse> responses = StreamSupport.stream(foodCategoryRepository.findAll().spliterator(), false)
                .map(entity -> {
                    FoodCategoryResponse response = modelMapper.map(entity, FoodCategoryResponse.class);
                    response.setId(String.valueOf(entity.getId())); // long -> String
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<IdModel> categoriesPost(FoodCategoryRequest request) {
        FoodCategory entityToSave = modelMapper.map(request, FoodCategory.class);
        FoodCategory savedEntity = foodCategoryRepository.save(entityToSave);

        IdModel idModel = new IdModel();
        idModel.setId(String.valueOf(savedEntity.getId())); // long -> String
        return ResponseEntity.status(201).body(idModel);
    }

    @Override
    public ResponseEntity<FoodCategoryResponse> categoriesIdGet(String id) {
        Optional<FoodCategory> categoryOpt = foodCategoryRepository.findById(Long.parseLong(id));

        if (categoryOpt.isPresent()) {
            FoodCategory entity = categoryOpt.get();
            FoodCategoryResponse response = modelMapper.map(entity, FoodCategoryResponse.class);
            response.setId(String.valueOf(entity.getId()));
            return ResponseEntity.ok(response);
        }

        // Ha nem találta meg
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<FoodCategoryResponse> categoriesIdPut(String id, FoodCategoryRequest foodCategoryRequest) {
        Optional<FoodCategory> categoryOpt = foodCategoryRepository.findById(Long.parseLong(id));

        if (categoryOpt.isPresent()) {
            FoodCategory existingCategory = categoryOpt.get();
            // Adatok frissítése
            existingCategory.setName(foodCategoryRequest.getName());

            FoodCategory updatedEntity = foodCategoryRepository.save(existingCategory);

            // Válasz összeállítása
            FoodCategoryResponse response = modelMapper.map(updatedEntity, FoodCategoryResponse.class);
            response.setId(String.valueOf(updatedEntity.getId()));
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(String id) {
        Long longId = Long.parseLong(id); // String -> Long
        if (foodCategoryRepository.existsById(longId)) {
            foodCategoryRepository.deleteById(longId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    //               ÉTEL VÉGPONTOK
    // ==========================================

    @Override
    public ResponseEntity<List<FoodResponse>> foodsGet() {
        List<FoodResponse> responses = StreamSupport.stream(foodRepository.findAll().spliterator(), false)
                .map(this::convertToFoodResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<IdModel> foodsPost(FoodRequest request) {
        // String kategória ID -> Long konverzió
        Optional<FoodCategory> categoryOpt = foodCategoryRepository.findById(Long.parseLong(request.getCategoryId()));
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Food entityToSave = modelMapper.map(request, Food.class);
        entityToSave.setFoodCategory(categoryOpt.get());

        Food savedEntity = foodRepository.save(entityToSave);

        IdModel idModel = new IdModel();
        idModel.setId(savedEntity.getId().toString()); // Itt marad a toString(), mert a Food ID-ja UUID
        return ResponseEntity.status(201).body(idModel);
    }

    @Override
    public ResponseEntity<FoodResponse> foodsIdGet(String id) {
        return foodRepository.findById(UUID.fromString(id))
                .map(entity -> ResponseEntity.ok(convertToFoodResponse(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<FoodResponse> foodsIdPut(String id, FoodRequest request) {
        UUID foodUuid = UUID.fromString(id);
        Optional<Food> existingFoodOpt = foodRepository.findById(foodUuid);

        if (existingFoodOpt.isPresent()) {
            Food existingFood = existingFoodOpt.get();

            // Kategória ID konverzió (String -> Long)
            Optional<FoodCategory> categoryOpt = foodCategoryRepository.findById(Long.parseLong(request.getCategoryId()));
            if (categoryOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            existingFood.setName(request.getName());
            existingFood.setPrice(request.getPrice());
            existingFood.setFoodCategory(categoryOpt.get());

            Food updatedEntity = foodRepository.save(existingFood);
            return ResponseEntity.ok(convertToFoodResponse(updatedEntity));
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> foodsIdDelete(String id) {
        UUID uuid = UUID.fromString(id);
        if (foodRepository.existsById(uuid)) {
            foodRepository.deleteById(uuid);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    //              SEGÉDMETÓDUSOK
    // ==========================================

    private FoodResponse convertToFoodResponse(Food entity) {
        FoodResponse response = modelMapper.map(entity, FoodResponse.class);
        if (entity.getFoodCategory() != null) {
            // Mivel a getFoodCategory().getId() egy primitív long, sosem null, így egyből mehet a String.valueOf()
            response.setCategoryId(String.valueOf(entity.getFoodCategory().getId()));
        }
        return response;
    }
}