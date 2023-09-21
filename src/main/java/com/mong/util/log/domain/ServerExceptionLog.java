package com.mong.util.log.domain;

import com.mong.util.log.domain.exception.ExceptionData;
import com.mong.util.log.domain.meta.MetaData;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerExceptionLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MetaData metaData;

    @Embedded
    private ExceptionData exceptionData;

    @CreatedDate
    private LocalDateTime createDateTime;

    @Builder
    public ServerExceptionLog (MetaData metaData, ExceptionData exceptionData) {
        this.metaData = metaData;
        this.exceptionData = exceptionData;
    }
}
