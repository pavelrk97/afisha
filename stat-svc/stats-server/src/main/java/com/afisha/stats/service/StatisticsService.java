package com.afisha.stats.service;

import com.afisha.stats.EndpointHitCreateDto;
import com.afisha.stats.EndpointHitDto;
import com.afisha.stats.StatsRequestDto;
import com.afisha.stats.ViewStatsDto;

import java.util.List;

public interface StatisticsService {
    EndpointHitDto create(EndpointHitCreateDto endpoint);

    List<ViewStatsDto> getStats(StatsRequestDto statsRequestDto);
}
