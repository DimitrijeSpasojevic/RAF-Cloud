package rs.raf.rafcloud.model;

import lombok.Getter;
import lombok.Setter;
import rs.raf.rafcloud.actions.ActionEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "errors")
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errorId;

    @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

    @Column(nullable=false)
    private Long machineId;

    @Column(nullable=false)
    private Long userId;

    @Column(nullable=false)
    private ActionEnum actionEnum;

    @Column(nullable=false)
    private String errorText;





}
