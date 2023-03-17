package ru.otus.lesson27.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.otus.lesson27.model.Status;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto {
    private Long id;
    private Date created;
    private Date updated;
    private Status status;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
