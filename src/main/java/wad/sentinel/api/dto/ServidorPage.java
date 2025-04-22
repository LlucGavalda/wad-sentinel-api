package wad.sentinel.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import wad.sentinel.api.entity.Servidor;

public class ServidorPage extends AbstractPage {

    private List<ServidorDto> list;

    public ServidorPage() {
        super();
    }

    public ServidorPage(Page<Servidor> page) {
        super(page);

        list = page.stream()
                .map(ent -> ent.mapDto()).collect(Collectors.toList());
    }

    public List<ServidorDto> getList() {
        return list;
    }

    public void setList(List<ServidorDto> list) {
        this.list = list;
    }

}
