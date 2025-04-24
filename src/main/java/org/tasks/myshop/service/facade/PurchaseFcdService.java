package org.tasks.myshop.service.facade;

import org.springframework.ui.Model;

public interface PurchaseFcdService {

    public Model purchase(Model model, Long cartId);

}
