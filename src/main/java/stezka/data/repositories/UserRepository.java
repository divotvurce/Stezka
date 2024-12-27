package stezka.data.repositories;

import org.springframework.data.repository.CrudRepository;
import stezka.data.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
