package com.asj.gestionhorarios.model.response.Person;

import com.asj.gestionhorarios.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortPersonResponse {
    private String email;
    private String name;
    private String lastname;

    public static ShortPersonResponse toResponse(Person person) {
        return ShortPersonResponse.builder()
                .email(person.getEmail())
                .name(person.getName())
                .lastname(person.getLastname())
                .build();
    }
}
