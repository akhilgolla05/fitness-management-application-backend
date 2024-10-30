package com.learnboot.fitnessmanagementsystem.repository;

import com.learnboot.fitnessmanagementsystem.domains.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<List<Address>> findByUserId(long userId);
}
