package com.ai.common.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.ai.common.jpa.PropertyFilter.MatchType;
import com.ai.common.utils.DateUtils;

public class SpecificationUtils {

    /**
     * 根据过滤条件获取查询对象
     *
     * @param filters
     * 
     * @param <T>
     * @return
     */
    public static <T> Specification<T> getSpecification(final List<PropertyFilter> filters) {
        return new Specification<T>() {

            @Override
            public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
                if (null != filters && !filters.isEmpty()) {
                    List<Predicate> predicates = new ArrayList<Predicate>();

                    Predicate predicate = null;
                    for (PropertyFilter pf : filters) {
                        if (pf.getSubList() != null && pf.getSubList().size() > 0) {
                            List<Predicate> tempPredicate = getPredicateList(pf.getSubList(), root, cb);
                            predicate = cb.or(tempPredicate.toArray(new Predicate[tempPredicate.size()]));
                        } else {
                            predicate = getPredicate(pf, root, cb);
                        }
                        if (predicate != null) {
                            predicates.add(predicate);
                        }
                    }
                    if (predicates.size() > 0) {
                        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }
                return cb.conjunction();
            }
        };
    }

    public static <T> List<Predicate> getPredicateList(final List<PropertyFilter> filters, final Root<T> root, final CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        for (PropertyFilter pf : filters) {
            Predicate predicate = getPredicate(pf, root, cb);

            if (predicate != null) {
                predicates.add(predicate);
            }
        }
        return predicates;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Predicate getPredicate(final PropertyFilter pf, final Root<T> root, final CriteriaBuilder cb) {
        Object value = pf.getPropertyValue();
        if (null == value) {
            return null;
        }
        Path path;
        if (pf.getPropertyName().indexOf("_") > -1) {
            String[] names = StringUtils.split(pf.getPropertyName(), "_");
            path = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                path = path.get(names[i]);
            }
        } else {
            path = root.get(pf.getPropertyName());
        }

        if (pf.getPropertyType().equals(Date.class)) {
            if (value.toString().length() == 10 && (pf.getMatchType() == MatchType.LT || pf.getMatchType() == MatchType.LE)) {
                value = DateUtils.parseDate((value.toString() + " 23:59:59"));
            } else {
                value = DateUtils.parseDate((value.toString()));
            }
        }
        switch (pf.getMatchType()) {
            case EQ :
                return cb.equal(path, value);
            case NQ :
                return cb.notEqual(path, value);
            case LIKE :
                return cb.like(path, "%" + value + "%");
            case LT :
                return cb.lessThan(path, (Comparable) value);
            case GT :
                return cb.greaterThan(path, (Comparable) value);
            case LE :
                return cb.lessThanOrEqualTo(path, (Comparable) value);
            case GE :
                return cb.greaterThanOrEqualTo(path, (Comparable) value);
            case IN :
                Object[] inArray = String.valueOf(value).split(",");
                return path.in(inArray);
            case NIN :
                // 暂时没实现
                return null;
            case ISNULL :
                return cb.isNull(path);
            case ISEMPTY :
            	List<Predicate> tempPredicate = new ArrayList<Predicate>();
                tempPredicate.add(cb.isNull(path));
                tempPredicate.add(cb.equal(path, ""));
                return cb.or(tempPredicate.toArray(new Predicate[tempPredicate.size()]));
            case BITMASK :
                Integer maskInt = Integer.valueOf(value.toString());
                if (maskInt > 0) {
                    Expression<Integer> div = cb.quot(path, maskInt).as(Integer.class);
                    Expression<Integer> mod = cb.mod(div, 10);
                    return cb.equal(mod, 1);
                } else if (maskInt < 0) {
                    return cb.equal(path, Math.abs(maskInt));
                } else {
                	return cb.equal(path, value);
                }
            case CHARMASK :
                Expression<String> left = cb.concat(",", path);
                Expression<String> rigth = cb.concat(left, ",");
                return cb.like(rigth, "%," + value + ",%");
			case INMASK:
				List<Predicate> tempPredicateIn = new ArrayList<Predicate>();
				Expression<String> leftIn = cb.concat(",", path);
				Expression<String> rigthIn = cb.concat(leftIn, ",");
				String[] keyArray = String.valueOf(value).split(",");
				for (String key : keyArray) {
					tempPredicateIn.add(cb.like(rigthIn, "%," + key + ",%"));
				}
				return cb.or(tempPredicateIn.toArray(new Predicate[tempPredicateIn
						.size()]));
        }
        return null;
    }
}
