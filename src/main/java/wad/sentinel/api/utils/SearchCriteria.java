package wad.sentinel.api.utils;

public class SearchCriteria {

    private String key;
    private Object value;
    private SearchSpecification.SearchOperator operator;
    private SearchSpecification.SearchType valueType;

    /**
     * Convenience constructor for a new search criteria
     * 
     * @param key       Field name
     * @param value     Field value
     * @param operation Operation as of Constants.SEARCH_*
     */
    public SearchCriteria(String key, Object value, SearchSpecification.SearchOperator operator,
            SearchSpecification.SearchType valueType) {
        setKey(key);
        setValue(value);
        setOperator(operator);
        setValueType(valueType);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SearchSpecification.SearchOperator getOperator() {
        return operator;
    }

    public void setOperator(SearchSpecification.SearchOperator operator) {
        this.operator = operator;
    }

    public SearchSpecification.SearchType getValueType() {
        return valueType;
    }

    public void setValueType(SearchSpecification.SearchType valueType) {
        this.valueType = valueType;
    }

}