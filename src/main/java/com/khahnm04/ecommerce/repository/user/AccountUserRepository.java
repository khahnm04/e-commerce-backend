package com.khahnm04.ecommerce.repository.user;

import com.khahnm04.ecommerce.entity.user.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long>, JpaSpecificationExecutor<AccountUser> {

    Optional<AccountUser> findByEmailAndIsDeletedFalse(String email);

}
