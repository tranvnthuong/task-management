package com.tvt.task_mgmt.repository;

import com.tvt.task_mgmt.entity.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<BlackListToken, String> {
}