package jpabook.jpashop.service;

import jpabook.jpashop.controller.form.BookForm;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void edit(BookForm form) {
        itemRepository.edit(form);
    }

    public Item find(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}
