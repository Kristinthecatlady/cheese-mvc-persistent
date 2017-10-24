package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model){
        Iterable<Menu> menus = menuDao.findAll();
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menus);
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addMenu(Model model){
        model.addAttribute(new Menu());
        model.addAttribute("title", "Create Menu");
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processMenu(@ModelAttribute @Valid Menu newMenu,
                              Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value= "view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int id){
        Menu menu = menuDao.findOne(id);
        model.addAttribute(menu);
        return "menu/view";
    }

    @RequestMapping(value="add-item/{id}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id){
        Menu menu = menuDao.findOne(id);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        AddMenuItemForm form = new AddMenuItemForm(menu, cheeses);
        model.addAttribute("form", form);
        model.addAttribute("title", "Add Item Too: " + menu.getName());
        return "menu/add-item";
    }
    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String processItem(@ModelAttribute @Valid AddMenuItemForm form,
                              Errors errors, Model model){
        Menu menu = menuDao.findOne(form.getMenuId());
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Item Too: " + menu.getName());
            return "menu/add-item";
        }
        Cheese cheese = cheeseDao.findOne(form.getCheeseId());
        menu.addItem(cheese);
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
}