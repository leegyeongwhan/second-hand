package com.secondhand.domain.town;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Town {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "town_id")
    private Long townId;

    @Column(length = 45, nullable = false)
    private String city;

    @Column(length = 45, nullable = false)
    private String county;

    @Column(length = 45, nullable = false)
    private String district;

    @Builder
    public Town(Long townId, String city, String county, String district) {
        this.townId = townId;
        this.city = city;
        this.county = county;
        this.district = district;
    }
}
