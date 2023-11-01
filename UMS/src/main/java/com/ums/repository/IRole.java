package com.ums.repository;

import com.ums.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRole extends JpaRepository<Role, String> {
    Role findByRoleNameAndIsDeleted(String roleName, Boolean isDeleted);
    @Query(value = "SELECT * FROM roles  WHERE is_deleted = false ORDER BY role_name ASC", nativeQuery = true)
    List<Role> findAllWithRequiredFields();

//    @Transactional
//    @Modifying
//    @Query("SELECT id " +
//            "FROM roles e1 " +
//            "INNER JOIN roles e2 ON e1.id = e2.reportingTo" +
//            "WHERE e1.isDeleted = false")
//    List<Role> performInnerJoin();

}
