package com.jskim.banking.data.repository.dailylimit;

import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyAccountUsageRepository extends JpaRepository<DailyAccountUsage, Long> {

  Optional<DailyAccountUsage> findByAccountAndDate(Account account, LocalDate date);
}
