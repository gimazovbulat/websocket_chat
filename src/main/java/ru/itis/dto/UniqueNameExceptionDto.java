package ru.itis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UniqueNameExceptionDto {
    private String entity;
    private String name;

    public String getErrorMessage(){
        return entity + " with name " + name + " already exists";
    }
}
