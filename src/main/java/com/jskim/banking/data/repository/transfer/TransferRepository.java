package com.jskim.banking.data.repository.transfer;

import com.jskim.banking.data.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

}
