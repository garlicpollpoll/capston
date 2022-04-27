package com.hello.capston.controller.upload;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.ItemDetail;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.service.DetailUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class DetailUploadController {

    private final DetailUploadService detailUploadService;
    private final ItemDetailRepository itemDetailRepository;

    @GetMapping("/detail_upload/{itemId}")
    public String detailUpload(@PathVariable("itemId") Long itemId, Model model) {
        model.addAttribute("itemId", itemId);

        return "detail_upload";
    }

    @PostMapping("/detail_upload/{itemId}")
    public String detailUploadPost(@PathVariable("itemId") Long itemId, HttpServletRequest request) {
        String[] sizes = request.getParameterValues("size");
        String[] stocks = request.getParameterValues("stock");

        detailUploadService.detailItemUpload(sizes, stocks, itemId);

        return "redirect:/";
    }
}
