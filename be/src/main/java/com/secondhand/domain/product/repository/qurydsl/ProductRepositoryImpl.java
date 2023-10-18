package com.secondhand.domain.product.repository.qurydsl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.Status;
import com.secondhand.web.dto.filtercondition.ProductCategorySearchCondition;
import com.secondhand.web.dto.filtercondition.ProductSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.secondhand.domain.categoriy.QCategory.category;
import static com.secondhand.domain.interested.QInterested.interested;
import static com.secondhand.domain.product.QProduct.product;
import static com.secondhand.domain.town.QTown.town;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductCustomRepository {

    // 페이지 크기를 10으로 고정
    public static final int PAGE_SIZE = 10;
    private final JPAQueryFactory jpaQueryFactory;
//
//    /**
//     * V1 offset paging
//     * @param condition
//     * @param pageable
//     * @param userId
//     * @return
//     */
//    @Override
//    public Slice<Product> findAllByTowns(ProductSearchCondition condition, Pageable pageable, long userId) {
//
//        log.debug("querydsl 실행 ========================");
//        JPAQuery<Product> query = jpaQueryFactory.selectFrom(product)
//                .leftJoin(product.towns, town).fetchJoin()
//                .leftJoin(product.category, category).fetchJoin()
//                .leftJoin(product.member, member).fetchJoin()
//                .where(locationEq(condition.getTownId()),
//                        categoryEq(condition.getCategoryId())
//                )
//                .orderBy(product.id.desc());
//
//        log.debug("offset = {}", pageable.getPageNumber() * PAGE_SIZE);
//        log.debug("pageable.getPageNumber() = {}", pageable.getPageNumber());
//        List<Product> products = query.offset(pageable.getPageNumber() * PAGE_SIZE)
//                .limit(PAGE_SIZE)
//                .fetch();
//
//        log.debug("qurelydsl 종료 =================");
//        int nextPageIndex = pageable.getPageNumber() * PAGE_SIZE;
//        return new SliceImpl<>(products, pageable, hasNext(products, PAGE_SIZE + nextPageIndex));
//    }

    /**
     * V2 Noffset paging
     *
     * @param condition
     * @param pageable
     * @param userId
     * @return
     */
    @Override
    public Slice<Product> findAllByTowns(ProductSearchCondition condition, Pageable pageable, long userId) {
        int pageSize = pageable.getPageSize() + 1;
        log.debug("querydsl 실행 ========================");

        List<Product> products = jpaQueryFactory.selectFrom(product)
                .where(locationEq(condition.getTownId()),
                        ltProductId(condition.getLastNum()),
                        categoryEq(condition.getCategoryId()),
                        isLikedEq(condition.getIsLiked(), userId),
                        isOnSales(condition),
                        product.deleted.eq(false))
                .limit(pageSize)
                .orderBy(product.id.desc())
                .fetch();

        System.out.println("pageSize = " + pageSize);
        log.debug("qurelydsl 종료 =================");
        return new SliceImpl<>(getContents(products, pageSize - 1), pageable, hasNext(products, pageSize - 1));
    }

    private Predicate ltProductId(Long productId) {
        if (productId == null) {
            return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다
        }
        return product.id.lt(productId);
    }

    private List<Product> getContents(List<Product> fetch, int pageSize) {
        return fetch.subList(0, Math.min(fetch.size(), pageSize));
    }


    @Override
    public Slice<Product> findAllByCategory(ProductCategorySearchCondition condition, Pageable pageable, long userId) {
        int pageSize = pageable.getPageSize() + 1;

        log.debug("querydsl 실행 ========================");

        JPAQuery<Product> query = jpaQueryFactory.selectFrom(product)
                .leftJoin(product.towns, town).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.member).fetchJoin()
                .leftJoin(product.interesteds, interested).fetchJoin()

                .where(
                        categoryEq(condition.getCategoryId()),
                        //      categoryListEq(condition.getCategoryId(), likedCategoryIds))
                        interested.isNotNull().and(interested.member.id.eq(userId)),
                        interested.member.id.eq(userId))
                //interested.product.category.categoryId.in(likedCategoryIds))
                .orderBy(product.id.desc());

        log.debug("offset = {}", pageable.getPageNumber() * PAGE_SIZE);
        log.debug("pageable.getPageNumber() = {}", pageable.getPageNumber());
        List<Product> products = query.offset(pageable.getPageNumber() * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .fetch();

        log.debug("qurelydsl 종료 =================");
        int nextPageIndex = pageable.getPageNumber() * PAGE_SIZE;
        return new SliceImpl<>(products, pageable, hasNext(products, PAGE_SIZE + nextPageIndex));
    }


    private static BooleanExpression isOnSales(ProductSearchCondition condition) {
        if (condition.getStatus() == null) {
            return null;
        }
        if (condition.getStatus() == 0) {
            return product.status.in(Status.SELLING, Status.RESERVING);
        }
        return product.status.eq(Status.SOLD);
    }


    private boolean hasNext(List<Product> fetch, int pageSize) {
        return fetch.size() > pageSize;
    }

    private BooleanExpression locationEq(Long locationId) {
        if (locationId == null) {
            return null;
        }
        return product.towns.townId.eq(locationId);
    }

    private BooleanExpression categoryEq(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return product.category.categoryId.eq(categoryId);
    }

    private BooleanExpression isLikedEq(Boolean isLiked, long userId) {
        if (isLiked == null) {
            return null;
        }
        if (isLiked) {
            return product.interesteds.any().member.id.eq(userId);
        } else {
            return product.interesteds.isEmpty();
        }
    }
}
