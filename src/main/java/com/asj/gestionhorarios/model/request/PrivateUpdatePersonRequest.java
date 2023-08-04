package com.asj.gestionhorarios.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateUpdatePersonRequest {
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Lastname can't be blank")
    private String lastname;
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must be a valid email")
    private String email;
    @NotBlank(message = "CUIT/CUIL can't be blank")
    @Size(min = 4, message = "CUIT/CUIL must have more than 4 characters")
    private String cuil;
    @NotBlank(message = "Telephone can't be blank")
    private String tel;

    @NotNull(message = "Hours Jornal can't be null")
    @Positive(message = "Hours Jornal can't be negative")
    private Integer hours_journal;
    private byte[] image;
//    @NotBlank(message = "Role can't be blank")
    // Hay que validar que sea Admin, Management o Developer @ValidateString()
//    private String role;

//   public static Person toEntity(PrivateUpdatePersonRequest request){
//        return Person.builder()
//                .name(request.getName())
//                .lastname(request.getLastname())
//                .email(request.getEmail())
//                .cuil(request.getCuil())
//                .tel(request.getTel())
//                .image(request.getImage())
//                .password(request.getPassword())
//                .build();
//
//    }


}
