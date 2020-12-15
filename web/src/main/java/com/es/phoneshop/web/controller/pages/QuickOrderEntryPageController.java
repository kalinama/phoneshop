package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.core.cart.service.exception.PhoneNotFoundException;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.service.PhoneService;
import com.es.phoneshop.web.entity.QuickOrderEntriesForm;
import com.es.phoneshop.web.entity.QuickOrderEntry;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/quickOrderEntry")
public class QuickOrderEntryPageController {
    @Resource
    private Validator quickOrderEntriesFormValidator;

    @Resource(name = "httpSessionCartService")
    private CartService cartService;

    @Resource(name = "defaultPhoneService")
    private PhoneService phoneService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(quickOrderEntriesFormValidator);
    }

    @GetMapping
    public String showQuickOrderPage(Model model) {
        model.addAttribute("quickOrderEntriesForm", new QuickOrderEntriesForm());
        return "quickOrderEntry";
    }

    @PostMapping
    public String addToCart(@Valid @ModelAttribute QuickOrderEntriesForm quickOrderEntriesForm,
                            BindingResult bindingResult, Model model,
                            HttpSession httpSession) throws ParseException {

        List<String> success = new ArrayList<>();
        Cart cart = cartService.getCart(httpSession);
        List<QuickOrderEntry> entries = quickOrderEntriesForm.getEntries();

        for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
            QuickOrderEntry entry = entries.get(entryIndex);

            if (isValidEntry(bindingResult, entryIndex, entry)) {
                Phone phone = phoneService.getByModel(entry.getModel()).get();
                cartService.addPhone(cart, phone.getId(), parseQuantity(entry.getQuantity()));

                success.add(entry.getModel() + "Successfully added");
                entries.set(entryIndex, new QuickOrderEntry());
            }
        }
        model.addAttribute("quickOrderEntriesForm", quickOrderEntriesForm);
        model.addAttribute("success", success);
        return "quickOrderEntry";
    }

    private boolean isValidEntry(BindingResult bindingResult, int entryIndex, QuickOrderEntry entry) {
        return !bindingResult.hasFieldErrors("entries[" + entryIndex + "].model")
                && !bindingResult.hasFieldErrors("entries[" + entryIndex + "].quantity")
                && !entry.getModel().isEmpty()
                && !entry.getQuantity().isEmpty();
    }

    private Long parseQuantity(String quantityInput) throws ParseException {
        Locale locale = LocaleContextHolder.getLocale();
        return NumberFormat.getInstance(locale)
                .parse(quantityInput)
                .longValue();
    }

}
