package com.khahnm04.ecommerce.security;

import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("employeeUserDetailsService")
@RequiredArgsConstructor
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return employeeRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND.getMessage()));
    }

}
