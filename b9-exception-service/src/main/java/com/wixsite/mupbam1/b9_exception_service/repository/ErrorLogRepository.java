package com.wixsite.mupbam1.b9_exception_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wixsite.mupbam1.b9_exception_service.models.ErrorLogEntity;

public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, Long> {
}