/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.payment.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.integration.payment.data.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> { }
