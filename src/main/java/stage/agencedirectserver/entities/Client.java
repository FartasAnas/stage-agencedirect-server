package stage.agencedirectserver.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity @Data @Table(name = "client")
@NoArgsConstructor @AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) @Size(max = 20)
    private String prenom;

    @Column(nullable = false) @Size(max = 20)
    private String nom;

    @Column(nullable = false,unique = true) @Size(max = 20)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;


    private String codeAccess;


    @Column(nullable = false,unique = true)
    private String CIN;

    @Column(nullable = false)
    private String telephone;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateNaissance;

    @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
    @Column(nullable = false) @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private Date dateInscription;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private Date dateExpiration;

    @Column(nullable = false)
    private String adresse;

    private String profession;

    @Column(nullable = false)
    private long codePostale;

    @Column(name = "cinRecto")
    private String cinRectoURL;
    @Column(name = "cinVerso")
    private String cinVersoURL;
    @Column(name = "selfie")
    private String selfieURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id")
    @JsonIncludeProperties(value = {"id","nom","ville","adresse"})
    private Agence agence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    @JsonIncludeProperties(value = {"id","nom","tarification"})
    private Pack pack;
}
