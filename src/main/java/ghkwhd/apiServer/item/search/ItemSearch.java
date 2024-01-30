package ghkwhd.apiServer.item.search;

import ghkwhd.apiServer.common.page.PageRequestDTO;
import ghkwhd.apiServer.common.page.PageResponseDTO;
import ghkwhd.apiServer.item.dto.ItemDTO;
import ghkwhd.apiServer.item.entity.Item;

import java.util.List;

public interface ItemSearch {

    PageResponseDTO<ItemDTO> searchList(PageRequestDTO requestDTO);
}
