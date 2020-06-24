package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lowagie.text.DocumentException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;

@Controller
public class AppController {

	@Autowired
	private ProductService service;

	@RequestMapping("/")
	public String viewHomePage(Model model, @Param("keyword") String keyword) {
		// String keyword = "iPhone";
		List<Product> listProducts = service.listAll(keyword);
		model.addAttribute("listProducts", listProducts); // next bc of thymeleaf we make the index.html
		model.addAttribute("keyword", keyword);

		return "index";
	}

	@RequestMapping("/new")
	public String showNewProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);

		return "new_product";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) {
		service.save(product);

		return "redirect:/";

	}

	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("edit_product");
		Product product = service.get(id);
		modelAndView.addObject("product", product);

		return modelAndView;
	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		service.delete(id);

		return "redirect:/";
	}

	//@GetMapping("/export")
	@RequestMapping("/export") 
	public void exportToPdf(HttpServletResponse response, 
							Model model, 
							@Param("keyword") String keyword) throws DocumentException, IOException {

		// System.out.println("keyword: " + keyword);
		
		response.setContentType("application/pdf");
		
		DateFormat dtformatter = new SimpleDateFormat("dd-mm-yyyy_HH:MM:SS"); 
		String currDateTime = dtformatter.format(new Date());  
		
		String headerKey = "Content-Disposition"; 
		String headerValue = "attachment; filename=products_" + currDateTime + ".pdf"; 
				
		response.setHeader(headerKey,  headerValue);
		
		//if(keyword != null) {
			List<Product> listProducts = service.listAll(keyword); 
			model.addAttribute("listProducts", listProducts); // next bc of thymeleaf we make the index.html
			model.addAttribute("keyword", keyword);
		//} else {
		//	List<Product> listProducts = service.listAll();  
		//}
			
		UserPDFExporter exporter = new UserPDFExporter(listProducts);
		exporter.export(response);
		
	}
	
}
