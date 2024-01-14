package com.ajith.dailyJobs.token;
import com.ajith.dailyJobs.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token {
        @Id
        @GeneratedValue( strategy = GenerationType.IDENTITY )
                private Integer id;
                private String token;
        @Enumerated(EnumType.STRING)
                private TokenType tokenType;
                private  boolean expired;
                private boolean revoked;
                private boolean isRefreshToken;
        @ManyToOne
        @JoinColumn(name = "worker_id")
                private Worker worker;
}
