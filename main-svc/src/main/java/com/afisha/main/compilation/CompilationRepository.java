package com.afisha.main.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.afisha.main.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {

    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
