package stage.agencedirectserver.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CreationTimestamp;
import stage.agencedirectserver.utils.ToLowerCaseDeserializer;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity @Data @Table(name = "client")
@NoArgsConstructor @AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true) @Size(max = 20)
    private String RIB;

    @Column(nullable = false) @Size(max = 16)
    private String cleRIB= RandomStringUtils.random(2, "0123456789");

    @Column(nullable = false,unique = true) @Size(max = 16)
    private String accountNumber= RandomStringUtils.random(16, "0123456789");

    @Column(nullable = false) @Size(max = 20)
    private String prenom;

    @Column(nullable = false) @Size(max = 20)
    private String nom;

    @Column(nullable = false,unique = true) @Size(max = 20)
    @JsonDeserialize(converter = ToLowerCaseDeserializer.class)
    private String email;


    private String codeAccess;


    @Column(nullable = false,unique = true)
    @JsonDeserialize(converter = ToLowerCaseDeserializer.class)
    private String CIN;

    @Column(nullable = false)
    private String telephone;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateNaissance;

    @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
    @Column(nullable = false) @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateInscription;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateExpiration;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private long codePostale;

    @Column(name = "cinRecto",length = 1000000)
    private String cinRectoURL;
    @Column(name = "cinVerso",length = 1000000)
    private String cinVersoURL;
    @Column(name = "selfie",length = 1000000)
    private String selfieURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id")
    @JsonIncludeProperties(value = {"id","nom","ville","adresse"})
    private Agence agence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    @JsonIncludeProperties(value = {"id","nom","tarification"})
    private Pack pack;

    @ManyToMany(fetch = FetchType.EAGER) @JsonIgnore
    private Collection<Role> roles=new ArrayList<>();

    private boolean isActive=false;
}
