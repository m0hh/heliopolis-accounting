package com.helioplis.accounting.shift;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "ShiftDTOMapping",
        classes = @ConstructorResult(
                targetClass = ShiftDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "user_id_opened", type = Integer.class), // Replace with the actual column name
                        @ColumnResult(name = "user_id_closed", type = Integer.class), // Replace with the actual column name
                        @ColumnResult(name = "total_shift", type = BigDecimal.class),
                        @ColumnResult(name = "created_at", type = LocalDateTime.class),
                        @ColumnResult(name = "closed_at", type = LocalDateTime.class)
                }
        )
)
@Data
public class ShiftDTO {
    private Integer id;
    private UserHelioplis userOpen;
    private UserHelioplis userClose;
    private BigDecimal totalShift;
    private LocalDateTime createdAt;
    private LocalDateTime closed_at;
}