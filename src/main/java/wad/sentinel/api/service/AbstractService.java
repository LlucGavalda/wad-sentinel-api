package wad.sentinel.api.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.apache.commons.lang3.ArrayUtils;

import wad.sentinel.api.utils.dto.SearchCriteriaDto;

public class AbstractService {

    /**
     * Prepares the pageable from the parameters to perform the search
     * 
     * @param pageNumber
     * @param pageSize
     * @param sortType
     * @param sortField
     * @return
     */
    public Pageable preparePageable(Integer pageNumber, Integer pageSize) {
        Pageable pageable;

        // Check page size
        if (pageSize < 0) {
            pageSize = Integer.MAX_VALUE;
        }

        // Check if order has to be applied
        pageable = PageRequest.of(pageNumber, pageSize);

        return pageable;
    }

    /**
     * Adds a filter that matches 'valid' = true
     * 
     * @param dtoArray
     * @return
     */
    public SearchCriteriaDto[] addFilterValid(SearchCriteriaDto[] dtoArray) {
        // Add filter valid = true
        dtoArray = ArrayUtils.add(
                dtoArray,
                new SearchCriteriaDto(
                        "disponible",
                        "equals",
                        "true"));
        return dtoArray;
    }
}
