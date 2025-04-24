package org.tasks.myshop.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tasks.myshop.dto.ItemDto;
import org.tasks.myshop.exception.SortException;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.MyshopService;
import org.tasks.myshop.service.facade.CartChangeFcdService;

@Controller
@RequestMapping("/myshop")
public class MyshopController {

    private final MyshopService myshopService;
    private final CartService cartService;
    private final CartChangeFcdService cartChangeFcdService;

    public MyshopController(MyshopService myshopService, CartService cartService, CartChangeFcdService cartChangeFcdService) {
        this.myshopService = myshopService;
        this.cartService = cartService;
        this.cartChangeFcdService = cartChangeFcdService;
    }

    @GetMapping
    public String getItems(
            Model model,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) throws SortException {
        model = myshopService.getItemsModel(model, search, pageSize, pageNumber, sort);
        return "main";
    }

    @GetMapping("/item/{id}")
    public String getItem(Model model, @PathVariable("id") Long id) {
        ItemDto itemDto = myshopService.getItemById(id);
        int countItem = cartService.getCountItemOrZeroIfAbsent(id, 1L);
        model.addAttribute("item", itemDto);
        model.addAttribute("countItem", countItem);
        return "item";
    }

    @PostMapping("/item/{id}/cart")
    public String changeItemCart(
            Model model,
            @PathVariable("id") Long id,
            @RequestParam("action") @Min(-1) @Max(1) int delta) {
        model = myshopService.changeItemCart(model, id, 1L, delta);
        return "item";
    }

    @PostMapping("/item/{id}/changecart")
    public String changeItemCartFromMain(
            Model model,
            @PathVariable("id") Long id,
            @RequestParam("action") String action) {
        Long cartId = 1L; // эмуляция 1-й корзины, в дальнейшем можно брать номер из авторизации user (напр, id user'а)
        cartChangeFcdService.updateItemInCart(cartId,  id, action);

        return "redirect:/myshop";
    }

}
