package com.afisha.main.category;

import org.springframework.data.jpa.repository.JpaRepository;
import com.afisha.main.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsByName(String name);

}
