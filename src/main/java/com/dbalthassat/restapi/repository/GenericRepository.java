package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.utils.SortUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Repository parent de tous les repository de l'application.
 *
 * Expose des méthodes génériques à ne pas réécrire pour chaque entité.
 *
 * @param <T> le type d'entité à lier au repository.
 */
@NoRepositoryBean
public interface GenericRepository<T extends ApiEntity> extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {
    default Optional<T> findFirst() {
        int count = (int) count();
        if(count == 0) {
            return Optional.empty();
        }
        Pageable p = new PageRequest(0, 1);
        return Optional.of(findAll(p).getContent().get(0));
    }

    default Optional<T> findLast() {
        int count = (int) count();
        if(count == 0) {
            return Optional.empty();
        }
        Pageable p = new PageRequest(count - 1, 1);
        return Optional.of(findAll(p).getContent().get(0));
    }

    /**
     * Récupère en base toutes les entités de type {@link T} en suivant les conditions de tri, de pagination,
     * de filtre, ainsi que la condition {@code condition) en paramètre. Si {@code fieldName} est précisé, on considère
     * que la requête a été faite à partir d'une sous-ressource (exemple : /greetings/1/messages?sort=significance),
     * et que les champs utilisés pour les tris dans la requête (ici significance) n'existent pas dans la ressource.
     * On ajoute donc le préfixe "'fieldName'." (qui vaut ici "messages") devant le nom de chaque champ utilisé pour le tri.
     *
     * @param request la requête HTTP
     * @param condition la condition à ajouter pour faire la requête en base
     * @param fieldName le nom du champ que l'on récupère et qui doit être utilisé pour préfixer les noms de champs
     * utilisés pour le tri
     * @return un objet de type {@link Page<T>}
     */
    default Page<T> findAll(HttpServletRequest request, BooleanExpression condition, String fieldName) {
        String prefix = StringUtils.isEmpty(fieldName) ? "" : fieldName + ".";
        Predicate predicate = findPredicate(request);
        Pageable originalPageable = findPageable(request);
        List<Sort> sorts = new LinkedList<>();
        // Modification du tri
        if(originalPageable.getSort() != null) {
            originalPageable.getSort().forEach(e -> sorts.add(new Sort(e.getDirection(), prefix + e.getProperty())));
        }
        Pageable realPageable = new PageRequest(originalPageable.getPageNumber(), originalPageable.getPageSize(), SortUtils.createSortFromList(sorts));
        Page<T> page = findAll(condition != null ? condition.and(predicate) : predicate, realPageable);
        // Affichage du tri tel que demandé par l'utilisateur
        Pageable shownPageable = new PageRequest(realPageable.getPageNumber(), realPageable.getPageSize(), originalPageable.getSort());
        return new PageImpl<>(page.getContent(), shownPageable, page.getTotalElements());
    }

    /**
     * Récupère en base toutes les entités de type {@link T} en suivant les conditions de tri, de pagination,
     * de filtre, ainsi que la condition {@code condition) en paramètre.
     * @param request la requête HTTP
     * @param condition la condition à ajouter pour faire la requête en base
     * @return un objet de type {@link Page<T>}
     */
    default Page<T> findAll(HttpServletRequest request, BooleanExpression condition) {
        return findAll(request, condition, null);
    }

    /**
     * Récupère en base toutes les entités de type {@link T} en suivant les conditions de tri, de pagination et
     * de filtre précisés par l'utilisateur dans l'URL
     *
     * @param request la requête HTTP
     * @return un objet de type {@link Page<T>}
     */
    default Page<T> findAll(HttpServletRequest request) {
        return findAll(request, null);
    }

    /**
     * Récupère un objet de type {@link Predicate} contenu dans la requête qui contient tous les filtres précisés
     * dans l'url par l'utilisateur
     *
     * @param request la requête HTTP
     * @return un objet de type {@link Predicate}
     */
    default Predicate findPredicate(HttpServletRequest request) {
        return (Predicate) request.getAttribute("predicate");
    }

    /**
     * Récupère un objet de type {@link Pageable} contenu dans la requête qui contient toutes les informations de
     * pagination ainsi que de tri précisées dans l'url par l'utilisateur
     *
     * @param request la requête HTTP
     * @return un objet de type {@link Pageable}
     */
    default Pageable findPageable(HttpServletRequest request) {
        return (Pageable) request.getAttribute("pageable");
    }
}
