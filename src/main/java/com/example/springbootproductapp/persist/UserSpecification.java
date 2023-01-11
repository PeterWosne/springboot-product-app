package com.example.springbootproductapp.persist;

import org.springframework.data.jpa.domain.Specification;

public final class UserSpecification {

    public static Specification<User> usernameLike(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + username + "%");
    }

    public static Specification<User> minAge(Integer minAge) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("age"), minAge);
    }

    public static Specification<User> maxAge(Integer maxAge) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get("age"), maxAge);
    }
}
