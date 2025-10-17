package com.model2.mvc.web.product;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

/**
 * ProductRestController.java (네 컨트롤러는 그대로, 이 파일만 추가)
 * - JSON API : /product/json/...
 * - 파일 API : /product/file/...
 * - 메서드명/흐름 : 네 기존 서비스 시그니처에 맞춤
 * - 디버깅 로그 : 전부 한글
 */
@RestController
@RequestMapping("/product/*")
public class ProductRestController {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    // 네 컨트롤러와 같은 상수명/경로
    private static final String PRODUCT_UPLOAD_DIR = "C:/onsesang/uploads/product";

    public ProductRestController() {
        System.out.println("==> [REST] ProductRestController 생성됨");
    }

    // ---------------------------------------------------------------------
    // 1) JSON API
    // ---------------------------------------------------------------------

    /** 상품 등록(JSON) */
    @PostMapping(
        value = "json/addProduct",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean addProduct(@RequestBody Product product) throws Exception {
        System.out.println("[REST][등록] /product/json/addProduct : 요청 수신");
        System.out.println("  - 상품명: " + product.getProdName());
        System.out.println("  - 가격: " + product.getPrice());
        System.out.println("  - 제조일자: " + product.getManufactureDay());
        System.out.println("  - 이미지파일명(imageFile): " + product.getImageFile());

        productService.addProduct(product);

        System.out.println("[REST][등록] 처리 완료");
        return true;
    }

    /** 상품 상세(JSON) */
    @GetMapping(
        value = "json/getProduct/{prodNo}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product getProduct(@PathVariable int prodNo) throws Exception {
        System.out.println("[REST][상세] /product/json/getProduct : 요청 수신, prodNo=" + prodNo);
        Product product = productService.getProduct(prodNo);
        System.out.println("[REST][상세] 조회 결과: " + (product != null ? "데이터 있음" : "데이터 없음"));
        return product;
    }

    /** 상품 리스트(JSON) */
    @PostMapping(
        value = "json/getProductList",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> getProductList(@RequestBody Search search) throws Exception {
        System.out.println("[REST][목록] /product/json/getProductList : 요청 수신");
        System.out.println("  - 현재페이지: " + search.getCurrentPage());
        System.out.println("  - 페이지크기: " + search.getPageSize());
        System.out.println("  - 검색조건: " + search.getSearchCondition());
        System.out.println("  - 검색어: " + search.getSearchKeyword());

        Map<String, Object> result = productService.getProductList(search);
        System.out.println("[REST][목록] 처리 완료, totalCount=" + (result != null ? result.get("totalCount") : null));
        return result;
    }

    /** 상품 수정(JSON) */
    @PostMapping(
        value = "json/updateProduct",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean updateProduct(@RequestBody Product product) throws Exception {
        System.out.println("[REST][수정] /product/json/updateProduct : 요청 수신");
        System.out.println("  - 상품번호: " + product.getProdNo());
        System.out.println("  - 변경상품명: " + product.getProdName());
        System.out.println("  - 변경가격: " + product.getPrice());
        System.out.println("  - 변경이미지파일명: " + product.getImageFile());

        productService.updateProduct(product);

        System.out.println("[REST][수정] 처리 완료");
        return true;
    }

    // ===== 거래상태 코드 =====
    // 네 프로젝트에서 확실히 존재하는 메서드에 맞춰 두 개 제공
    // updateProTranCodeToSold(int), updateProTranCodeToSale(int)

    /** 거래상태 → 판매완료 */
    @PostMapping(value = "json/updateTranCodeToSold", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateTranCodeToSold(@RequestParam("prodNo") int prodNo) throws Exception {
        System.out.println("[REST][거래상태] /product/json/updateTranCodeToSold : 요청 수신, prodNo=" + prodNo);
        int cnt = productService.updateProTranCodeToSold(prodNo);
        System.out.println("  - 반영행수: " + cnt);
        return cnt > 0;
    }

    /** 거래상태 → 판매중 */
    @PostMapping(value = "json/updateTranCodeToSale", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateTranCodeToSale(@RequestParam("prodNo") int prodNo) throws Exception {
        System.out.println("[REST][거래상태] /product/json/updateTranCodeToSale : 요청 수신, prodNo=" + prodNo);
        int cnt = productService.updateProTranCodeToSale(prodNo);
        System.out.println("  - 반영행수: " + cnt);
        return cnt > 0;
    }

    // (선택) 일반형: 프로젝트에 productService.updateTranCode(Product)가 “실제로” 있으면 주석 해제
    /*
    @PostMapping(
        value = "json/updateTranCode",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean updateTranCode(@RequestBody Product product) throws Exception {
        System.out.println("[REST][거래상태] /product/json/updateTranCode : 요청 수신");
        System.out.println("  - 상품번호: " + product.getProdNo());
        System.out.println("  - 변경코드: " + product.getProTranCode());

        productService.updateTranCode(product);

        System.out.println("[REST][거래상태] 처리 완료");
        return true;
    }
    */

    // ---------------------------------------------------------------------
    // 2) 파일 업로드 API (헬퍼명도 네 컨벤션: saveUpload / deleteUpload)
    // ---------------------------------------------------------------------

    /** 단일 파일 업로드 */
    @PostMapping(
        value = "file/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FileUploadResponse upload(@RequestPart("file") MultipartFile file) throws Exception {
        System.out.println("[REST][업로드] /product/file/upload : 요청 수신 (단일 파일)");
        String savedName = saveUpload(file);
        System.out.println("  - 저장파일명: " + savedName + "  (이 값을 product.imageFile 로 사용)");
        return new FileUploadResponse(true, savedName, null);
    }

    /** 다중 파일 업로드(옵션) */
    @PostMapping(
        value = "file/uploads",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FileUploadResponse uploads(@RequestPart("files") List<MultipartFile> files) throws Exception {
        System.out.println("[REST][업로드] /product/file/uploads : 요청 수신 (다중 파일)");
        List<String> names = new ArrayList<>();
        if (files != null) {
            for (MultipartFile f : files) {
                names.add(saveUpload(f));
            }
        }
        System.out.println("  - 저장개수: " + names.size());
        return new FileUploadResponse(true, null, names);
    }

    /** 파일 삭제(옵션) */
    @DeleteMapping(value = "file/delete/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FileUploadResponse delete(@PathVariable String filename) throws Exception {
        System.out.println("[REST][삭제] /product/file/delete : 요청 수신, filename=" + filename);
        boolean ok = deleteUpload(filename);
        System.out.println("  - 결과: " + (ok ? "성공" : "실패(파일 없음)"));
        return new FileUploadResponse(ok, ok ? filename : null, null);
    }

    // ---------------------------------------------------------------------
    // 3) 내부 헬퍼 (자바8 호환: isBlank() 사용 안함)
    // ---------------------------------------------------------------------

    /** 실제 파일 저장 */
    private String saveUpload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        File dir = new File(PRODUCT_UPLOAD_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("업로드 디렉터리 생성 실패: " + PRODUCT_UPLOAD_DIR);
        }

        String original = file.getOriginalFilename();
        String saved = buildSafeName(original);

        file.transferTo(new File(dir, saved));
        return saved;
    }

    /** 파일 삭제 */
    private boolean deleteUpload(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }
        File target = new File(PRODUCT_UPLOAD_DIR, filename);
        if (target.exists()) {
            return target.delete();
        }
        return false;
    }

    /** 안전한 파일명 생성: yyyyMMdd_HHmmssSSS_originalName (공백→_) */
    private String buildSafeName(String original) {
        String base = (original == null || original.trim().isEmpty())
                ? "file"
                : original.replaceAll("\\s+", "_");
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
        return ts + "_" + base;
    }

    // ---------------------------------------------------------------------
    // 4) 업로드 응답 DTO (내부 static)
    // ---------------------------------------------------------------------
    public static class FileUploadResponse {
        private boolean success;
        private String filename;
        private List<String> filenames;

        public FileUploadResponse() {}

        public FileUploadResponse(boolean success, String filename, List<String> filenames) {
            this.success = success;
            this.filename = filename;
            this.filenames = filenames;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }

        public List<String> getFilenames() { return filenames; }
        public void setFilenames(List<String> filenames) { this.filenames = filenames; }
    }
}
