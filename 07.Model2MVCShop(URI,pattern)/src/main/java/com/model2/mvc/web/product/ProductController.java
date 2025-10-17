package com.model2.mvc.web.product;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// ===== [세션 플래그 바인딩용] =====
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product") // 대문자 P 사용 시, JSP/리다이렉트도 모두 /Product 로 통일
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	public ProductController() {
		System.out.println("[ProductController] default 생성자 호출");
	}
	
	// ─────────────────────────────────────────────────────────────
	// 세션 기반 플래그 바인딩 : isLogin / isAdmin 을 model에 주입
	// ─────────────────────────────────────────────────────────────
	private void bindSessionFlags(Model model) {
		try {
			ServletRequestAttributes attrs =
				(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attrs.getRequest();
			HttpSession session = request.getSession(false);

			boolean isLogin = false;
			boolean isAdmin = false;

			if (session != null) {
				Object obj = session.getAttribute("user");
				if (obj == null) {
					obj = session.getAttribute("userVO"); // 혼재 프로젝트 대비
				}

				if (obj != null) {
					isLogin = true;

					String uid = "";
					String role = "";

					if (obj instanceof User) {
						User u = (User) obj;
						uid = (u.getUserId() == null ? "" : u.getUserId().trim());
						role = (u.getRole()   == null ? "" : u.getRole().trim());
					} else {
						uid = String.valueOf(obj).trim();
					}

					String uidLc  = uid.toLowerCase();
					String roleLc = role.toLowerCase();

					isAdmin =
							"admin".equals(uidLc)
						||  "admin".equals(roleLc)
						||  "role_admin".equals(roleLc)
						||  "a".equals(roleLc);
				}
			}

			model.addAttribute("isLogin", isLogin);
			model.addAttribute("isAdmin", isAdmin);

		} catch (IllegalStateException ignore) {
			// 비웹 컨텍스트 등: 무시
		}
	}
	
	// 1. 등록 화면 : GET /Product/addProduct
	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProductView(HttpServletRequest req) throws Exception {
		return "/product/addProductView.jsp";
	}
	
	// 2. 등록 처리 : POST /Product/addProduct
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product, Model model) throws Exception {
	    System.out.println("[ProductController] addProduct() start");

	    bindSessionFlags(model);

	    productService.addProduct(product);
		model.addAttribute("product", product);
		
		// 등록 후 목록으로 (클래스 매핑 포함해 /Product/listProduct 로 이동)
		return "redirect:/product/listProduct";
	}
	
	// 3. 상세 조회 : GET /Product/getProduct?prodNo=...
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct(@RequestParam("prodNo") int prodNo, Model model) throws Exception {
		System.out.println("[ProductController] getProduct() start");
		
		bindSessionFlags(model);

		Product product = productService.getProduct(prodNo);
		model.addAttribute("product", product);
		
		return "/product/getProduct.jsp";
	}
	
	// 4. 수정 화면 : GET /Product/updateProduct?prodNo=...
	//    (기존 updateProductView 메서드명/시그니처 유지)
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception {
		System.out.println("[ProductController] updateProductView() start");
		
		bindSessionFlags(model);

		Product product = productService.getProduct(prodNo);
		model.addAttribute("product", product);
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@================제조일자==========="+product.getManufactureDay());
		
		return "/product/updateProductView.jsp";
	}
	
	// 5. 수정 처리 : POST /Product/updateProduct
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product") Product product, Model model) throws Exception {
		System.out.println("[ProductController] updateProduct() start");
		
		bindSessionFlags(model);

		productService.updateProduct(product);
		Product updated = productService.getProduct(product.getProdNo());
		
		model.addAttribute("product", updated);
		return "/product/getProduct.jsp";
	}
	
	// 6. 목록 조회 : GET/POST /Product/listProduct
	@RequestMapping(value="listProduct", method={RequestMethod.GET, RequestMethod.POST})
	public String listProduct(@ModelAttribute("search") Search search, Model model) throws Exception {
		System.out.println("[ProductController] listProduct() start");
		
		bindSessionFlags(model);

		// 기본값 보정
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		if(search.getPageSize() == 0) {
			search.setPageSize(3); 
		}
		if(search.getSearchCondition() == null || search.getSearchCondition().trim().length() == 0) {
			search.setSearchCondition("0");   // 0=상품명, 1=가격(정확히)
		}
		if(search.getSearchKeyword() == null) {
			search.setSearchKeyword("");
		}
		
		Map<String, Object> data = productService.getProductList(search);
		
		@SuppressWarnings("unchecked")
		List<Product> list = (List<Product>) data.get("list");
		int totalCount = (Integer) data.get("totalCount");
		
		model.addAttribute("list", list);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("search", search);
		model.addAttribute("resultPage",
				new Page(search.getCurrentPage(), totalCount, 5, search.getPageSize()));
		
		return "/product/listProduct.jsp";
	}
}
