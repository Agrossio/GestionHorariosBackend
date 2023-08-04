package com.asj.gestionhorarios.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonPasswordRequest {
    @NotBlank(message = "Old password can't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "Password must have more than 7 characters and have at least one uppercase, one lowercase and one number")
    private String oldPassword;
    @NotBlank(message = "New password can't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "Password must have more than 7 characters and have at least one uppercase, one lowercase and one number")
    private String newPassword;
}
