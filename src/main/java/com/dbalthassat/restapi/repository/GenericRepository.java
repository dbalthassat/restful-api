package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.utils.SortUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@NoRepositoryBean
public interface GenericRepository<T> extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {
    default Page<T> findAll(HttpServletRequest request) {
        Predicate predicate = findPredicate(request);
        Pageable pageable = findPageable(request);
        return findAll(predicate, pageable);
    }

    // TODO doc rapidement.
    default Page<T> findAll(HttpServletRequest request, String fieldName, BooleanExpression condition) {
        Predicate predicate = findPredicate(request);
        Pageable originalPageable = findPageable(request);
        List<Sort> sorts = new LinkedList<>();
        originalPageable.getSort().forEach(e -> sorts.add(new Sort(e.getDirection(), fieldName + "." + e.getProperty())));
        Pageable realPageable = new PageRequest(originalPageable.getPageNumber(), originalPageable.getPageSize(), SortUtils.createSortFromList(sorts));
        Page<T> page = findAll(condition.and(predicate), realPageable);
        Pageable shownPageable = new PageRequest(realPageable.getPageNumber(), realPageable.getPageSize(), originalPageable.getSort());
        return new PageImpl<>(page.getContent(), shownPageable, page.getTotalElements());
    }

    default Predicate findPredicate(HttpServletRequest request) {
        return (Predicate) request.getAttribute("predicate");
    }

    default Pageable findPageable(HttpServletRequest request) {
        return (Pageable) request.getAttribute("pageable");
    }
}
