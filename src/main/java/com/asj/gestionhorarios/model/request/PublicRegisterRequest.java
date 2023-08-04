package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicRegisterRequest {
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must be a valid email")
    private String email;
    @NotBlank(message = "Password can't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "Password must have more than 7 characters and have at least one uppercase, one lowercase and one number")
    private String password;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Lastname can't be blank")
    private String lastname;
    @NotBlank(message = "Telephone can't be blank")
    private String tel;
    @NotBlank(message = "CUIT/CUIL can't be blank")
    @Size(min = 4, message = "CUIT/CUIL must have more than 4 characters")
    private String cuil;
    private byte[] image;

   public static Person toEnty(PublicRegisterRequest request){
        return Person.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .lastname(request.getLastname())
                .tel(request.getTel())
                .cuil(request.getCuil())
                .image(request.getImage())
                .build();
   }
}
