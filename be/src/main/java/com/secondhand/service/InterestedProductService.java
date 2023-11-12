package com.secondhand.service;

import com.secondhand.domain.categoriy.CategoryRepository;
import com.secondhand.domain.interested.Interested;
import com.secondhand.domain.interested.InterestedRepository;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.repository.ProductRepository;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.NotFoundException;
import com.secondhand.web.dto.requset.StatusOrLikeRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class InterestedProductService {

    private final ProductRepository productRepository;
    private final InterestedRepository interestedRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void changeLike(Long productId, Long memberId, StatusOrLikeRequest request) {
        if (request.getIsLiked()) {
            registerInterestedProduct(productId, memberId);
            return;
        }
        removeInterestedProduct(productId, memberId);
    }

    private void registerInterestedProduct(Long productId, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.NOT_FOUND,
                        String.format("%s 번호의 아이템을 찾을 수 없습니다.", productId)));
        Interested interested = interestedRepository.save(Interested.of(productId, memberId));
        product.increaseInterestedCount(interested);
    }

    private void removeInterestedProduct(Long productId, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> NotFoundException.productNotFound(ErrorMessage.NOT_FOUND, memberId));
        Optional<Interested> interested = interestedRepository.findByMemberIdAndProductId(
                memberId, productId);
        product.decreaseInterestedCount(interested.get());
        interestedRepository.deleteByProductIdAndMemberId(productId, memberId);
    }
}
