package organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "trailers")
public class Trailer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "registration_number")
    private String registrationNumber;

    private Double width; // rozwazyc doulbe

    private Double length; // rozwazyc doulbe

    private String height;

    @Column(name = "has_spring")
    private Boolean hasSpring;

    @Column(name = "has_spare_wheel")
    private Boolean hasSpareWheel;

    @Column(name = "price_4h")
    private Integer price4h;

    @Column(name = "price_24h")
    private Integer price24h;

    @Column(name = "axle_count")
    private String axleCount;

    @Column(name = "additional_info")
    private String additionalInfo;

    public Trailer() {
    }
}
