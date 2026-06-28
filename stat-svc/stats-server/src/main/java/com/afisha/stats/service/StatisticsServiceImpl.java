package com.afisha.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.afisha.stats.*;
import com.afisha.stats.mapper.EndpointHitMapper;
import com.afisha.stats.mapper.ViewStatsMapper;
import com.afisha.stats.model.EndpointHit;
import com.afisha.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public EndpointHitDto create(EndpointHitCreateDto endpointHitCreateDto) {
        log.info("Создание EndpointHit с данными: {}", endpointHitCreateDto);
        EndpointHit hit = EndpointHitMapper.toEndpointHitFromCreateDto(endpointHitCreateDto);
        EndpointHit createdHit = endpointHitRepository.save(hit);
        return EndpointHitMapper.toEndpointHitDto(createdHit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> getStats(StatsRequestDto request) {
        log.info("Получение статистики: {}", request);

        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();
        Boolean unique = request.getUnique();
        List<String> uris = request.getUris();

        if (start != null && end != null && start.isAfter(end)) {
            log.warn("Некорректный запрос: start={} позже end={}", start, end);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be before end date");
        }

        List<ViewStats> viewStats;
        if (unique) {
            if (uris != null && !uris.isEmpty()) {
                viewStats = endpointHitRepository.findStatsUniqueIp(start, end, uris);
            } else {
                viewStats = endpointHitRepository.findStatsUniqueIpAllUris(start, end);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                viewStats = endpointHitRepository.findStats(start, end, uris);
            } else {
                viewStats = endpointHitRepository.findStatsAllUris(start, end);
            }
        }
        log.info("Получена статистика: {}", viewStats);

        return viewStats != null ? viewStats.stream()
                .map(ViewStatsMapper::toViewStatsDto)
                .collect(Collectors.toList()) : List.of();
    }
}
