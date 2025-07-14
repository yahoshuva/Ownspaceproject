package com.fwitter.config;

import com.fwitter.models.Category;
import com.fwitter.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryDataLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    private static final List<String> DEFAULT_CATEGORIES = List.of(
            "Nature", "Food", "Traveling", "Technology", "Art", "Sports"
    );

    @Override
    public void run(String... args) throws Exception {
        for (String name : DEFAULT_CATEGORIES) {
            categoryRepository.findByName(name)
                    .or(() -> Optional.of(categoryRepository.save(new Category(name))));
        }
    }
}
