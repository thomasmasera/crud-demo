package org.acme.article;


import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@RegisterForReflection
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
//ho ritenuto necessario aggiungere i getter a questa classe per poter gestire gli ipotetici valori errati di id e version
//e per poter recuperare le entità tramite id dato che provando senza mi dava problemi di sessione
@Getter
public class ArticleVO {
    private Long id;
    private long version;
    @NotBlank
    private String name;
    private String description;
}