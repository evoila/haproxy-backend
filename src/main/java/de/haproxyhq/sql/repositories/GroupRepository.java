package de.haproxyhq.sql.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.haproxyhq.sql.Group;

public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

}
