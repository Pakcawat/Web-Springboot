package project.model;

import java.util.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imageinfoes")
public class Imageinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String name;
    private String lastname;
    private String thainationalIDcard;
    private String buyin;

    public Imageinfo(String filename, String name, String lastname, String thainationalIDcard, String buyin, Date time, String url_image) {
        this.filename = filename;
        this.name = name;
        this.lastname = lastname;
        this.thainationalIDcard = thainationalIDcard;
        this.buyin = buyin;
        this.time = time;
        this.url_image = url_image;
    }

    @Column(name = "LastUpdateDateTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private String url_image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    private void onCreate(){
        Date date = new Date();
        time = date ;
    }

    public Imageinfo(String filename, String url_image) {
        this.filename = filename;
        this.url_image = url_image;
    }


}
