package wad.sentinel.api.utils.dto;

public class SearchCriteriaDto {

    private String logicalOperator = "AND";
    private String field;
    private String operator;
    private String value;

    public SearchCriteriaDto() {
        super();
    }

    /**
     * Convenience constructor
     * 
     * @param field
     * @param operator
     * @param value
     */
    public SearchCriteriaDto(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

}