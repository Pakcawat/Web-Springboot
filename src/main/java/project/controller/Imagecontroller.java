package project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import project.model.Imageinfo;
import project.service.FilesStorageService;
import project.service.Imageservice;
import project.service.impl.CustomUserDetails;


@Controller
public class Imagecontroller {
	@Autowired
	private Imageservice imageservice;
	@Autowired
	private FilesStorageService storageService;

	@RequestMapping("/imageList")
	public String viewHomePage(Model model, @Param("keyword") String keyword,@AuthenticationPrincipal CustomUserDetails customUserDetails ,@AuthenticationPrincipal UserDetails userDetails) {

		List<Imageinfo> listProducts = imageservice.listAll(keyword , userDetails.getUsername());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_USER"));

		System.out.println(customUserDetails.getId());
		System.out.println(hasUserRole);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("keyword", keyword);

		return "Image_List";
	}

	@RequestMapping("/new")
	public String showNewProductForm(Model model) {
		Imageinfo product = new Imageinfo();
		model.addAttribute("product", product);

		return "Image_New";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Imageinfo product, Model model,
			@RequestParam("file") MultipartFile file ,@AuthenticationPrincipal UserDetails userDetails) {

		System.out.println(userDetails.getUsername());

		imageservice.save(product , userDetails.getUsername() ,file);

		return "redirect:/imageList";
	}

    @PostMapping("/save_edit/{id}")
    public String saveEdit(@PathVariable("id") long productId, @ModelAttribute("product") Imageinfo product,  Model model){


        imageservice.save_edit(product, productId);

        return "redirect:/imageList";
    }


	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("Image_Edit");

		Imageinfo product = imageservice.get(id);
		mav.addObject("product", product);

		return mav;
	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		imageservice.delete(id);

		return "redirect:/imageList";
	}

	@PostMapping("/imageList/{image_name}")
	public String getImage(@PathVariable(name = "image_name") String image_name , @ModelAttribute("Url_image") String Url_image)
	{
		Url_image = MvcUriComponentsBuilder
          .fromMethodName(FileController.class, "getFile", image_name.toString()).build().toString();

		return "redirect:/imageList";
	}
}
