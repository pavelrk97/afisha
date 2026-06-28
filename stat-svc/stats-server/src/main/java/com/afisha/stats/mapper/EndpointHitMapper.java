package com.afisha.stats.mapper;

import com.afisha.stats.EndpointHitCreateDto;
import com.afisha.stats.EndpointHitDto;
import com.afisha.stats.model.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHitFromCreateDto(EndpointHitCreateDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }
}