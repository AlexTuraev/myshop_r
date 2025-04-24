package org.tasks.myshop.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tasks.myshop.dao.model.ItemPicsEntity;

public interface ItemPicsRepository extends JpaRepository<ItemPicsEntity, Long> {
}
