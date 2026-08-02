package com.lubomirgeorgiev.meal_planner.config;


import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.DishCategory;
import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.model.entity.user.UserRole;
import com.lubomirgeorgiev.meal_planner.repository.dish.DishRepository;
import com.lubomirgeorgiev.meal_planner.repository.group.GroupRepository;
import com.lubomirgeorgiev.meal_planner.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeederConfig {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final DishRepository dishRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner dataSeeder() {
        return args -> {
            User admin = null;

            if (userRepository.count() == 0) {
                Group adminGroup = new Group();
                adminGroup.setDummy(true);
                adminGroup.setPublic(false);
                groupRepository.save(adminGroup);

                admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(UserRole.ADMIN);
                admin.setGroup(adminGroup);
                userRepository.save(admin);
            } else {
                admin = userRepository.findByUsername("admin").orElse(null);
            }

            if (dishRepository.count() == 0 && admin != null) {
                seedDish("Oatmeal with Berries", DishCategory.BREAKFAST, 320,
                        "Creamy steel-cut oats topped with fresh blueberries, strawberries, and honey.",
                        admin);
                seedDish("Avocado Toast", DishCategory.BREAKFAST, 280,
                        "Toasted sourdough spread with mashed avocado, cherry tomatoes, and sea salt.",
                        admin);
                seedDish("Grilled Chicken Salad", DishCategory.LUNCH, 410,
                        "Mixed greens with grilled chicken breast, cucumber, and balsamic vinaigrette.",
                        admin);
                seedDish("Pasta Primavera", DishCategory.LUNCH, 480,
                        "Penne pasta tossed with seasonal vegetables, garlic, olive oil, and basil.",
                        admin);
                seedDish("Baked Salmon & Rice", DishCategory.DINNER, 520,
                        "Oven-baked salmon fillet served with steamed brown rice and roasted asparagus.",
                        admin);
                seedDish("Vegetable Stir-Fry", DishCategory.DINNER, 350,
                        "Colorful mix of bell peppers, broccoli, and snap peas in ginger-soy sauce.",
                        admin);
                seedDish("Mixed Nuts", DishCategory.SNACK, 180,
                        "A handful of almonds, cashews, and walnuts — a protein-rich afternoon pick-me-up.",
                        admin);
                seedDish("Apple with Peanut Butter", DishCategory.SNACK, 210,
                        "Sliced crisp apple paired with a tablespoon of natural peanut butter.",
                        admin);
            }
        };
    }

    private void seedDish(String name, DishCategory category, int calories, String description, User createdBy) {
        Dish dish = new Dish();
        dish.setName(name);
        dish.setCategory(category);
        dish.setCalories(calories);
        dish.setDescription(description);
        dish.setCreatedBy(createdBy);
        dishRepository.save(dish);
    }
}
