package com.hello.capston.controller.upload;

import com.hello.capston.dto.form.ItemNameDuplicateForm;
import com.hello.capston.dto.form.UniqueCodeDuplicateForm;
import com.hello.capston.entity.Item;
import com.hello.capston.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DuplicateController {

    private final ItemRepository itemRepository;

    @GetMapping("/item_name_duplicate")
    public String itemNameDuplicate(Model model) {
        ItemNameDuplicateForm form = new ItemNameDuplicateForm();
        model.addAttribute("duplicate", form);
        return "item_name_duplicate";
    }

    @GetMapping("/unique_code_duplicate")
    public String uniqueCodeDuplicate(Model model) {
        UniqueCodeDuplicateForm form = new UniqueCodeDuplicateForm();
        model.addAttribute("duplicate", form);
        return "unique_code_duplicate";
    }

    @PostMapping("/item_name_duplicate")
    public String itemNameDuplicatePost(@Validated @ModelAttribute("duplicate")ItemNameDuplicateForm form, BindingResult bindingResult, Model model) {
        List<Item> findItem = itemRepository.findByItemName(form.getItemName());

        if (bindingResult.hasErrors()) {
            return "item_name_duplicate";
        }

        if (findItem.size() == 0) {
            bindingResult.reject("PassItemNameDuplicate");
            model.addAttribute("result", form.getItemName());
            return "item_name_duplicate";
        }
        else {
            model.addAttribute("item", findItem);
        }
        return "item_name_duplicate";
    }

    @PostMapping("/unique_code_duplicate")
    public String uniqueCodeDuplicatePost(@Validated @ModelAttribute("duplicate")UniqueCodeDuplicateForm form, BindingResult bindingResult, Model model) {
        Item findItem = itemRepository.findByUniqueCode(form.getUniqueCode()).orElse(null);

        if (bindingResult.hasErrors()) {
            return "unique_code_duplicate";
        }

        if (findItem == null) {
            bindingResult.reject("PassUniqueCodeDuplicate");
            model.addAttribute("result", form.getUniqueCode());
            return "unique_code_duplicate";
        }
        else {
            model.addAttribute("item", findItem);
        }
        return "unique_code_duplicate";
    }
}
