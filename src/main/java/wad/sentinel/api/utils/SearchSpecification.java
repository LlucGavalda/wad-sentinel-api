package wad.sentinel.api.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.ImmutableMap;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import wad.sentinel.api.Constants;
import wad.sentinel.api.exceptions.ParameterInvalidException;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

/**
 * 
 * Generic entity search, included by join entity fields.
 * <br>
 * Operators and data types allowed:<br>
 * "equals":<br>
 * String<br>
 * Integer<gr>
 * Date<br>
 * Boolean<br>
 * "contains"<br>
 * String<br>
 * "begins"<br>
 * String<br>
 * "greater"<br>
 * Integer<gr>
 * Date<br>
 * "lower"<br>
 * Integer<gr>
 * Date<br>
 * "greaterEquals"<br>
 * Integer<gr>
 * Date<br>
 * "lowerEquals"<br>
 * Integer<gr>
 * Date<br>
 * <br>
 * Boolean values must be "true" (value true) or anything else (interpreted as
 * false).<br>
 * <br>
 * Requires a minimum of Constants.MIN_LENGTH_SEARCH_VALUE chars to search.
 * Otherwise throws an exception.
 * 
 * @param <T>
 */
public class SearchSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1900581010229669687L;

    /**
     * Allowed types of fields to search
     */
    public static enum SearchType {
        STRING,
        NUMBER,
        DATE_TIME,
        BOOLEAN
    }

    /**
     * Allowed types of fields to search
     */
    public static enum SearchOperator {
        EQUALS,
        BEGINS,
        CONTAINS,
        GREATER,
        LOWER,
        GREATER_EQUALS,
        LOWER_EQUALS,
        AND,
        OR,
        OPEN_PAR,
        CLOSE_PAR
    }

    /**
     * Mapped search operators
     */
    public static Map<String, SearchOperator> searchOperators = ImmutableMap.<String, SearchOperator>builder()
            .put("equals", SearchOperator.EQUALS)
            .put("begins", SearchOperator.BEGINS)
            .put("contains", SearchOperator.CONTAINS)
            .put("greater", SearchOperator.GREATER)
            .put("lower", SearchOperator.LOWER)
            .put("greaterEquals", SearchOperator.GREATER_EQUALS)
            .put("lowerEquals", SearchOperator.LOWER_EQUALS)
            .put("AND", SearchOperator.AND)
            .put("OR", SearchOperator.OR)
            .put("(", SearchOperator.OPEN_PAR)
            .put(")", SearchOperator.CLOSE_PAR)
            .build();

    /**
     * Mapped search operators
     */
    public static Map<String, String> searchOperatorsTranslation = ImmutableMap.<String, String>builder()
            .put("equals", "=")
            // .put("begins", SearchOperator.BEGINS)
            .put("contains", "LIKE")
            .put("greater", ">")
            .put("lower", "<")
            .put("greaterEquals", ">=")
            .put("lowerEquals", "<=")
            .build();
    private List<SearchCriteria> list;

    public SearchSpecification() {
        this.list = new ArrayList<>();
    }

    /**
     * Function that translate operator string to really used operator in the psql
     * sentence
     * ej: equals translate to =
     * 
     * @param searchableFields
     * @param dtoArray
     */
    public String translateOperator(String logicalOperator) {
        return searchOperatorsTranslation.get(logicalOperator);
    }

    /**
     * Convenience constructor that creates all the internal structure from the
     * parameters
     * 
     * @param searchableFields
     * @param dtoArray
     */
    public SearchSpecification(Map<String, SearchType> searchableFields, SearchCriteriaDto[] dtoArray) {

        Object value;

        // Clean the search criteria list
        this.list = new ArrayList<>();

        // Convert all the predicates to search criteria
        for (SearchCriteriaDto dto : dtoArray) {

            // Check field
            if (!searchableFields.containsKey(dto.getField()))
                throw new ParameterInvalidException("field", dto.getField());

            // // Check operator
            // if (!searchOperators.containsKey(dto.getOperator()))
            // throw new ParameterInvalidException("operator", dto.getOperator());

            checkOperator(searchableFields, dto);

            // Check value
            value = checkValue(searchableFields, dto);

            // Everything is ok, so add the criteria
            list.add(new SearchCriteria(dto.getField(), value, searchOperators.get(dto.getOperator()),
                    searchableFields.get(dto.getField())));
        }
    }

    /**
     * Validates that the operator is correct and valid for the field type.
     * 
     * @param value
     */
    private static void checkOperator(Map<String, SearchType> searchableFields, SearchCriteriaDto dto) {
        // Check the operator is a valid one
        if (!searchOperators.containsKey(dto.getOperator()))
            throw new ParameterInvalidException("operator", dto.getOperator());

        // Check the operator is valid for the type of field
        boolean ok = false;
        SearchType fieldType = searchableFields.get(dto.getField());
        SearchOperator operator = searchOperators.get(dto.getOperator());

        if (operator == SearchOperator.EQUALS) {
            ok = true;

        } else if (operator == SearchOperator.BEGINS) {
            if (fieldType == SearchType.STRING) {
                ok = true;
            }

        } else if (operator == SearchOperator.CONTAINS) {
            if (fieldType == SearchType.STRING) {
                ok = true;
            }

        } else if (operator == SearchOperator.GREATER) {
            if ((fieldType == SearchType.NUMBER) ||
                    (fieldType == SearchType.DATE_TIME)) {
                ok = true;
            }

        } else if (operator == SearchOperator.LOWER) {
            if ((fieldType == SearchType.NUMBER) ||
                    (fieldType == SearchType.DATE_TIME)) {
                ok = true;
            }

        } else if (operator == SearchOperator.GREATER_EQUALS) {
            if ((fieldType == SearchType.NUMBER) ||
                    (fieldType == SearchType.DATE_TIME)) {
                ok = true;
            }

        } else if (operator == SearchOperator.LOWER_EQUALS) {
            if ((fieldType == SearchType.NUMBER) ||
                    (fieldType == SearchType.DATE_TIME)) {
                ok = true;
            }

        } else if (operator == SearchOperator.AND ||
                operator == SearchOperator.OR ||
                operator == SearchOperator.OPEN_PAR ||
                operator == SearchOperator.CLOSE_PAR) {
            ok = true;
        }

        if (!ok)
            throw new ParameterInvalidException("operator", dto.getOperator());
    }

    /**
     * Validates that the value received complies with minimum conditions and
     * sets the value type.
     * 
     * @param value
     */
    private static Object checkValue(Map<String, SearchType> searchableFields, SearchCriteriaDto dto) {

        Object value = null;

        // Check there is a value to validate
        if ((dto.getValue() != null) && (dto.getValue().trim().length() > 0)) {

            SearchType fieldType = searchableFields.get(dto.getField());
            SearchOperator operator = searchOperators.get(dto.getOperator());

            // Check value
            if (fieldType == SearchType.STRING) {
                // Check minimum length of the value; if the operation is EQUALS, accept any
                // length
                if ((operator == SearchOperator.EQUALS)
                        || (dto.getValue().trim().length() >= Constants.MIN_LENGTH_SEARCH_VALUE))
                    value = dto.getValue().trim();

            } else if (fieldType == SearchType.NUMBER) {
                try {
                    value = Integer.decode(dto.getValue());
                } catch (NumberFormatException ex) {
                    // Nothing to do; value will be null and will throw at the end
                }

            } else if (fieldType == SearchType.DATE_TIME) {
                try {
                    value = Timestamp.valueOf(LocalDateTime.parse(dto.getValue()));
                } catch (DateTimeParseException | NullPointerException ex) {
                    // Nothing to do; value will be null and will throw at the end
                }

            } else if (fieldType == SearchType.BOOLEAN) {
                value = "true".equalsIgnoreCase(dto.getValue());
            }
        }

        if (value == null)
            throw new ParameterInvalidException("value", dto.getValue());

        return value;
    }

    /**
     * Checks all the search criteria against the searchableFields.
     * 
     * @param dtoArray
     * @param searchableFields
     */
    public static void checkSearchCriteria(SearchCriteriaDto[] dtoArray, Map<String, SearchType> searchableFields) {
        for (SearchCriteriaDto dto : dtoArray) {

            // Check operator
            if (!searchOperators.containsKey(dto.getOperator()))
                throw new ParameterInvalidException("operator", dto.getOperator());

            SearchOperator operator = searchOperators.get(dto.getOperator());
            if (operator == SearchOperator.AND ||
                    operator == SearchOperator.OR ||
                    operator == SearchOperator.OPEN_PAR ||
                    operator == SearchOperator.CLOSE_PAR) {
                // Do not check field and value, they should be empty.
                // Otherwise, they will be ignored.
            } else {
                // Check field
                if (!searchableFields.containsKey(dto.getField()))
                    throw new ParameterInvalidException("field", dto.getField());

                // Check value
                checkValue(searchableFields, dto);
            }
        }
    }

    /**
     * Adds a String or Integer (as string) predicate related to the operator.
     * Validates the operator, throwing an exception if needed.
     * 
     * @param predicates
     * @param builder
     * @param expression
     * @param operator
     * @param value
     * 
     */
    private void addPredicateString(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Expression<String> expression,
            SearchCriteria criteria) {

        String value = criteria.getValue().toString();

        if (criteria.getOperator() == SearchOperator.EQUALS) {
            predicates.add(builder.equal(expression, value));
        } else if (criteria.getOperator() == SearchOperator.CONTAINS) {
            predicates.add(builder.like(expression, "%" + value + "%"));
        } else if (criteria.getOperator() == SearchOperator.BEGINS) {
            predicates.add(builder.like(expression, value + "%"));
        } else if (criteria.getOperator() == SearchOperator.GREATER) {
            predicates.add(builder.greaterThan(expression, criteria.getValue().toString()));
        } else if (criteria.getOperator() == SearchOperator.LOWER) {
            predicates.add(builder.lessThan(expression, criteria.getValue().toString()));
        } else if (criteria.getOperator() == SearchOperator.GREATER_EQUALS) {
            predicates.add(builder.greaterThanOrEqualTo(expression, criteria.getValue().toString()));
        } else if (criteria.getOperator() == SearchOperator.LOWER_EQUALS) {
            predicates.add(builder.lessThanOrEqualTo(expression, criteria.getValue().toString()));
        }
    }

    /**
     * Adds a Timestamp predicate related to the operator.
     * Validates the operator, throwing an exception if needed.
     * 
     * @param predicates
     * @param builder
     * @param expression
     * @param operator
     * @param value
     * 
     */
    private void addPredicateTimestamp(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Expression<Timestamp> expression,
            SearchCriteria criteria) {

        // Parameter must be of Timestamp class
        Timestamp value = (Timestamp) criteria.getValue();

        if (criteria.getOperator() == SearchOperator.EQUALS) {
            predicates.add(builder.equal(expression, value));
        } else if (criteria.getOperator() == SearchOperator.GREATER) {
            predicates.add(builder.greaterThan(expression, value));
        } else if (criteria.getOperator() == SearchOperator.LOWER) {
            predicates.add(builder.lessThan(expression, value));
        } else if (criteria.getOperator() == SearchOperator.GREATER_EQUALS) {
            predicates.add(builder.greaterThanOrEqualTo(expression, value));
        } else if (criteria.getOperator() == SearchOperator.LOWER_EQUALS) {
            predicates.add(builder.lessThanOrEqualTo(expression, value));
        }
    }

    /**
     * Adds a Boolean predicate related to the operator.
     * Validates the operator, throwing an exception if needed.
     * 
     * @param predicates
     * @param builder
     * @param expression
     * @param operator
     * @param value
     * 
     */
    private void addPredicateBoolean(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Expression<Boolean> expression,
            SearchCriteria criteria) {

        // Parameter must be of Timestamp class
        Boolean value = (Boolean) criteria.getValue();

        predicates.add(builder.equal(expression, value));
    }

    /**
     * Creates a search predicate.
     * 
     * Throws an exception in case some error in the parameters arrises.
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        // Create a new predicate list
        List<Predicate> predicates = new ArrayList<>();
        // In case some joins are needed
        Join<Object, Object> previousJoin;
        String key;

        // Convert all criterias to predicates
        for (SearchCriteria criteria : list) {

            key = criteria.getKey();

            Join<Object, Object> tableJoin;
            previousJoin = null;

            // Split the key in order to treat join fields
            // Last component is the field name. Previous components are join fields
            String[] path = key.split("\\.");
            for (int i = 0; i < path.length; i++) {
                if (i == (path.length - 1)) {
                    // Last path component, so it's the field name
                    key = path[i];
                    if (previousJoin == null) {
                        // There is no previous join, so use root
                        // if (criteria.getValueType() == SearchSpecification.SearchType.DATE_TIME) {
                        // addPredicateTimestamp(predicates, builder, root.<Timestamp>get( key ),
                        // criteria);
                        // } else {
                        // addPredicateString(predicates, builder, root.get( key ), criteria);
                        // }
                        switch (criteria.getValueType()) {
                            case DATE_TIME:
                                addPredicateTimestamp(predicates, builder, root.<Timestamp>get(key), criteria);
                                break;
                            case BOOLEAN:
                                addPredicateBoolean(predicates, builder, root.<Boolean>get(key), criteria);
                                break;
                            default:
                                addPredicateString(predicates, builder, root.get(key), criteria);
                        }
                    } else {
                        // There is previous join, so use it
                        // if (criteria.getValueType() == SearchSpecification.SearchType.DATE_TIME) {
                        // addPredicateTimestamp(predicates, builder, previousJoin.<Timestamp>get( key
                        // ), criteria);
                        // } else {
                        // addPredicateString(predicates, builder, previousJoin.get( key ), criteria);
                        // }
                        switch (criteria.getValueType()) {
                            case DATE_TIME:
                                addPredicateTimestamp(predicates, builder, previousJoin.<Timestamp>get(key), criteria);
                                break;
                            case BOOLEAN:
                                addPredicateBoolean(predicates, builder, previousJoin.<Boolean>get(key), criteria);
                                break;
                            default:
                                addPredicateString(predicates, builder, previousJoin.get(key), criteria);
                        }
                    }
                } else {
                    // Not the last component, so it's a join field
                    if (previousJoin == null) {
                        // There is no previous join, so use root
                        tableJoin = root.join(path[i]);
                    } else {
                        // There is previous join, so use it
                        tableJoin = previousJoin.join(path[i]);
                    }
                    previousJoin = tableJoin;
                }
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
