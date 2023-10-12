package com.secondhand.config;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.secondhand.domain.categorie.Category;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.town.Town;
import com.secondhand.web.dto.requset.ProductSaveRequest;

import static com.secondhand.config.FixtureBuilderFactory.builderProduct;

public class FixtureFactory {

    private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
            .defaultNotNull(true)
            .build();


    public static Product buildProductWithId(long id) {

        return builderProduct()
                .set("id", id)
                .sample();
    }

    public static Product buildProductWithProductSaveRequestAndTownAndCategory(ProductSaveRequest productSaveRequest, Member member, Category category, Town town) {
        return builderProduct()
                .set("title", productSaveRequest.getTitle())
                .set("content", productSaveRequest.getContent())
                .set("price", productSaveRequest.getPrice())
                .set("category", category)
                .set("town", town)
                .set("member",member)
                .sample();
    }
}
