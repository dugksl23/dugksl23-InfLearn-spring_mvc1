package hello.Itemservice.web.basic;


import hello.Itemservice.domain.item.Item;
import hello.Itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
@Slf4j
public class BasicItemController {

    private final ItemRepository store;

    @GetMapping
    public String items(Model model) {

        List<Item> all = store.findAll();
        model.addAttribute("list", all);
        return "basic/items";
    }


    @GetMapping("/add")
    public String addItem() {
        return "basic/addForm";
    }


//    @PostMapping("/add")
//    public String addItemV1WithRedirectString(@ModelAttribute Item item, Model model) {
//        Item save = store.save(item);
//        //model.addAttribute("list", store.findAll());
//        log.info("id {}", save.getId());
//        return "redirect:/basic/items/" + save.getId();
//    }

    @PostMapping("/add")
    public String addItemV2WithRedirectAttribute(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item save = store.save(item);
        redirectAttributes.addAttribute("id", save.getId());
        redirectAttributes.addAttribute("result", true);
        log.info("1, {}", save.getId());
        return "redirect:/basic/items/{id}";
    }


    @GetMapping("/{id}")
    public String findByItem(@ModelAttribute Item item, @PathVariable Long id, Model model) {
        log.info("2, {}", item.getId());
        Item one = store.findOne(id);
        model.addAttribute("item", one);

        return "basic/item";
    }

    @GetMapping("/updateProc/{id}")
    public String updateProcItem(@PathVariable Long id, Model model) {
        Item one = store.findOne(id);
        model.addAttribute("item", one);
        return "basic/editForm";
    }

    @PostMapping("/updateItem/{itemId}")
    public String updateItem(Item item, @PathVariable Long itemId) { //ex) @ModelAttribute("item")
        log.info("update come : {}", itemId);
        // @PathVariable은 name 값을 정해주거나 변수명을 일치시켜야 한다.

        Item update = store.update(itemId, item);
//        model.addAttribute("item", update);
//        @ModelAttribute("name")의 name 값을 지정하면, 해당 어노테이션 사용한 객체가 model에 자동 등록되기에 생략 가능.
//        name 값도 생략 가능하며, 지정된 class type을 첫글자만 소문자만 바꾼 후에, key값으로 저장한다.-> ex) @ModelAttribute
//        @ModelAttribute 도 생략가능
        return "redirect:/basic/items/{itemId}";
    }


    @PostConstruct
    public void init() {
        Item item = new Item("1", 1000, 10);
        Item item1 = new Item("2", 1000, 10);
        store.saveAll(Arrays.asList(item, item1));
    }
}
