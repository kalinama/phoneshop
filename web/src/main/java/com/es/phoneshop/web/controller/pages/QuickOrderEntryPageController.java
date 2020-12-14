package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.core.phone.dao.PhoneDao;
import com.es.phoneshop.web.controller.helper.BindingResultHelper;
import com.es.phoneshop.web.entity.ErrorMessageForQuickOrder;
import com.es.phoneshop.web.entity.Model2QuantityInputUnit;
import com.es.phoneshop.web.entity.TypeOfErrorMessageForQuickOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/quickOrderEntry")
public class QuickOrderEntryPageController {

    @Resource(name = "model2QuantityInputValidator")
    private Validator validator;

    @Resource(name = "defaultBindingResultHelper")
    private BindingResultHelper bindingResultHelper;

    @Resource(name = "httpSessionCartService")
    private CartService cartService;

    @Resource(name = "jdbcPhoneDao")
    private PhoneDao phoneDao;


    @GetMapping
    public String get() {
        return "quickOrderEntry";
    }

    @PostMapping
    public String post(@RequestParam(value = "model") List<String> models,
                       @RequestParam(value = "quantity") List<String> quantities,
                       Model model, Locale locale, HttpSession httpSession) throws ParseException {

        Map<Integer, List<ErrorMessageForQuickOrder>> errors = new HashMap<>();
        List<String> success = new ArrayList<>();
        Cart cart = cartService.getCart(httpSession);

        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).isEmpty()&& quantities.get(i).isEmpty()) continue;
            Model2QuantityInputUnit inputUnit = new Model2QuantityInputUnit(models.get(i), quantities.get(i), locale);
            BindingResult bindingResult = bindingResultHelper.getBindingResult(inputUnit, validator);
            if (bindingResult.hasErrors()) {
                addErrorMessages(errors, bindingResult.getAllErrors(), i);
            } else {
                cartService.addPhone(cart, phoneDao.getByModel(models.get(i)).get().getId(),
                        parseQuantity(inputUnit));
                success.add( models.get(i) + "Successfully added");
            }
        }
        model.addAttribute("errors", errors);
        model.addAttribute("success", success);
        return "quickOrderEntry";
    }

    private Long parseQuantity(Model2QuantityInputUnit inputUnit) throws ParseException {
        return NumberFormat.getInstance(inputUnit.getLocale())
                .parse(inputUnit.getQuantity())
                .longValue();
    }

    private void addErrorMessages(Map<Integer, List<ErrorMessageForQuickOrder>> errorsMessages,
                                  List<ObjectError> errors, int itemNumber) {
        List<ErrorMessageForQuickOrder> messages = new ArrayList<>();
        for(ObjectError error: errors) {
            TypeOfErrorMessageForQuickOrder type;
            if (error.getCodes()[1].equals("model"))
                type = TypeOfErrorMessageForQuickOrder.MODEL;
            else if (error.getCodes()[1].equals("quantity"))
                type = TypeOfErrorMessageForQuickOrder.QUANTITY;
            else type = null;
            messages.add(new ErrorMessageForQuickOrder(error.getDefaultMessage(), type));
        }
        errorsMessages.put(itemNumber, messages);

    }

}
