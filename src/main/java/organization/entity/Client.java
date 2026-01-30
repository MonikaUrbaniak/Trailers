package organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import organization.service.ClientType;
import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType type;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;
    private String companyName;

    @NotBlank
    @Column(nullable = false)
    private String address;

    private String nip;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @NotBlank
    @Column(nullable = false)
    private String idNumber;

    private String idIssuedBy;
}

