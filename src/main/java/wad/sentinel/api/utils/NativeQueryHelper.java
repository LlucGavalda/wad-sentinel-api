package wad.sentinel.api.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.Formula;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ch.qos.logback.classic.Logger;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import wad.sentinel.api.entity.AbstractEntity;
import wad.sentinel.api.exceptions.BadLogicalOperatorsException;
import wad.sentinel.api.exceptions.BadQueryException;
import wad.sentinel.api.exceptions.BadQueryParenthisationException;
import wad.sentinel.api.exceptions.ParameterInvalidException;
import wad.sentinel.api.exceptions.PropertyNotFoundException;
import wad.sentinel.api.utils.SearchSpecification.SearchType;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

public class NativeQueryHelper {

    // Structured field info
    protected class QueryField {
        String fieldName;
        boolean replaced = false; // Means that the field name has been replaced by some expression
        String tableAlias = null; // Alias of the table where the field is in

        public QueryField(String fieldName) {
            this.fieldName = fieldName;
            this.replaced = false;
        }

        public QueryField(String fieldName, String tableAlias) {
            this.fieldName = fieldName;
            this.tableAlias = tableAlias;
        }

        public QueryField(String fieldName, boolean replaced) {
            this.fieldName = fieldName;
            this.replaced = replaced;
        }
    }

    // Logger utility
    private static final Logger logger = (Logger) LoggerFactory.getLogger(NativeQueryHelper.class);

    private Class<?> entityClass; // Base entity
    private String tableName; // Table name from the entity
    private List<QueryField> selectFields = new ArrayList<QueryField>(); // Fields to be returned as in the SELECT
                                                                         // clause
    private List<String> excludeFields = new ArrayList<String>(); // Fields to exclude from the SELECT clause, if needed
    private SearchCriteriaDto[] searchCriteria; // To compose the WHERE clause
    // private Map<String, Class<?>> joinFields = null;
    private Map<String, SearchSpecification.SearchType> searchableFields = null;
    private Map<String, String> whereFieldsMap = new HashMap<String, String>(); // In case there is a need to change
                                                                                // some field in the WHERE clause, map
                                                                                // it here
    private String sortField; // Field to have in the ORDER clause
    private String sortType; // Sort order ASC or DESC
    private Map<String, Class<?>> fieldJoinTable = new HashMap<String, Class<?>>();
    private Map<String, String> fieldJoinTableAlias = new HashMap<String, String>();
    private int superClasses = 0;
    private boolean joinSuperclasses = false; // Forces to create joins with the tables corresponding to the entity
                                              // superclasses.
                                              // Does make sense only if setFieldsSuperClasses in the constructor has
                                              // been set to >0. Otherwise has no effect.
    private List<Class<?>> subClasses = new ArrayList<Class<?>>(); // List of subclasses to add to the query, to manage
                                                                   // tables hierarchy

    public NativeQueryHelper() {
        // Default constructor
    }

    /**
     * Regular constructor to initialise all the entity fields.
     * Gets the entity and the table name from the entityClass.
     * Gets all the fields from the entity up to setFieldsSuperClasses super
     * classes. Set to <0 if not needed.
     * 
     * @param entityClass
     * @param setFieldsSuperClasses
     */
    public NativeQueryHelper(Class<?> entityClass, int setFieldsSuperClasses) {
        this.superClasses = setFieldsSuperClasses;
        setEntityClass(entityClass, setFieldsSuperClasses);
    }

    /**
     * Regular constructor to initialise all the entity fields with some fields
     * excluded.
     * Gets the entity and the table name from the entityClass.
     * Gets all the fields from the entity up to setFieldsSuperClasses super
     * classes. Set to <0 if not needed.
     * 
     * @param entityClass
     * @param setFieldsSuperClasses
     */
    public NativeQueryHelper(Class<?> entityClass, int setFieldsSuperClasses, List<String> excludeFields) {
        this.excludeFields = excludeFields;
        this.superClasses = setFieldsSuperClasses;
        setEntityClass(entityClass, setFieldsSuperClasses);
    }

    /**
     * Regular constructor to initialise all the entity fields with some fields
     * excluded and forcing joins with superclasses' tables.
     * Gets the entity and the table name from the entityClass.
     * Gets all the fields from the entity up to setFieldsSuperClasses super
     * classes. Set to <0 if not needed.
     * 
     * @param entityClass
     * @param setFieldsSuperClasses
     */
    public NativeQueryHelper(Class<?> entityClass, int setFieldsSuperClasses, List<String> excludeFields,
            Boolean joinSuperclasses) {
        if (excludeFields != null)
            this.excludeFields = excludeFields;
        if (joinSuperclasses != null)
            this.joinSuperclasses = joinSuperclasses;
        this.superClasses = setFieldsSuperClasses;

        setEntityClass(entityClass, setFieldsSuperClasses);
    }

    protected void setEntityClass(Class<?> entityClass, int setFieldsSuperClasses) {
        this.entityClass = entityClass;
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            this.tableName = table.name();
        }
        setAllFields(setFieldsSuperClasses);
        setSearchableFields();
        // setJoinFields();
    }

    /**
     * Adds a subclass entity. All its table fields will be added to the query via a
     * LEFT JOIN with
     * the main table.
     * The table alias will be STn, where n starts with 0.
     */
    public void addSubClass(Class<?> cl) {
        // Add all fields
        setAllFieldsForClass(cl, "ST" + subClasses.size());
        // Save the class for further actions
        subClasses.add(cl);
    }

    /**
     * Returns the complete native query, ready to be executed
     * 
     * @return
     */
    public String getNativeQuery() {
        String res;

        String selectQuery = prepareNativeQuerySelect(selectFields);
        String joinQuery = prepareNativeQueryJoin();
        String joinQuerySuperclasses = prepareNativeQueryJoinSuperclasses();
        String joinQuerySubclasses = prepareNativeQueryJoinSubclasses();
        String fromQuery = prepareNativeQueryFrom(tableName);
        String whereQuery = prepareNativeQueryWhere(searchCriteria, whereFieldsMap);
        String sortQuery = prepareNativeQueryOrder(sortField, sortType);

        res = selectQuery + fromQuery + joinQuerySuperclasses + joinQuerySubclasses + joinQuery + whereQuery
                + sortQuery;

        return res;
    }

    /**
     * Returns the complete native query count, for pagination e.g., ready to be
     * executed
     * 
     * @return
     */
    public String getNativeQueryCount() {
        String selectQueryCount = prepareNativeQuerySelectCount();
        String fromQuery = prepareNativeQueryFrom(tableName);
        String joinQuery = prepareNativeQueryJoin();
        String joinQuerySuperclasses = prepareNativeQueryJoinSuperclasses();
        String whereQuery = prepareNativeQueryWhere(searchCriteria, whereFieldsMap);

        return selectQueryCount + fromQuery + joinQuerySuperclasses + joinQuery + whereQuery;
    }

    /**
     * Prepares the clause SELECT of the query
     * 
     * @param fields
     * @return
     */
    protected String prepareNativeQuerySelect(List<QueryField> fields) {
        String result = "SELECT ";
        boolean addComma = false;

        for (QueryField field : fields) {
            if (addComma)
                result = result + ", ";
            if (field.replaced)
                result = result + field.fieldName; // Do not add the table alias
            else
                result = result + field.tableAlias + "." + field.fieldName; // Plain field, add the table alias
            addComma = true;
        }

        if (subClasses.size() > 0)
            result = result + ", 0 as clazz_ ";

        return result;
    }

    /**
     * Prepares the clause SELECT COUNT of the query, mainly to paginate
     * 
     * @return
     */
    protected String prepareNativeQuerySelectCount() {
        String result = "SELECT count(*)";

        return result;
    }

    /**
     * Prepares the clause FROM of the query
     * 
     * @param tableName
     * @return
     */
    protected String prepareNativeQueryFrom(String tableName) {
        String result = " FROM " + tableName + " T0 ";

        return result;
    }

    /**
     * Prepares the clause INNER JOIN of the query, in case it's needed
     * 
     * @param joinFields
     * @return
     */
    protected String prepareNativeQueryJoin() {
        String result = "";
        int joinNumber = 1;
        String previousTable = "";
        Class<?> currentEntityClass = this.getEntityClass();

        if (searchCriteria != null) {
            // Look for search fields that indicate joins, with a '.'
            for (SearchCriteriaDto criteria : searchCriteria) {
                if (criteria.getField() != null) {
                    // Check if the field is in the form "field1.field2.fiel3"
                    String[] fields = criteria.getField().split("\\.");
                    String field;
                    for (int i = 0; i < fields.length - 1; i++) {
                        // We have some fields separated by ".", so a join to implement

                        field = fields[i];

                        // Get the column name of the join field
                        String columnName = getJoinColumnName(currentEntityClass, field);

                        // Get the table name of the join table
                        Class<?> joinEntityClass = getFieldClass(currentEntityClass, field);
                        Table table = joinEntityClass.getAnnotation(Table.class);
                        String tableName = table.name();
                        String aliasTable = "J" + joinNumber;

                        // Create the join
                        if (i == 0) // Previous table is main table T0 (or main structure if superclasses... T1, T2,
                                    // ...)
                            result = result + " INNER JOIN " + tableName + " " + aliasTable + " ON "
                                    + getTableAliasForColumn(columnName) + "." + columnName + " = " + aliasTable
                                    + ".id ";
                        else // Inside a deeper join
                            result = result + " INNER JOIN " + tableName + " " + aliasTable + " ON " + previousTable
                                    + "." + columnName + " = " + aliasTable + ".id ";

                        // Save the table class and alias related to the field for future references
                        fieldJoinTable.put(field, joinEntityClass);
                        fieldJoinTableAlias.put(field, aliasTable);

                        joinNumber++;
                        previousTable = aliasTable;
                        currentEntityClass = joinEntityClass;
                    }
                    // Reset values for the next field
                    previousTable = "";
                    currentEntityClass = this.getEntityClass();

                }
            }
        }
        return result;
    }

    protected String prepareNativeQueryJoinSuperclasses() {
        String result = "";
        String tableName = "";
        String alias;

        if (joinSuperclasses) {
            Class<?> currentClass = this.entityClass;
            for (int i = 0; i <= superClasses; i++) {
                Table table = currentClass.getAnnotation(Table.class);
                if (table != null) {
                    tableName = table.name(); // If table found, get its name
                } // Otherwise will keep the same table name as before. This is the case of a
                  // superclass entity that does not map to a different table (AbstractEntity,
                  // e.g.)
                alias = "T" + i;
                if (i > 0) { // Skip the main table
                    result = result + " INNER JOIN " + tableName + " " + alias + " ON T0.id = " + alias + ".id";
                }
                currentClass = currentClass.getSuperclass();
            }
        }

        return result;
    }

    /**
     * Prepares the clause WHERE of the query
     * 
     * @param searchCriteria
     * @param mapFields
     * @return
     */
    protected String prepareNativeQueryWhere(SearchCriteriaDto[] searchCriteria, Map<String, String> mapFields) {
        String where = "";

        // Check searchableFields
        if (this.searchableFields == null)
            throw new PropertyNotFoundException(this.entityClass.getSimpleName(), "SearchableFields");
        SearchSpecification.checkSearchCriteria(searchCriteria, this.searchableFields);

        if (searchCriteria != null) {
            // There are conditions, so prepare the WHERE clause
            where = where + " WHERE ( ";

            // If no operator is defined, use "AND" by default
            boolean insertAnd = false;
            // AND or OR is not accepted in the next position
            boolean acceptedAndOr = false;

            // Open parenthesis cannot be negative (more closed than opened) and at the end
            // must be 0 (all open have been closed)
            int openParenthesis = 0;

            for (SearchCriteriaDto criteria : searchCriteria) {

                SearchSpecification.SearchOperator operator = SearchSpecification.searchOperators
                        .get(criteria.getOperator());
                if (operator == SearchSpecification.SearchOperator.OPEN_PAR) {
                    if (insertAnd)
                        where = where + " AND";
                    where = where + " (";
                    openParenthesis = openParenthesis + 1;
                    insertAnd = false;
                    acceptedAndOr = false;
                } else if (operator == SearchSpecification.SearchOperator.CLOSE_PAR) {
                    where = where + " )";
                    openParenthesis = openParenthesis - 1;
                    if (openParenthesis < 0)
                        throw new BadQueryParenthisationException();
                    insertAnd = true;
                    acceptedAndOr = true;
                } else if (operator == SearchSpecification.SearchOperator.AND) {
                    if (!acceptedAndOr)
                        throw new BadLogicalOperatorsException();
                    where = where + " AND";
                    insertAnd = false;
                    acceptedAndOr = false;
                } else if (operator == SearchSpecification.SearchOperator.OR) {
                    if (!acceptedAndOr)
                        throw new BadLogicalOperatorsException();
                    where = where + " OR";
                    insertAnd = false;
                    acceptedAndOr = false;
                } else {

                    if (insertAnd)
                        where = where + " AND";

                    // Not parenthesis or logical operator, so go for the field
                    String originalField = criteria.getField();
                    SearchSpecification.SearchType type = searchableFields.get(originalField);
                    if (type == null)
                        throw new ParameterInvalidException("SearchableFields", originalField);

                    // Check if this is a join field
                    String[] splitOriginalField = originalField.split("\\.");
                    String columnName;
                    if (splitOriginalField.length == 1) {
                        // This is not a join field

                        if (mapFields.get(originalField) != null) {
                            // There is a map for the field, so use the field in the map
                            columnName = mapFields.get(originalField);
                            // Don't add the table alias
                        } else {
                            // Get the column name of the field
                            columnName = getColumnName(this.getEntityClass(), originalField);
                            if (columnName == null)
                                throw new ParameterInvalidException("SearchableFields", originalField);
                            // Add the main table alias
                            columnName = getTableAliasForColumn(columnName) + "." + columnName;
                        }

                    } else {
                        // Join field

                        // Get the column name of the field
                        int fieldPos = splitOriginalField.length;
                        String joinFieldName = splitOriginalField[fieldPos - 2];
                        String fieldName = splitOriginalField[fieldPos - 1];
                        Class<?> joinEntityClass = fieldJoinTable.get(joinFieldName);
                        columnName = getColumnName(joinEntityClass, fieldName);
                        if (columnName == null)
                            throw new ParameterInvalidException("SearchableFields", originalField);
                        // Add the table alias
                        columnName = fieldJoinTableAlias.get(joinFieldName) + "." + columnName;

                    }

                    // Prepare the where clause
                    switch (type) {
                        case BOOLEAN:
                            where = where + " " + whereBoolean(columnName, criteria);
                            break;
                        default:
                            where = where + " " + whereString(columnName, criteria);
                    }

                    insertAnd = true;
                    acceptedAndOr = true;
                }
            }

            where = where + " )";

            if (openParenthesis != 0)
                throw new BadQueryParenthisationException();

        }
        return where;
    }

    protected String whereString(String field, SearchCriteriaDto criteria) {
        String where = "";
        switch (criteria.getOperator()) {
            case "equals":
                where = where + " " + field + " = '" + criteria.getValue() + "'";
                break;
            case "begins":
                where = where + " " + field + " like '" + criteria.getValue() + "%'";
                break;
            case "contains":
                where = where + " " + field + " like '%" + criteria.getValue() + "%'";
                break;
            case "greater":
                where = where + " " + field + " > '" + criteria.getValue() + "'";
                break;
            case "lower":
                where = where + " " + field + " < '" + criteria.getValue() + "'";
                break;
            case "greaterEquals":
                where = where + " " + field + " >= '" + criteria.getValue() + "'";
                break;
            case "lowerEquals":
                where = where + " " + field + " <= '" + criteria.getValue() + "'";
                break;
            default:
                throw new ParameterInvalidException("Operator", criteria.getOperator());
        }
        return where;
    }

    protected String whereBoolean(String field, SearchCriteriaDto criteria) {
        String where = "";

        Boolean condition = Boolean.parseBoolean(criteria.getValue().trim());
        switch (criteria.getOperator()) {
            case "equals":
                if (condition)
                    where = field;
                else
                    where = "NOT " + field;
                break;
            default:
                throw new ParameterInvalidException("Operator", criteria.getOperator());
        }
        return where;
    }

    protected String prepareNativeQueryOrder(String sortField, String sortType) {
        String sortQuery = "";
        if (sortField != null) {
            // Get the column name of the field
            String columnName = getColumnName(this.getEntityClass(), sortField);
            if (columnName == null)
                throw new ParameterInvalidException("SortField", sortField);
            sortQuery = sortQuery + " ORDER BY " + columnName;
            if (sortType != null)
                sortQuery = sortQuery + " " + sortType;
        }

        return sortQuery;
    }

    // public void setFields(String[] fields) {
    // for (String fieldName : fields) {
    // this.selectFields.add(new QueryField(fieldName));
    // }
    // }

    /**
     * Sets all the fields from the provided class to the field list.
     * The parameter superClasses is the number of superclasses' fields to be added
     * as well.
     * 
     * @param c
     * @param superClass
     */
    public void setAllFields(int superClasses) {
        Class<?> currentClass = this.entityClass;
        String tableAlias = "T0";
        for (int i = 0; i <= superClasses; i++) {
            if (joinSuperclasses)
                tableAlias = "T" + i; // If join then the alias changes for every table
            setAllFieldsForClass(currentClass, tableAlias);
            currentClass = currentClass.getSuperclass();
        }
    }

    /**
     * Adds all the class/table fields
     * 
     * @param cl
     * @param tableAlias
     */
    protected void setAllFieldsForClass(Class<?> cl, String tableAlias) {
        for (Field f : cl.getDeclaredFields()) {
            if (!excludeFields.contains(f.getName())) {
                // Try to get a table column
                Column column = f.getAnnotation(Column.class);
                // Try to get a table join column
                JoinColumn joinColumn = f.getAnnotation(JoinColumn.class);
                Formula formulaColumn = f.getAnnotation(Formula.class);
                if (column != null) {
                    this.selectFields.add(new QueryField(column.name(), tableAlias));
                }
                if (joinColumn != null)
                    this.selectFields.add(new QueryField(joinColumn.name(), tableAlias));

                if (formulaColumn != null) {
                    // check if the formula contains an isolated 'id' with spaces around it
                    if (formulaColumn.value().matches(".*(?<=\\s|^)id(?=\\s*=?\\s*[^a-zA-Z0-9_]).*")) {
                        // if we dont change formula id with table reference we get ambiguous id error.
                        String newFormString = formulaColumn.value()
                                .replaceFirst("(?<=\\s|^)id(?=\\s*=?\\s*[^a-zA-Z0-9_])", tableAlias + ".id");
                        this.selectFields.add(new QueryField(newFormString + " as " + f.getName(), true));
                    } else {
                        // add the field without modification
                        this.selectFields.add(new QueryField(formulaColumn.value() + " as " + f.getName(), true));
                    }
                }

            }
        }

    }

    // /**
    // * Gets and saves the SEARCHABLE_FIELDS property of the stored entity class
    // */
    // @SuppressWarnings("unchecked")
    // protected void setJoinFields() {
    // try {
    // Field field = entityClass.getDeclaredField("SEARCHABLE_FIELDS_JOINS");
    // this.joinFields = (Map<String, Class<?>>) field.get(null);
    // } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
    // | IllegalAccessException e) {
    // // Field does not exist
    // }
    // }

    /**
     * Gets and saves the SEARCHABLE_FIELDS property of the stored entity class
     */
    @SuppressWarnings("unchecked")
    protected void setSearchableFields() {
        try {
            Field field = entityClass.getDeclaredField("SEARCHABLE_FIELDS");
            this.searchableFields = (Map<String, SearchType>) field.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // Field does not exist
        }
    }

    /**
     * Replaces a field of the actual list of fields.
     * 
     * @param oldField
     * @param newField
     */
    public void replaceSelectField(String oldField, String newField) {
        for (QueryField f : this.selectFields) {
            if (f.fieldName.equalsIgnoreCase(oldField)) {
                f.fieldName = newField;
                f.replaced = true;
            }
        }
    }

    /**
     * Replaces a field of the actual list of fields for an expression, maintaining
     * its name.
     * 
     * @param fieldName
     * @param expression
     */
    public void replaceSelectPropertyForExpression(String propertyName, String expression) {
        // Get the field name
        String fieldName = getColumnName(this.getEntityClass(), propertyName);

        // Replace the column
        for (QueryField f : this.selectFields) {
            if (f.fieldName.equalsIgnoreCase(fieldName)) {
                f.fieldName = expression + " as " + fieldName;
                f.replaced = true;
            }
        }
    }

    @SuppressWarnings("unchecked") // To avoid the cast of the result of query.getResultList()
    public PageImpl<?> getQueryResultPage(EntityManager entityManager, Pageable pageable) {
        // Create the main query and execute
        String sQuery = this.getNativeQuery();
        logger.info("Executing native query: " + sQuery);
        logger.info("Executing native query upon: " + this.getEntityClass().getName());
        Query query = entityManager.createNativeQuery(sQuery, this.getEntityClass());

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<AbstractEntity> resultList;
        try {
            resultList = query.getResultList();
        } catch (Exception ex) {
            throw new BadQueryException();
        }
        // Create the COUNT query, set parameters and execute
        String sQueryCount = this.getNativeQueryCount();
        logger.info("Executing native query: " + sQueryCount);
        Query queryCount = entityManager.createNativeQuery(sQueryCount);

        long resultCount;
        try {
            resultCount = (long) queryCount.getSingleResult();
        } catch (Exception ex) {
            throw new BadQueryException();
        }

        return new PageImpl<AbstractEntity>(resultList, pageable, resultCount);
    }

    @SuppressWarnings("unchecked") // To avoid the cast of the result of query.getResultList()
    public PageImpl<?> getQueryResultPageCustomQuery(EntityManager entityManager, Pageable pageable,
            String sQueryCount) {
        // Create the main query and execute
        String sQuery = this.getNativeQuery();
        logger.info("Executing native query: " + sQuery);
        logger.info("Executing native query upon: " + this.getEntityClass().getName());
        Query query = entityManager.createNativeQuery(sQuery, this.getEntityClass());

        List<AbstractEntity> resultList;
        try {
            resultList = query.getResultList();
        } catch (Exception ex) {
            throw new BadQueryException();
        }

        logger.info("Executing native query: " + sQueryCount);
        Query queryCount = entityManager.createNativeQuery(sQueryCount);

        long resultCount;
        try {
            resultCount = (long) queryCount.getSingleResult();
        } catch (Exception ex) {
            throw new BadQueryException();
        }

        return new PageImpl<AbstractEntity>(resultList, pageable, resultCount);
    }

    /**
     * Checks the searchCriteria against the entityClass.SEARCHABLE_FIELDS provided.
     * If the entityClass does not have the field SEARCHABLE_FIELDS, no check is
     * performed.
     * If some check fails, an exception is thrown.
     * 
     * @param searchCriteria
     */
    @SuppressWarnings("unchecked") // Unsafe cast in (Map<String, SearchType>)field.get(null)
    public void setSearchCriteria(SearchCriteriaDto[] searchCriteria) {
        try {
            Field field;
            field = entityClass.getDeclaredField("SEARCHABLE_FIELDS");
            if (field != null) {
                // Check all fields
                SearchSpecification.checkSearchCriteria(searchCriteria, (Map<String, SearchType>) field.get(null));
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // Field not found in class, so just omit the check
        }

        this.searchCriteria = searchCriteria;
    }

    public void setWhereFieldsMap(Map<String, String> fieldsMap) {
        this.whereFieldsMap = fieldsMap;
    }

    public void addWherePropertyMap(String field1, String field2) {
        this.whereFieldsMap.put(field1, field2);
    }

    public void setSort(String sortField, String sortType) {
        if (sortField != null) {
            getColumnName(entityClass, sortField);

            this.sortField = sortField;
            this.sortType = sortType;
        }
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Returns the column name of the property in the entity class
     * 
     * @param c
     * @param property
     * @return
     */
    public String getColumnName(Class<?> c, String property) {
        String columnName = null;
        Field field = null;

        // Look for the field in the class and its superclasses
        while ((field == null) && (c != null)) {
            try {
                // Try to find the field in the current class
                field = c.getDeclaredField(property);
            } catch (Exception e) {
                // Not found, do nothing and continue
            }
            // If not found, try the superclass
            c = c.getSuperclass();
        }

        if ((field != null) && field.getName().equalsIgnoreCase(property)) {
            // Field has been found; try to get the column name
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                // Found. Return it
                columnName = column.name();
            }
        }

        return columnName;
    }

    /**
     * Returns the join column name of the property in the entity class
     * 
     * @param c
     * @param property
     * @return
     */
    public String getJoinColumnName(Class<?> c, String property) {
        String columnName = null;
        Field field = null;

        // Look for the field in the class and its superclasses
        while ((field == null) && (c != null)) {
            try {
                // Try to find the field in the current class
                field = c.getDeclaredField(property);
            } catch (Exception e) {
                // Not found, do nothing and continue
            }
            // If not found, try the superclass
            c = c.getSuperclass();
        }

        if ((field != null) && field.getName().equalsIgnoreCase(property)) {
            // Field has been found; try to get the column name
            JoinColumn column = field.getAnnotation(JoinColumn.class);
            if (column != null) {
                // Found. Return it
                columnName = column.name();
            }
        }

        return columnName;
    }

    /**
     * Returns the class of the field in the entity class
     * 
     * @param c
     * @param property
     * @return
     */
    public Class<?> getFieldClass(Class<?> c, String property) {
        Class<?> result = null;
        Field field = null;

        // Look for the field in the class and its superclasses
        while ((field == null) && (c != null)) {
            try {
                // Try to find the field in the current class
                field = c.getDeclaredField(property);
            } catch (Exception e) {
                // Not found, do nothing and continue
            }
            // If not found, try the superclass
            c = c.getSuperclass();
        }

        if ((field != null) && field.getName().equalsIgnoreCase(property)) {
            // Field has been found; get the class
            result = field.getType();
        }

        return result;
    }

    /**
     * Finds the table alias in the list of fields for the received column name and
     * returns it
     * 
     * @param columnName
     * @return
     */
    protected String getTableAliasForColumn(String columnName) {
        String res = "";
        for (QueryField field : selectFields) {
            if (field.fieldName.equalsIgnoreCase(columnName))
                return field.tableAlias;
        }
        return res;
    }

    /**
     * Creates the join corresponding to the subclasses.
     * It will set table aliases STn, starting with n=0.
     * 
     * @return
     */
    protected String prepareNativeQueryJoinSubclasses() {
        String res = "";

        int nSc = 0;
        for (Class<?> c : subClasses) {
            Table table = c.getAnnotation(Table.class);
            if (table != null) {
                String tableName = table.name();
                String tableAlias = "ST" + nSc;
                res = res + " LEFT JOIN " + tableName + " " + tableAlias + " ON T0.id=" + tableAlias + ".id ";
            }
            nSc++;
        }
        return res;
    }
}
