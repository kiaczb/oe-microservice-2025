package hu.oe.yokudlela;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // minden végpontra
                        .allowedOrigins("*") // bárhonnan engedélyezve
                        .allowedMethods("*") // minden HTTP metódus (GET, POST stb.)
                        .allowedHeaders("*"); // minden fejléc
            }
        };
    }
}