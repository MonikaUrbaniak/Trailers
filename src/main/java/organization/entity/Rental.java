package organization.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // klient
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    // przyczepa
    @ManyToOne(optional = false)
    @JoinColumn(name = "trailer_id")
    private Trailer trailer;

    // data i czas
    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDate;
}
