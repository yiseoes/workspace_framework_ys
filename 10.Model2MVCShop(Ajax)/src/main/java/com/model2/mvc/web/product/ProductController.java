package com.model2.mvc.web.product;

import java.util.List;
import java.util.Map;

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
@RequestMapping("/product/*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 내부 저장소 상대 경로(웹컨텍스트 기준)
    private static final String PRODUCT_UPLOAD_DIR = "images/uploadFiles";

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

    // 1. 등록 화면 : GET /product/addProduct
    @RequestMapping(value="addProduct", method=RequestMethod.GET)
    public String addProductView(HttpServletRequest req) throws Exception {
        return "/product/addProductView.jsp";
    }

    // 2. 등록 처리 : POST /product/addProduct
    @RequestMapping(value="addProduct", method=RequestMethod.POST)
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam(value="fName", required=false) MultipartFile imageFile,
                             HttpServletRequest request,
                             Model model) throws Exception {
        // ===================== 디버깅 시작 =====================
        System.out.println("\n★☆★☆★☆★☆★☆[ProductController] addProduct() start");
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

            // 2) 파라미터 맵 확인
            System.out.println("[DBG] --- Parameter Map ---");
            Map<String,String[]> paramMap = request.getParameterMap();
            System.out.println("[DBG] keys   : " + paramMap.keySet());
            for (Map.Entry<String,String[]> e : paramMap.entrySet()) {
                System.out.println("[DBG]   " + e.getKey() + " = " + java.util.Arrays.toString(e.getValue()));
            }

            // 3) 바인딩 결과 확인
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

        bindSessionFlags(model);

        // 2-1) 이미지 업로드 (없으면 null)
        String savedName = saveUpload(imageFile, request);

        // 2-2) 도메인에 파일명 세팅
        if (savedName != null) {
            product.setImageFile(savedName);
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
    // 내부 저장소(배포폴더) 저장 + 소스폴더 미러링(하드코딩)
    private String saveUpload(MultipartFile file, HttpServletRequest request) throws java.io.IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 1) 유효성/확장자
        String submitted = java.nio.file.Paths.get(file.getOriginalFilename()).getFileName().toString();
        String ext = "";
        int dot = submitted.lastIndexOf('.');
        if (dot > -1) {
            ext = submitted.substring(dot).toLowerCase();
        }
        if (!ext.matches("\\.(png|jpg|jpeg|gif|webp|jfif)$")) {
            throw new java.io.IOException("허용되지 않는 파일 형식: " + ext);
        }

        // 2) 저장 파일명
        String savedName = java.util.UUID.randomUUID().toString().replace("-", "") + ext;

        // 3) 런타임 배포폴더 저장 (/images/uploadFiles)
        String uploadDirPath = request.getServletContext().getRealPath("/" + PRODUCT_UPLOAD_DIR);
        java.io.File runtimeDir = new java.io.File(uploadDirPath);
        if (!runtimeDir.exists() && !runtimeDir.mkdirs()) {
            throw new java.io.IOException("업로드 디렉터리 생성 실패: " + uploadDirPath);
        }
        java.io.File dest = new java.io.File(runtimeDir, savedName);
        file.transferTo(dest);
        System.out.println("[UPLOAD PATH CTRL] " + dest.getAbsolutePath());

        // 4) 소스폴더 미러링 (하드코딩 경로)
        try {
            String mirrorPath = "C:\\workspace_framework\\10.Model2MVCShop(Ajax)\\src\\main\\webapp\\images\\uploadFiles";
            java.io.File mirrorDir = new java.io.File(mirrorPath);
            System.out.println("[MIRROR TARGET CTRL] " + mirrorDir.getAbsolutePath());

            if (!mirrorDir.exists()) {
                boolean mk = mirrorDir.mkdirs();
                System.out.println("[MIRROR MKDIR CTRL] " + mirrorDir.getAbsolutePath() + " : " + mk);
            }

            java.io.File mirrorFile = new java.io.File(mirrorDir, savedName);
            java.nio.file.Files.copy(
                dest.toPath(),
                mirrorFile.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
            System.out.println("[MIRROR COPY CTRL] " + mirrorFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("[MIRROR ERR CTRL] 소스 미러링 중 예외: " + e);
        }

        return savedName;
    }

    // 3. 상세 조회 : GET /product/getProduct?prodNo=...
    @RequestMapping(value="getProduct", method=RequestMethod.GET)
    public String getProduct(@RequestParam("prodNo") int prodNo, Model model) throws Exception {
        System.out.println("★☆★☆★☆★☆★☆[ProductController] getProduct() start");

        bindSessionFlags(model);

        Product product = productService.getProduct(prodNo);
        model.addAttribute("product", product);

        return "/product/getProduct.jsp";
    }

    // 4. 수정 화면 : GET /product/updateProduct?prodNo=...
    @RequestMapping(value="updateProduct", method=RequestMethod.GET)
    public String updateProductView(@RequestParam("prodNo") int prodNo, Model model) throws Exception {
        System.out.println("★☆★☆★☆★☆★☆[ProductController] updateProduc() start");

        bindSessionFlags(model);

        Product product = productService.getProduct(prodNo);
        
        System.out.println("[★★★★★★★★★★★★디버깅] 서비스가 찾아온 상품: " + product);
        
        model.addAttribute("product", product);

        return "/product/updateProduct.jsp";
    }

    // 5. 수정 처리 : POST /product/updateProduct
    @RequestMapping(value="updateProduct", method=RequestMethod.POST)
    public String updateProduct(@ModelAttribute("product") Product product,
                                @ModelAttribute("search") Search search, // 👈 search 객체도 받도록 추가!
                                @RequestParam(value="fName", required=false) MultipartFile imageFile,
                                HttpServletRequest request) throws Exception { // 👈 Model은 이제 필요 없어!

        // 1) 새 파일이 있으면 저장 (기존 코드)
        String savedName = saveUpload(imageFile, request);
        if (savedName != null) {
            product.setImageFile(savedName);
        }

        // 2) 업데이트 (기존 코드)
        productService.updateProduct(product);

        // 3) 상세 페이지로 리다이렉트! (가장 중요한 변경점!)
        String queryString = String.format(
            "?prodNo=%d&currentPage=%d&searchCondition=%s&searchKeyword=%s&view=%s",
            product.getProdNo(),
            search.getCurrentPage(),
            search.getSearchCondition(),
            java.net.URLEncoder.encode(search.getSearchKeyword(), "UTF-8"), // 키워드는 URL 인코딩!
            request.getParameter("view") // view는 search 객체에 없으니 request에서 직접 받기
        );

        return "redirect:/product/getProduct" + queryString;
    }

    // 6. 목록 조회 : GET/POST /product/listProduct
    @RequestMapping(value="listProduct", method={RequestMethod.GET, RequestMethod.POST})
    public String listProduct(@ModelAttribute("search") Search search, Model model) throws Exception {
        System.out.println("★☆★☆★☆★☆★☆[ProductController] listProduct() start");

        bindSessionFlags(model);

        // 기본값 보정
        if(search.getCurrentPage() == 0) {
            search.setCurrentPage(1);
        }
        if(search.getPageSize() == 0) {
            search.setPageSize(3);
        }
        if(search.getSearchCondition() == null || search.getSearchCondition().trim().length() == 0) {
            search.setSearchCondition("0");   // 0=상품명
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
