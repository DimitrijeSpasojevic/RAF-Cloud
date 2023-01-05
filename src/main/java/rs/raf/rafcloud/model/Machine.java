package rs.raf.rafcloud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String status;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User createdBy;

    @Column(nullable=false)
    private Boolean active;

    @Column(name = "creation_date",nullable=false)
    private Date creationDate;

    @Column(nullable=false)
    private Boolean isDeleted = false;

//    @Column
//    @Version
//    private Integer version;

}
