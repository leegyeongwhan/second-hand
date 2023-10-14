package com.secondhand.config;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.secondhand.domain.categorie.Category;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberProfile;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.town.Town;
import com.secondhand.web.dto.requset.ProductSaveRequest;
import net.jqwik.api.Arbitraries;

import java.util.List;

public class FixtureBuilderFactory {

    private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
            .defaultNotNull(true)
            .build();

    private static String[] nameList = {"감자1", "감자2", "감자3", "감자4"};
    private static String[] contentList = {"안녕하세요1", "안녕하세요2", "안녕하세요3"};
    private static String[] imageList = {"사진1", "사진2", "사진3", "사진4"};
    private static String[] townList = {"신촌", "홍대", "강남", "강북", "강서", "강동", "서초", "서대문", "마포", "종로", "용산", "성동", "성북", "중랑", "중구", "동대문", "동작", "관악"};
    private static String[] countyList = {"Suffolk", "Los Angeles County", "Cook"};
    private static String[] districtList = {"Manhattan", "Hollywood", "Downtown Chicago"};
    private static String[] categoryList = {"전자", "가구", "유아"};
    private static String[] etcList = {"판매중", "세일중", "판매"};

    private static String pickRandomString(String[] names) {

        return names[buildInteger(10000) % names.length];
    }

    public static int buildInteger(int bound) {

        return Arbitraries.integers().between(1, bound).sample();
    }

    public static int buildInteger() {

        return Arbitraries.integers().between(1, 100).sample();
    }

    public static long buildLong(int bound) {

        return Arbitraries.integers().between(1, bound).sample();
    }

    public static long buildCategoryId() {

        return Arbitraries.integers().between(1, 18).sample();
    }

    public static long buildTownId() {

        return Arbitraries.integers().between(1, 31).sample();
    }

    public static ArbitraryBuilder<Product> builderProduct() {

        return fixtureMonkey.giveMeBuilder(Product.class)
                .set("title", pickRandomString(nameList))
                .set("content", pickRandomString(contentList))
                .set("price", buildInteger())
                .set("thumbnailUrl", pickRandomString(imageList))
                .set("status", pickRandomString(etcList))
                .set("countLike", 0)
                .set("countView", 0)
                .set("deleted", false)
                .set("towns", builderTown().sample())
                .set("category", builderCategory().sample())
                .set("member", builderMember().sample());
    }

    public static ArbitraryBuilder<Category> builderCategory() {
        return fixtureMonkey.giveMeBuilder(Category.class)
                .set("name", pickRandomString(nameList))
                .set("imgUrl", pickRandomString(imageList))
                .set("placeholder", pickRandomString(nameList));
    }

    public static ArbitraryBuilder<Town> builderTown() {
        return fixtureMonkey.giveMeBuilder(Town.class)
                .set("city", pickRandomString(townList))
                .set("county", pickRandomString(countyList))
                .set("district", pickRandomString(districtList));
    }

    public static ArbitraryBuilder<ProductSaveRequest> builderProductSaveRequest() {
        return fixtureMonkey.giveMeBuilder(ProductSaveRequest.class)
                .set("title", pickRandomString(nameList))
                .set("content", pickRandomString(contentList))
                .set("price", buildInteger())
                .set("categoryId", buildCategoryId())
                .set("townId", buildTownId());
    }

    public static ArbitraryBuilder<Member> builderMember() {
        return fixtureMonkey.giveMeBuilder(Member.class)
                .set("id", buildLong(100))
                .set("loginName", pickRandomString(nameList))
                .set("imgUrl", pickRandomString(imageList))
                .set("oauthProvider", pickRandomString(nameList))
                .set("memberProfile", builderProfile().sample())
                .set("mainTown", builderTown().sample());
    }

    public static ArbitraryBuilder<MemberProfile> builderProfile() {
        return fixtureMonkey.giveMeBuilder(MemberProfile.class)
                .set("id", buildLong(100))
                .set("memberEmail", "email@email.com");
    }
}
