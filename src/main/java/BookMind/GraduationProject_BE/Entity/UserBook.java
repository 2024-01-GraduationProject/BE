package BookMind.GraduationProject_BE.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_book")
@Getter
@Setter
@NoArgsConstructor
public class UserBook {

    @Id
    private String userbookId;

    private Long userId;
    private Long bookId;

    @Enumerated(EnumType.STRING)
    private Status status = null; // 기본값을 null로 설정

    private Boolean favorite = false;
    private float lastReadPage = 0.0f; // float으로 변경
    private Date startDate = null;
    private Date endDate = null;
    private Byte rating = null;

    @OneToMany(mappedBy = "userBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBookIndices> indexPages = new ArrayList<>();

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
