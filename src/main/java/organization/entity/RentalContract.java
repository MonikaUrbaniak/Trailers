package organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "rental_contracts")
public class RentalContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "trailer_id", nullable = true)
    private Trailer trailer;


//    @ManyToOne(optional = false)
//    @JoinColumn(name = "trailer_id")
//    private Trailer trailer;

    // ===== DANE UMOWY =====

    @Column(nullable = false)
    private LocalDate contractDate;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String returnLocation;

}
