package uk.co.firefly.library.rest.version0_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import uk.co.firefly.library.rest.version0_1.model.PictureSummary;

public interface PictureSummaryRepository extends JpaRepository<PictureSummary, Long>, QuerydslPredicateExecutor<PictureSummary> {

}