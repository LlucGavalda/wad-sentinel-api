package wad.sentinel.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import wad.sentinel.api.entity.Monitorizacion;

public class MonitorizacionPage extends AbstractPage {

    private List<MonitorizacionDto> list;

    public MonitorizacionPage() {
        super();
    }

    public MonitorizacionPage(Page<Monitorizacion> page) {
        super(page);

        list = page.stream()
                .map(ent -> ent.mapDto()).collect(Collectors.toList());
    }

    public List<MonitorizacionDto> getList() {
        return list;
    }

    public void setList(List<MonitorizacionDto> list) {
        this.list = list;
    }

}
