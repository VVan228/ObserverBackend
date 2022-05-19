package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.isu.observer.model.hierarchy.Hierarchy;

import java.util.List;

public interface HierarchyRepo extends JpaRepository<Hierarchy, Long> {

    @Query("select o.hierarchyLegend from Organisation o where o.id=:organisationId")
    String getHierarchyLabelsByOrganisation(@Param("organisationId") Long organisationId);

    @Query("select o.id from HierarchyRoot h join h.organisation o where o.id=:organisationId")
    Long getHierarchyIdByOrganisation(@Param("organisationId") Long organisationId);

    @Query("select hh.id from Hierarchy h join h.children hh where h.id=:rootId")
    List<Long> getHierarchyLevel1(@Param("rootId") Long rootId);

    @Query("select hhh.id from Hierarchy h join h.children hh join hh.children hhh where h.id=:rootId")
    List<Long> getHierarchyLevel2(@Param("rootId") Long rootId);

    @Query("select hhhh.id from Hierarchy h join h.children hh join hh.children hhh join hhh.children hhhh where h.id=:rootId")
    List<Long> getHierarchyLevel3(@Param("rootId") Long rootId);

    @Query("select hhhhh.id from Hierarchy h join h.children hh join hh.children hhh join hhh.children hhhh join hhhh.children hhhhh where h.id=:rootId")
    List<Long> getHierarchyLevel4(@Param("rootId") Long rootId);

    @Query("select h.name from HierarchyBranch h where h.id=:id")
    String getNameById(@Param("id") Long id);

}
