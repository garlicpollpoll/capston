package com.hello.capston.controller.upload;

import com.hello.capston.S3.S3Uploader;
import com.hello.capston.dto.form.UploadForm;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.ItemDetail;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.service.ItemService;
import com.hello.capston.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController {

    private final S3Uploader s3Uploader;
    private final ItemService itemService;

    private final MemberService memberService;

    @PostMapping("/upload_item")
    public String uploadItem(@Validated @ModelAttribute("upload")UploadForm form, BindingResult bindingResult, HttpSession session,
                             RedirectAttributes redirectAttributes) throws IOException {
        String loginId = (String) session.getAttribute("loginId");
        Member findMember = memberService.findMember(loginId);
        Item findItemByUniqueCode = itemService.findByUniqueCode(form.getUniqueCode());

        if (findItemByUniqueCode != null) {
            bindingResult.reject("UniqueCodeDuplicate");
            return "upload";
        }

        if (form.getImage().isEmpty()) {
            bindingResult.reject("ImageEmpty");
            return "upload";
        }

        if (bindingResult.hasErrors()) {
            return "upload";
        }

        String imageUrl = s3Uploader.upload(form.getImage(), "static");

        Item item = itemService.saveItem(form, imageUrl, findMember);

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/detail_upload/{itemId}";
    }
}
