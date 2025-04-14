package wad.sentinel.api.dto;

import org.springframework.data.domain.Page;

public abstract class AbstractPage {

    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;

    public AbstractPage() {
        super();
    }

    public AbstractPage(Page<?> page) {
        copyStructureFrom(page);
    }

    public void copyStructureFrom(Page<?> page) {
        this.setPageNumber(page.getNumber());
        this.setPageSize(page.getSize());
        this.setTotalElements(page.getTotalElements());
        this.setTotalPages(page.getTotalPages());
        this.setIsLast(page.isLast());
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }
}
