package com.hello.capston.service;

import com.hello.capston.entity.Item;
import com.hello.capston.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class ClickDuplicationPreventService {

    private final ItemRepository itemRepository;

    public void viewCountUp(Item item, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + item.getId() + "]")) {
                item.addClick();
                oldCookie.setValue(oldCookie.getValue() + "_[" + item.getId() + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 10);
                response.addCookie(oldCookie);
                itemRepository.save(item);
            }
        }
        else {
            item.addClick();
            Cookie newCookie = new Cookie("postView", "[" + item.getId() + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 10);
            response.addCookie(newCookie);
        }
    }
}
