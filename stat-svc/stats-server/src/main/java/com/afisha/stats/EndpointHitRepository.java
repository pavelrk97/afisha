package com.afisha.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.afisha.stats.model.EndpointHit;
import com.afisha.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new com.afisha.stats.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "AND e.uri IN (?3) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findStatsUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new com.afisha.stats.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findStatsUniqueIpAllUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.afisha.stats.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "AND e.uri IN (?3) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new com.afisha.stats.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findStatsAllUris(LocalDateTime start, LocalDateTime end);
}