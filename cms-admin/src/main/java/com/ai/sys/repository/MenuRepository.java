package com.ai.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.sys.entity.Menu;

@Repository
public interface MenuRepository extends AbstractRepository<Menu, Long> {

    List<Menu> findByIdIn(List<Long> idList);

    List<Menu> findByParentId(Long parentId);

    @Query(" select m from Menu m order by m.parent, m.sort ")
    List<Menu> findAllMenu();
    
    @Query(" select m from Menu m where m.isShow = 1 order by m.parent, m.sort ")
    List<Menu> findAllShowMenu();

    @Query(" select m from Role r left join r.menuList m where r.id=:roleId and m.parentId=:menuId order by m.sort ")
    List<Menu> findMenusByRoleId(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    @Query(" select m from Role r join r.menuList m where r.id in :roleIdList ")
    List<Menu> findByRoleIdIn(@Param("roleIdList") List<Long> roleIdList);

}
