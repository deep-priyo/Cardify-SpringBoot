package io.aiven.spring.mysql.spring_app;

import org.springframework.data.repository.CrudRepository;

import io.aiven.spring.mysql.spring_app.User;
public interface UserRepository extends CrudRepository<User, Integer> {


}
