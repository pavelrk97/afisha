package com.afisha.main.compilation.service;

import com.afisha.main.compilation.dto.CompilationDto;
import com.afisha.main.compilation.dto.NewCompilationDto;
import com.afisha.main.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    void delete(Long id);

    List<CompilationDto> getAllCompilations(Integer from, Integer size, Boolean pinned);

    CompilationDto findCompilationById(Long compId);
}
