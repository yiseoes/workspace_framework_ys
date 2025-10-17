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
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	public ProductController() {
		System.out.println("[ProductController] default 생성자 호출");
	}
	
	// ─────────────────────────────────────────────────────────────
	// 세션 기반 플래그 바인딩 : isLogin / isAdmin 을 model에 주입
	//  - 메서드 시그니처 변경 없이, 현재 요청에서 세션을 안전하게 조회
	//  - userId == admin || role in (admin, ROLE_ADMIN, A) → 관리자
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

			// JSP에서 바로 사용 가능
			model.addAttribute("isLogin", isLogin);
			model.addAttribute("isAdmin", isAdmin);

		} catch (IllegalStateException ignore) {
			// 요청 컨텍스트 없을 때(비웹 컨텍스트 등) : 아무 것도 바인딩하지 않음
		}
	}
	
	// 1. 등록 화면
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {
		return "/product/addProductView.jsp";
	}
	
	// 2. 등록 처리
	@RequestMapping(value="/addProduct.do", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product, Model model) throws Exception {
	    System.out.println("[ProductController] addProduct() start");

	    // 세션 플래그 바인딩
	    bindSessionFlags(model);

	    productService.addProduct(product);
		
		// 등록된 product를 결과 페이지에서 사용
		model.addAttribute("product", product);
		
		// add 후 list로 이동
		return "redirect:/listProduct.do";
	}
	
	// 3. 상세 조회
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo, Model model) throws Exception {
		System.out.println("[ProductController] getProduct() start");
		
		// 세션 플래그 바인딩
		bindSessionFlags(model);

		Product product = productService.getProduct(prodNo);
		model.addAttribute("product", product);
		
		return "/product/getProduct.jsp";
	}
	
	// 4. 수정 화면
	@RequestMapping("/updateProductView.do")
	public String updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception {
		System.out.println("[ProductController] updateProductView() start");
		
		// 세션 플래그 바인딩
		bindSessionFlags(model);

		Product product = productService.getProduct(prodNo);
		model.addAttribute("product", product);
		
		return "/product/updateProductView.jsp";
	}
	
	// 5. 수정 처리
	@RequestMapping(value="/updateProduct.do", method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product") Product product, Model model) throws Exception {
		System.out.println("[ProductController] updateProduct() start");
		
		// 세션 플래그 바인딩
		bindSessionFlags(model);

		productService.updateProduct(product);
		Product updated = productService.getProduct(product.getProdNo());
		
		model.addAttribute("product", updated);
		return "/product/getProduct.jsp";
	}
	
	// 6. 목록 조회
	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, Model model) throws Exception {
		System.out.println("[ProductController] listProduct() start");
		
		// 세션 플래그 바인딩
		bindSessionFlags(model);

		// === 검색/페이징 기본값 보정 ===
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
		
		// 서비스 호출
		Map<String, Object> data = productService.getProductList(search);
		
		@SuppressWarnings("unchecked")
		List<Product> list = (List<Product>) data.get("list");
		int totalCount = (Integer) data.get("totalCount");
		
		// 모델 바인딩
		model.addAttribute("list", list);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("search", search);
		
		model.addAttribute("resultPage",
				new Page(search.getCurrentPage(), totalCount, 5, search.getPageSize()));
		
		return "/product/listProduct.jsp";
	}
}
