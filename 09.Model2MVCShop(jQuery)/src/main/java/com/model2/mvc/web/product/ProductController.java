package com.model2.mvc.web.product;

import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Arrays;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/product/*") // 대문자 P 사용 시, JSP/리다이렉트도 모두 /Product 로 통일
public class ProductController {
	
	@Autowired
	private ProductService productService;
	private static final String PRODUCT_UPLOAD_DIR = "C:/onsesang/uploads/product";
	
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
	
//	// 1. 등록 화면 : GET /Product/addProduct
//	@RequestMapping(value="addProduct", method=RequestMethod.GET)
//	public String addProductView(HttpServletRequest req) throws Exception {
//		return "/product/addProductView.jsp";
//	}
//	
//	// 2. 등록 처리 : POST /Product/addProduct
//	@RequestMapping(value="addProduct", method=RequestMethod.POST)
//	public String addProduct(@ModelAttribute("product") Product product, Model model) throws Exception {
//	    System.out.println("[ProductController] addProduct() start");
//
//	    bindSessionFlags(model);
//
//	    productService.addProduct(product);
//		model.addAttribute("product", product);
//		
//		// 등록 후 목록으로 (클래스 매핑 포함해 /Product/listProduct 로 이동)
//		return "redirect:/product/listProduct";
//	}
	
	// 1. 등록 화면 : GET /Product/addProduct  << 그대로 둬~
	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProductView(HttpServletRequest req) throws Exception {
	    return "/product/addProductView.jsp";
	}

	// 2. 등록 처리 : POST /Product/addProduct  << 여기만 디버깅 로그 추가
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product,
	                         @RequestParam(value="fName", required=false) MultipartFile imageFile,
	                         HttpServletRequest request,
	                         Model model) throws Exception {
	    // ===================== 디버깅 시작 =====================
	    System.out.println("\n[ProductController] addProduct() start ===== 디버깅 시작 =====");
	    try {
	        // 0) 요청 메타 확인
	        System.out.println("[DBG] Request URI     : " + request.getRequestURI());
	        System.out.println("[DBG] Method          : " + request.getMethod());
	        System.out.println("[DBG] Content-Type    : " + request.getContentType());
	        System.out.println("[DBG] CharacterEncoding: " + request.getCharacterEncoding());

	        // 1) 헤더 확인
	        System.out.println("[DBG] --- Request Headers ---");
	        Enumeration<String> headerNames = request.getHeaderNames();
	        while (headerNames != null && headerNames.hasMoreElements()) {
	            String name = headerNames.nextElement();
	            System.out.println("[DBG]   " + name + " : " + request.getHeader(name));
	        }

	        // 2) 파라미터 맵 확인 (multipart 파싱 여부 판단)
	        System.out.println("[DBG] --- Parameter Map ---");
	        Map<String,String[]> paramMap = request.getParameterMap();
	        System.out.println("[DBG] keys   : " + paramMap.keySet());
	        for (Map.Entry<String,String[]> e : paramMap.entrySet()) {
	            System.out.println("[DBG]   " + e.getKey() + " = " + Arrays.toString(e.getValue()));
	        }

	        // 3) 바인딩 결과 확인(Product에 들어온 값)
	        System.out.println("[DBG] --- Bound Product ---");
	        System.out.println("[DBG] prodName       : " + product.getProdName());
	        System.out.println("[DBG] prodDetail     : " + product.getProdDetail());
	        System.out.println("[DBG] manufactureDay : " + product.getManufactureDay());
	        System.out.println("[DBG] price          : " + product.getPrice());
	        System.out.println("[DBG] imageFile(prop): " + product.getImageFile());

	        // 4) MultipartFile 상태 확인
	        if (imageFile == null) {
	            System.out.println("[DBG] MultipartFile  : null");
	        } else {
	            System.out.println("[DBG] MultipartFile  : isEmpty=" + imageFile.isEmpty()
	                    + ", originalName=" + imageFile.getOriginalFilename()
	                    + ", size=" + imageFile.getSize());
	        }
	    } catch (Exception e) {
	        System.out.println("[DBG][WARN] addProduct() 초기 디버깅 중 예외 : " + e);
	    }
	    // ===================== 디버깅 끝 =======================

	    bindSessionFlags(model); // 기존 유지

	    // 2-1) 이미지 업로드 (없으면 null 반환)
	    String savedName = saveUpload(imageFile, request);

	    // 2-2) 도메인에 파일명 세팅 (프로퍼티명에 맞춰 ↓ 둘 중 하나 사용)
	    if (savedName != null) {
	        // 만약 Product가 imageFile 필드를 가진다면
	        product.setImageFile(savedName);

	        // 만약 Product가 fileName 필드라면 이 줄로 교체
	        // product.setFileName(savedName);
	    }

	    // 2-3) 서비스 호출
	    try {
	        System.out.println("[DBG] 서비스 호출 직전 :: prodName=" + product.getProdName()
	                + ", price=" + product.getPrice()
	                + ", manufactureDay=" + product.getManufactureDay()
	                + ", imageFile=" + product.getImageFile());
	        productService.addProduct(product);
	        System.out.println("[DBG] 서비스 호출 완료(addProduct) OK");
	    } catch (Exception svcEx) {
	        // 서비스/DAO/SQL 단계 예외를 콘솔에서 즉시 확인
	        System.out.println("[ERR] productService.addProduct() 예외 : " + svcEx);
	        throw svcEx;
	    }
	    
	    if (product.getProTranCode() == null || product.getProTranCode().isEmpty()) {
	        product.setProTranCode("판매중");
	    }

	    model.addAttribute("product", product);

	    System.out.println("[ProductController] addProduct() saved image : " + savedName);
	    System.out.println("[ProductController] addProduct() end ===== 디버깅 종료 =====\n");
	    return "redirect:/product/listProduct";
	}

	// ========= 유틸 : 업로드 저장 =========
	
//	private String saveUpload(MultipartFile file, HttpServletRequest request) throws IOException {
//	    // 파일 파트가 비어 있으면 업로드 스킵 (정상 케이스)
//	    if (file == null) {
//	        System.out.println("[ProductController] saveUpload() : MultipartFile == null (폼 name 확인 필요)");
//	        return null;
//	    }
//	    if (file.isEmpty()) {
//	        System.out.println("[ProductController] saveUpload() : empty file (업로드 스킵)");
//	        return null;
//	    }
//
//	    // 원본 파일명/확장자 로깅
//	    String submitted = Paths.get(file.getOriginalFilename()).getFileName().toString();
//	    String ext = "";
//	    int dot = submitted.lastIndexOf('.');
//	    if (dot > -1) {
//	        ext = submitted.substring(dot);
//	    }
//	    String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
//
//	    // 실제 저장 경로 확인
//	    String uploadDir = request.getServletContext().getRealPath("/upload/product");
//	    System.out.println("[ProductController] saveUpload() : uploadDir=" + uploadDir);
//	    File dir = new File(uploadDir);
//	    if (!dir.exists()) {
//	        boolean made = dir.mkdirs();
//	        System.out.println("[ProductController] saveUpload() : mkdirs=" + made);
//	        if (!made) {
//	            throw new IOException("업로드 디렉터리 생성 실패 : " + uploadDir);
//	        }
//	    }
//
//	    // 저장 수행
//	    File dest = new File(dir, savedName);
//	    file.transferTo(dest);
//	    System.out.println("[ProductController] saveUpload() : 저장파일=" + dest.getAbsolutePath()
//	            + " (size=" + file.getSize() + " bytes)");
//	    return savedName;
//	}
	
	private String saveUpload(MultipartFile file, HttpServletRequest request) throws IOException {
	    if (file == null || file.isEmpty()) return null;

	    String submitted = java.nio.file.Paths.get(file.getOriginalFilename()).getFileName().toString();
	    String ext = "";
	    int dot = submitted.lastIndexOf('.');
	    if (dot > -1) ext = submitted.substring(dot).toLowerCase();

	    // 확장자 간단 화이트리스트(선택이지만 권장)
	    if (!ext.matches("\\.(png|jpg|jpeg|gif|webp)$")) {
	        throw new IOException("허용되지 않는 파일 형식: " + ext);
	    }

	    String savedName = java.util.UUID.randomUUID().toString().replace("-", "") + ext;

	    java.io.File dir = new java.io.File(PRODUCT_UPLOAD_DIR);
	    if (!dir.exists() && !dir.mkdirs()) {
	        throw new IOException("업로드 디렉터리 생성 실패: " + PRODUCT_UPLOAD_DIR);
	    }

	    file.transferTo(new java.io.File(dir, savedName));
	    return savedName;
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

		return "/product/updateProductView.jsp";
	}
	
	// 5. 수정 처리 : POST /Product/updateProduct
//	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
//	public String updateProduct(@ModelAttribute("product") Product product,
//	                            @RequestParam(value="fName", required=false) MultipartFile imageFile,
//	                            HttpServletRequest request,
//	                            Model model) throws Exception {
//	    System.out.println("[ProductController] updateProduct() start");
//
//	    bindSessionFlags(model);
//
//	    // 1) 새 파일 업로드가 있으면 저장
//	    String savedName = saveUpload(imageFile, request);
//
//	    // 2) 새 파일이 실제로 저장되었을 때만 도메인에 반영
//	    if (savedName != null) {
//	        product.setImageFile(savedName);
//	    }
//	    // 새 파일이 없으면 imageFile를 건드리지 않음(= null 유지) → 매퍼에서 동적 SET로 컬럼 제외
//
//	    // 3) 업데이트
//	    productService.updateProduct(product);
//
//	    // 4) 갱신 데이터 조회/바인딩
//	    Product updated = productService.getProduct(product.getProdNo());
//	    model.addAttribute("product", updated);
//
//	    return "/product/getProduct.jsp";
//	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product") Product product,
	                            @RequestParam(value="fName", required=false) MultipartFile imageFile,
	                            HttpServletRequest request,
	                            Model model) throws Exception {
	    bindSessionFlags(model);

	    // 1) 새 파일이 있으면 저장
	    String savedName = saveUpload(imageFile, request);
	    if (savedName != null) {
	        product.setImageFile(savedName); // 새 파일 올라온 경우에만 세팅
	    }

	    // 2) 업데이트
	    productService.updateProduct(product);

	    // 3) 갱신 조회
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
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
	    binder.registerCustomEditor(int.class,     new CustomNumberEditor(Integer.class, true));
	}

}
