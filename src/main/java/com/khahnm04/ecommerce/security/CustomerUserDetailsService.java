package com.khahnm04.ecommerce.security;

import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.user.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customerUserDetailsService")
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return customerRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND.getMessage()));
    }

}
