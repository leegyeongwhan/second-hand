//package com.secondhand.service;
//
//import com.secondhand.config.FixtureBuilderFactory;
//import com.secondhand.config.FixtureFactory;
//import com.secondhand.domain.categoriy.Category;
//import com.secondhand.domain.product.repository.Image.ImageRepository;
//import com.secondhand.domain.interested.InterestedRepository;
//import com.secondhand.domain.member.Member;
//import com.secondhand.domain.product.Product;
//import com.secondhand.domain.product.repository.ProductRepository;
//import com.secondhand.domain.town.Town;
//import com.secondhand.web.dto.requset.ProductSaveRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//    @Mock
//    private InterestedRepository interestedRepository;
//    @Mock
//    private CategoryService categoryService;
//    @Mock
//    private TownService townService;
//    @Mock
//    private MemberService memberService;
//    @Mock
//    private ImageService imageService;
//    @Mock
//    private ImageRepository imageRepository;
//    @InjectMocks
//    private ProductService productService;
//
//    private long userId = FixtureBuilderFactory.buildLong(1000);
//
//    @Nested
//    @DisplayName("상품의 제목, 내용, 가격, 카테고리, 지역 정보를 입력받아")
//    class saveTest {
//        private ProductSaveRequest productSaveRequest;
//        private Town town;
//        private Category category;
//        private Member member;
//        private Product product;
//
//        @BeforeEach
//        void setUp() {
//            productSaveRequest = FixtureBuilderFactory.builderProductSaveRequest().sample();
//            category = FixtureBuilderFactory.builderCategory().sample();
//            town = FixtureBuilderFactory.builderTown().sample();
//            member = FixtureBuilderFactory.builderMember().sample();
//            product = FixtureFactory.buildProductWithProductSaveRequestAndTownAndCategory(productSaveRequest, member, category, town);
//        }
//
//
//            @Test
//            @DisplayName("상품을 정상적으로 추가할 수 있다.")
//            void success() {
//
//                // given
//                given(categoryService.findById(productSaveRequest.getCategoryId()))
//                        .willReturn(category);
//                given(townService.findById(productSaveRequest.getTownId()))
//                        .willReturn(town);
//                given(memberService.findMemberById(userId))
//                        .willReturn(member);
//                given(productRepository.save(any(Product.class)))
//                        .willReturn(product);
//
//                // when
//            productService.save(userId, productSaveRequest, thumbnailImage, images);
//
//            // then
//            assertAll(
//                    () -> then(categoryService).should(times(1))
//                            .findById(productSaveRequest.getCategoryId()),
//                    () -> then(townService).should(times(1))
//                            .findById(productSaveRequest.getTownId()),
//                    () -> then(memberService).should(times(1))
//                            .findMemberById(userId),
//                    () -> then(productRepository).should(times(1))
//                            .save(any(Product.class))
//            );
//        }
//
//        @Test
//        @DisplayName("우산 고유번호가 이미 존재하는 경우 예외를 발생시킨다.")
//        void withSameId() {
//
//        }
//
//        @Test
//        @DisplayName("추가하려고 하는 가게 고유번호가 존재하지 않는 경우 예외를 발생시킨다.")
//        void atNonExistingStore() {
//
//        }
//    }
//
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void changeLike() {
//    }
//
//    @Test
//    void changeStatus() {
//    }
//
//    @Test
//    void delete() {
//    }
//
//    @Test
//    void findById() {
//    }
//}
