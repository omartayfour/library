package com.vodafone.library.repository;

import com.vodafone.library.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByCustomerId(Long customerId);
    List<BorrowingRecord> findByBookId(Long bookId);
}
