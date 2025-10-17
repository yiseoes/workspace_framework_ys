package com.model2.mvc.web.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@RestController
@RequestMapping("/product") // ★ '/product/*'에서 '/product'로 교체
public class ProductRestController {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    private static final String PRODUCT_UPLOAD_DIR = "images/uploadFiles";

    public ProductRestController() {
        System.out.println("==> [REST] ProductRestController 생성됨");
    }

    // ===================== JSON API =====================

    @PostMapping(
        value = "/json/addProduct",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean addProduct(@RequestBody Product product) throws Exception {
        productService.addProduct(product);
        return true;
    }

    @GetMapping(
        value = "/json/getProduct/{prodNo}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product getProduct(@PathVariable int prodNo) throws Exception {
        return productService.getProduct(prodNo);
    }

    @PostMapping(
        value = "/json/getProductList",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> getProductList(@RequestBody Search search) throws Exception {
        return productService.getProductList(search);
    }

    @PostMapping(value = "/json/updateTranCodeToSold", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateTranCodeToSold(@RequestParam("prodNo") int prodNo) throws Exception {
        return productService.updateProTranCodeToSold(prodNo) > 0;
    }

    @PostMapping(value = "/json/updateTranCodeToSale", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateTranCodeToSale(@RequestParam("prodNo") int prodNo) throws Exception {
        return productService.updateProTranCodeToSale(prodNo) > 0;
    }

    // ===================== 파일 업로드 API(생략 없이 유지) =====================
    @PostMapping(
        value = "/file/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FileUploadResponse upload(@RequestPart("file") MultipartFile file) throws Exception {
        String savedName = saveUpload(file);
        return new FileUploadResponse(true, savedName, null);
    }

    @PostMapping(
        value = "/file/uploads",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FileUploadResponse uploads(@RequestPart("files") List<MultipartFile> files) throws Exception {
        List<String> names = new ArrayList<>();
        if (files != null) {
            for (MultipartFile f : files) {
                names.add(saveUpload(f));
            }
        }
        return new FileUploadResponse(true, null, names);
    }

    @DeleteMapping(value = "/file/delete/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FileUploadResponse delete(@PathVariable String filename) throws Exception {
        boolean ok = deleteUpload(filename);
        return new FileUploadResponse(ok, ok ? filename : null, null);
    }

    // ===================== 오토컴플릿 =====================
    @GetMapping(value="/json/autocomplete", produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> autocomplete(
        @RequestParam(defaultValue="0") String searchCondition, // 0=상품명, 1=상세, 2=상품번호
        @RequestParam(defaultValue="")  String searchKeyword
    ) throws Exception {

        Map<String, Object> out = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();
        out.put("items", items);

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            return out;
        }

        Search search = new Search();
        if ("0".equals(searchCondition)) {
            search.setSearchCondition("0");       // Mapper: 이름 LIKE
            search.setSearchKeyword(searchKeyword);
        } else {
            search.setSearchCondition(null);      // 널널 조회 후 자바 후처리
            search.setSearchKeyword("");
        }
        search.setCurrentPage(1);
        search.setPageSize(50);

        Map<String, Object> svc = productService.getProductList(search);
        @SuppressWarnings("unchecked")
        List<Product> list = (List<Product>) svc.get("list");
        if (list == null) list = Collections.emptyList();

        String kw = searchKeyword.trim();
        List<Product> filtered = new ArrayList<>();

        if ("0".equals(searchCondition)) {
            for (Product p : list) { filtered.add(p); if (filtered.size() >= 10) break; }
        } else if ("1".equals(searchCondition)) {
            for (Product p : list) {
                String d = (p.getProdDetail() == null ? "" : p.getProdDetail());
                if (d.toLowerCase().contains(kw.toLowerCase())) {
                    filtered.add(p); if (filtered.size() >= 10) break;
                }
            }
        } else if ("2".equals(searchCondition)) {
            for (Product p : list) {
                String noStr = String.valueOf(p.getProdNo());
                if (noStr.contains(kw)) {
                    filtered.add(p); if (filtered.size() >= 10) break;
                }
            }
        } else {
            for (Product p : list) { filtered.add(p); if (filtered.size() >= 10) break; }
        }

        for (Product p : filtered) {
            Map<String, Object> item = new HashMap<>();
            if ("0".equals(searchCondition)) {
                item.put("label", p.getProdName());
                item.put("value", p.getProdName());
            } else if ("1".equals(searchCondition)) {
                String detail = (p.getProdDetail() == null ? "" : p.getProdDetail());
                String label = (detail.length() > 60 ? detail.substring(0, 60) + "…" : detail);
                item.put("label", label + " · (" + p.getProdName() + ")");
                item.put("value", detail);
            } else if ("2".equals(searchCondition)) {
                String noStr = String.valueOf(p.getProdNo());
                item.put("label", "#" + noStr + " · " + p.getProdName());
                item.put("value", noStr);
            } else {
                item.put("label", p.getProdName());
                item.put("value", p.getProdName());
            }
            item.put("prodNo", p.getProdNo());
            item.put("prodName", p.getProdName());
            items.add(item);
        }

        return out;
    }

    // ===================== 내부 헬퍼/DTO (네 코드 유지) =====================
    private String saveUpload(org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        if (file == null || file.isEmpty()) {
            throw new java.lang.IllegalArgumentException("업로드할 파일이 없습니다.");
        }
        org.springframework.web.context.request.ServletRequestAttributes attrs =
            (org.springframework.web.context.request.ServletRequestAttributes)
            org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes();
        javax.servlet.http.HttpServletRequest req = attrs.getRequest();

        String original = file.getOriginalFilename();
        String saved = buildSafeName(original);

        String uploadDirPath = req.getServletContext().getRealPath("/" + PRODUCT_UPLOAD_DIR);
        java.io.File runtimeDir = new java.io.File(uploadDirPath);
        if (!runtimeDir.exists() && !runtimeDir.mkdirs()) {
            throw new java.io.IOException("업로드 디렉터리 생성 실패: " + uploadDirPath);
        }
        java.io.File dest = new java.io.File(runtimeDir, saved);
        file.transferTo(dest);

        try {
            String mirrorPath = "C:\\workspace_framework\\10.Model2MVCShop(Ajax)\\src\\main\\webapp\\images\\uploadFiles";
            java.io.File mirrorDir = new java.io.File(mirrorPath);
            if (!mirrorDir.exists()) { mirrorDir.mkdirs(); }
            java.io.File mirrorFile = new java.io.File(mirrorDir, saved);
            java.nio.file.Files.copy(dest.toPath(), mirrorFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ignore) {}

        return saved;
    }

    private boolean deleteUpload(String filename) throws java.io.IOException {
        if (filename == null || filename.trim().isEmpty()) return false;

        org.springframework.web.context.request.ServletRequestAttributes attrs =
            (org.springframework.web.context.request.ServletRequestAttributes)
            org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes();
        javax.servlet.http.HttpServletRequest req = attrs.getRequest();

        boolean removed = false;

        String runtimePath = req.getServletContext().getRealPath("/" + PRODUCT_UPLOAD_DIR + "/" + filename);
        java.io.File runtimeFile = new java.io.File(runtimePath);
        if (runtimeFile.exists() && runtimeFile.delete()) removed = true;

        try {
            String mirrorPath = "C:\\workspace_framework\\10.Model2MVCShop(Ajax)\\src\\main\\webapp\\images\\uploadFiles";
            java.io.File srcFile = new java.io.File(mirrorPath, filename);
            if (srcFile.exists() && srcFile.delete()) removed = true;
        } catch (Exception ignore) {}

        return removed;
    }

    private String buildSafeName(String original) {
        String base = (original == null || original.trim().isEmpty())
                ? "file"
                : original.replaceAll("\\s+", "_");
        String ts = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
        return ts + "_" + base;
    }

    public static class FileUploadResponse {
        private boolean success;
        private String filename;
        private List<String> filenames;

        public FileUploadResponse() {}
        public FileUploadResponse(boolean success, String filename, List<String> filenames) {
            this.success = success; this.filename = filename; this.filenames = filenames;
        }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }
        public List<String> getFilenames() { return filenames; }
        public void setFilenames(List<String> filenames) { this.filenames = filenames; }
    }
}
