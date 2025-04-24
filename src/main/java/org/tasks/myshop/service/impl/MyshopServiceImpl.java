package org.tasks.myshop.service.impl;

import com.opencsv.CSVReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dao.model.ItemModel;
import org.tasks.myshop.dao.model.ItemPicsEntity;
import org.tasks.myshop.dao.repository.ItemPicsRepository;
import org.tasks.myshop.dao.repository.ItemRespository;
import org.tasks.myshop.dto.ItemDto;
import org.tasks.myshop.dto.ItemModelDto;
import org.tasks.myshop.dto.PagingDto;
import org.tasks.myshop.enums.SortEnum;
import org.tasks.myshop.exception.LoadItemException;
import org.tasks.myshop.exception.SortException;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.MyshopService;
import org.tasks.myshop.service.mapper.ItemMapper;
import org.tasks.myshop.service.mapper.ItemModelMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyshopServiceImpl implements MyshopService {

    private final int DEFAULT_PAGE_SIZE = 5;
    private final int DEFAULT_PAGE_NUMBER = 0;
    private final SortEnum DEFAULT_SORT = SortEnum.NO;
    private final String DEFAULT_SEARCH = "";

    private final ItemRespository itemRespository;
    private final ItemPicsRepository itemPicsRepository;
    private final ItemMapper  itemMapper;
    private final CartService  cartService;
    private final ItemModelMapper itemModelMapper;

    public MyshopServiceImpl(ItemRespository itemRespository, ItemPicsRepository itemPicsRepository, ItemMapper itemMapper, CartService cartService, ItemModelMapper itemModelMapper) {
        this.itemRespository = itemRespository;
        this.itemPicsRepository = itemPicsRepository;
        this.itemMapper = itemMapper;
        this.cartService = cartService;
        this.itemModelMapper = itemModelMapper;
    }

    @Override
    public Page<ItemEntity> getItems(String search, Integer pageSize, Integer pageNumber, SortEnum sortType) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortType.getSortField());
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
        return itemRespository.findByTitle(search, pageable);
    }

    @Override
    public Page<ItemModel> getItemsOverMinQuantity(String search, Integer pageSize, Integer pageNumber, SortEnum sortType, int minQuantity) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortType.getSortField());
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);

        return itemRespository.findByTitleAndOverMinQuantityNew(search, pageable, minQuantity);
    }

    @Override
    public Model getItemsModel(Model model, String search, Integer pageSize, Integer pageNumber, String sort) throws SortException {
        int pageLimit = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        int pageNo = pageNumber == null ? DEFAULT_PAGE_NUMBER : pageNumber - 1;
        SortEnum sortEnum = (sort == null || sort.isEmpty()) ? DEFAULT_SORT : SortEnum.getByValue(sort);
        String searchString = search == null ? DEFAULT_SEARCH : search;

        Page<ItemModel> pageItems = getItemsOverMinQuantity(searchString, pageLimit, pageNo, sortEnum, 1);
        List<ItemModelDto> items = pageItems.getContent().stream().map(itemModelMapper::toDto).toList();

        model.addAttribute("items", items);
        model.addAttribute("search", searchString);
        model.addAttribute("sort", sortEnum.getValue());
        model.addAttribute("paging", new PagingDto(pageLimit, pageNo+1, pageItems.getTotalPages()));
        return model;
    }

    @Override
    @Transactional
    public void loadItemsFromCsv(MultipartFile file, MultipartFile[] images) throws LoadItemException {
        try {
            List<String[]> records;
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReader(reader);
            records = csvReader.readAll();
            if (!records.isEmpty()) {
                records.remove(0); // удаляем заголовок csv-файла
            }

            List<ItemEntity> items = saveItems(records);
            saveImages(images, items, records);
        }catch (Exception e) {
            throw new LoadItemException(e.getMessage());
        }
    }

    @Override
    public ItemDto getItemById(Long id) {
        return itemRespository.findById(id).map(entity -> itemMapper.toDto(entity))
                .orElseThrow(()->new RuntimeException("Item not found"));
    }

    @Override
    @Transactional
    public Model changeItemCart(Model model, Long itemId, Long cartId, int delta) {
        CartEntity cart = cartService.updateCountItem(itemId, cartId, delta);
        ItemEntity item = updateCountItem(itemId, -delta);
        // ToDo
        model.addAttribute("item", itemMapper.toDto(item));
        model.addAttribute("countItem", cart.getCountItem());
        return model;
    }

    @Override
    public ItemEntity updateCountItem(Long itemId, int deltaCount) {
        return itemRespository
                .findById(itemId)
                .map(item -> {
                    item.setQuantity(item.getQuantity() + deltaCount);
                    return item;
                })
                .map(itemRespository::save)
                .orElseThrow();
    }

    private List<ItemEntity> saveItems(List<String[]> records) {
        List<ItemEntity> items = new ArrayList<>();

        records.forEach(strings -> {
            ItemEntity entity = new ItemEntity();
            entity.setTitle(strings[0]);
            entity.setDescription(strings[1]);
            entity.setPrice(BigDecimal.valueOf(Double.parseDouble(strings[2])));
            entity.setQuantity(Integer.valueOf(strings[3]));
            items.add(entity);
        });

        return itemRespository.saveAll(items);
    }

    private void saveImages(MultipartFile[] images, List<ItemEntity> items, List<String[]> records) throws IOException {
        if (items.size() != records.size()) {
            throw new RuntimeException("Ошибка сохранения товаров");
        }

        Map<String, MultipartFile> imagesMap = new HashMap<>();
        for (MultipartFile image : images) {
            imagesMap.put(image.getOriginalFilename(), image);
        }

        List<ItemPicsEntity> itemPics = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            MultipartFile imageFile = imagesMap.get(records.get(i)[4]);
            if (imageFile != null) {
                ItemPicsEntity  itemPic = new ItemPicsEntity();

                itemPic.setItemId(items.get(i).getId());
                itemPic.setImageType(imageFile.getContentType());
                itemPic.setImage(imageFile.getBytes());

                itemPics.add(itemPic);
            }
        }

        itemPicsRepository.saveAll(itemPics);
    }

}
