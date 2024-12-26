package stezka.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stezka.data.entities.NewsletterEntity;

public interface NewsletterRepository extends JpaRepository<NewsletterEntity, Long> {
    // Optional: Add custom queries if needed
}
