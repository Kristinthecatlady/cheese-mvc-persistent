package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    CheeseDao cheeseDao;

    @Autowired
    CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors,
                                       @RequestParam int categoryId,
                                       Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);

        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }
    @RequestMapping(value = "category/{id}", method = RequestMethod.GET)
    public String category(@PathVariable int id, Model model){
        Category cat = categoryDao.findOne(id);
        List<Cheese> cheeses = cat.getCheeses();
        model.addAttribute("title", "All " + cat.getName() + " Cheeses");
        model.addAttribute("cheeses", cheeses);
        return "cheese/index";
    }

    /**@RequestMapping(value="edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId){
        Cheese cheese = cheeseDao.findOne(cheeseId);
        model.addAttribute("cheese", cheese);
        model.addAttribute("title", "Edit Cheese " + cheese.getName() + " (ID = " + cheese.getId() + ")");
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(@ModelAttribute @Valid Cheese editedCheese,
                                  Errors errors, Model model,
                                  @RequestParam int id,
                                  @RequestParam int categoryId){
        Cheese cheese = cheeseDao.findOne(id);
        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Cheese " + cheese.getName() + " (ID = " + id + ")");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }
        Category cat = categoryDao.findOne(categoryId);
        cheese.setCategory(cat);
        cheese.setName(editedCheese.getName());
        cheese.setDescription(editedCheese.getDescription());
        cheeseDao.save(cheese);

        return "redirect:";
    }*/

}
