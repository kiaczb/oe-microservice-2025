package hu.oe.yokudlela;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // --- UUID Konverterek (Food-hoz) ---
        modelMapper.addConverter(new Converter<UUID, String>() {
            public String convert(MappingContext<UUID, String> context) {
                return context.getSource() == null ? null : context.getSource().toString();
            }
        });

        modelMapper.addConverter(new Converter<String, UUID>() {
            public UUID convert(MappingContext<String, UUID> context) {
                return context.getSource() == null ? null : UUID.fromString(context.getSource());
            }
        });

        // --- Long <-> String Konverterek (FoodCategory-hoz) ---
        // Ez kell, hogy a Long ID-ból String legyen a JSON-ben és vissza
        modelMapper.addConverter(new Converter<Long, String>() {
            public String convert(MappingContext<Long, String> context) {
                return context.getSource() == null ? null : context.getSource().toString();
            }
        });

        modelMapper.addConverter(new Converter<String, Long>() {
            public Long convert(MappingContext<String, Long> context) {
                return context.getSource() == null ? null : Long.valueOf(context.getSource());
            }
        });

        // --- Tiltások (Hogy ne a 0-át akarja menteni) ---

        // Food mentésnél skip ID
        modelMapper.typeMap(hu.oe.yokudlela.food.generated.rest.model.FoodRequest.class,
                        hu.oe.yokudlela.food.generated.entity.Food.class)
                .addMappings(mapper -> mapper.skip(hu.oe.yokudlela.food.generated.entity.Food::setId));

        // FoodCategory mentésnél skip ID
        modelMapper.typeMap(hu.oe.yokudlela.food.generated.rest.model.FoodCategoryRequest.class,
                        hu.oe.yokudlela.food.generated.entity.FoodCategory.class)
                .addMappings(mapper -> mapper.skip(hu.oe.yokudlela.food.generated.entity.FoodCategory::setId));

        return modelMapper;
    }
}