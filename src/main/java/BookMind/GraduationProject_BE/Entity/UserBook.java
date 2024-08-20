package BookMind.GraduationProject_BE.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserBook {

    @Id
    private String userbookId;

    private Long userId;
    private Long bookId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean favorite = false;
    private int lastReadPage = 0;
    private Date startDate = null;
    private Date endDate = null;

    private Byte rating = null;

    @PrePersist
    public void prePersist(){
        if (this.userbookId == null) {
            this.userbookId = userId + "-" + bookId;
        }
    }

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    public enum Status {
        READING, COMPLETED
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
