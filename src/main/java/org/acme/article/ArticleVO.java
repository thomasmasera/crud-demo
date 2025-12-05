package org.acme.article;


import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArticleVO articleVO = (ArticleVO) o;
        return version == articleVO.version && Objects.equals(id, articleVO.id) && Objects.equals(name, articleVO.name) && Objects.equals(description, articleVO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, name, description);
    }
}