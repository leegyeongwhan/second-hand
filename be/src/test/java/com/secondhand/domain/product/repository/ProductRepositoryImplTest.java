//package com.secondhand.domain.product.repository;
//
//import com.secondhand.domain.product.Product;
//import com.secondhand.web.dto.filtercondition.ProductSearchCondition;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("local") // 테스트 환경 설정에 맞는 프로파일을 사용
//@Transactional // 트랜잭션 관리를 위해 사용
//class ProductRepositoryImplTest {
//    @Autowired
//    ProductRepository productRepository;
//    @Autowired
//    ProductRepositoryImpl paginationLegacy;
//
//    @Test
//    void 기존_페이징_방식() throws Exception {
//        //when
//        Pageable pageable = PageRequest.of(100000, 10); // 페이지 번호와 페이지 크기 설정
//        ProductSearchCondition condition = new ProductSearchCondition(); // 빈 ProductSearchCondition
//
//        List<Product> books = paginationLegacy.findAllByTowns(condition
//                , pageable, 1L).getContent(); // pageNo는 0부터 시작이라 1이면 두번째 페이지 조회
//
//        System.out.println("books = " + books);
//
//        //then
//        assertThat(books).hasSize(10);
//        assertThat(books.get(0).getTitle()).isEqualTo("Product 15331");
//    }
//}
