package rs.raf.rafcloud.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "errors")
public class MyError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errorId;

    @Column(nullable=false)
    private String errorText;

    @Column
    private Long machineId;

    @Column
    private Long userId;

}
