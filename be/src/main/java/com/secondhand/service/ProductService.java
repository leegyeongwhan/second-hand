package com.secondhand.service;

import com.secondhand.domain.categorie.Category;
import com.secondhand.exception.NotUserMineProductException;
import com.secondhand.exception.ProductNotFoundException;
import com.secondhand.domain.image.Image;
import com.secondhand.domain.image.ImageRepository;
import com.secondhand.domain.interested.Interested;
import com.secondhand.domain.interested.InterestedRepository;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.repository.ProductRepository;
import com.secondhand.domain.town.Town;
import com.secondhand.web.dto.requset.ProductSaveRequest;
import com.secondhand.web.dto.requset.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InterestedRepository interestedRepository;
    private final CategoryService categoryService;
    private final TownService townService;
    private final MemberService memberService;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Transactional
    public Long save(long userId, ProductSaveRequest requestInfo) {
        Category category = categoryService.findById(requestInfo.getCategoryId());
        Town town = townService.findById(requestInfo.getTownId());
        Member member = memberService.findMemberById(userId);
        Product product = Product.create(requestInfo, member, category, town);
        Product saveProduct = productRepository.save(product);
        List<String> imageUrls = imageService.uploadImageList(requestInfo.getProductImages()); //s3에 이미지 올라감

        saveProduct.updateThumbnail(imageUrls.get(0));
        for (String url : imageUrls) {
            Image image = new Image(url, saveProduct);
            imageRepository.save(image);
        }
        return saveProduct.getId();
    }

    @Transactional
    public void update(long productId, ProductUpdateRequest updateRequest, long userId) {
        Category category = categoryService.findById(updateRequest.getCategoryId());
        Town town = townService.findById(updateRequest.getTownId());
        Product product = findById(productId);
        checkIsMine(userId, product.getMember().getId());
        product.update(updateRequest, category, town);
        log.debug("product = {}", product);
    }

    @Transactional
    public void changeLike(long productId, long userId) {
        Member member = memberService.findMemberById(userId);
        Product product = findById(productId);
        Optional<Interested> interested = interestedRepository.findByMemberAndProduct(member, product);
        if (interested.isPresent()) {
            log.debug("이미 좋아요 누른경우 ======================");
            Interested existInterested = interested.get();
            product.decreaseCountView();
            interestedRepository.delete(existInterested);
            existInterested.deleteInterested(existInterested, member, product);
            return;
        }
        log.debug("처음 좋아요  누른경우 ======================");
        Interested newInterested = new Interested();
        newInterested.changeInterested(newInterested, member, product);
        interestedRepository.save(newInterested);
        product.increaseCountView();
        log.debug("상품 좋아요 수 = {}", product.getCountLike());
    }


    @Transactional
    public void changeStatus(long productId, long userId, Integer statusRequest) {
        Product product = findById(productId);
        Long memberId = product.getMember().getId();
        if (memberId == userId) {
            product.updateStatus(statusRequest);
            return;
        }
        throw new NotUserMineProductException();
    }

    @Transactional
    public void delete(long userId, long productId) {
        Product product = findById(productId);
        product.checkIsDetailPageMine(userId);
        if (product.checkIsMine(userId)) {
            productRepository.delete(product);
        }
    }

    private void checkIsMine(long userId, long product) {
        if (product == userId) {
            return;
        }
        throw new NotUserMineProductException();
    }

    public Product findById(long productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

}
