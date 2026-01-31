package com.wixsite.mupbam1.b9_exception_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wixsite.mupbam1.b9_exception_service.models.ErrorLogEntity;
import java.util.List;

public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, Long> {
    
    // Spring Data JPA сам сгенерирует SQL запрос для сортировки по времени
    List<ErrorLogEntity> findAllByOrderByTimestampDesc();

    // Поиск ошибок конкретного сервиса (например, только для 'hello-service')
    List<ErrorLogEntity> findByServiceNameOrderByTimestampDesc(String serviceName);
}