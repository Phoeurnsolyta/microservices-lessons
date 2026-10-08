package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import kh.edu.istad.platform.customer.persistence.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setId(customer.getId().value());
        customerEntity.setUsername(customer.getUsername());
        customerEntity.setFamilyName(customer.getFamilyName());
        customerEntity.setGivenName(customer.getGivenName());
        customerEntity.setEmail(customer.getEmail().value());
        customerEntity.setPhoneNumber(customer.getPhoneNumber().value());
        customerEntity.setStatus(customer.getStatus());

        CustomerEntity savedEntity = customerJpaRepository.save(customerEntity);

        return Customer.builder()
                .id(new CustomerId(savedEntity.getId()))
                .username(savedEntity.getUsername())
                .familyName(savedEntity.getFamilyName())
                .givenName(savedEntity.getGivenName())
                .email(new Email(savedEntity.getEmail()))
                .phoneNumber(new PhoneNumber(savedEntity.getPhoneNumber()))
                .status(savedEntity.getStatus())
                .build();
    }

    @Override
    public Optional<Customer> findById(CustomerId customerId) {
        return customerJpaRepository.findById(customerId.value())
                .map(customerEntity -> Customer.builder()
                        .id(new CustomerId(customerEntity.getId()))
                        .username(customerEntity.getUsername())
                        .familyName(customerEntity.getFamilyName())
                        .givenName(customerEntity.getGivenName())
                        .email(new Email(customerEntity.getEmail()))
                        .phoneNumber(new PhoneNumber(customerEntity.getPhoneNumber()))
                        .status(customerEntity.getStatus())
                        .build()
                );
    }


}
