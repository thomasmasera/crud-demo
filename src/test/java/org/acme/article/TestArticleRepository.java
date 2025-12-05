package org.acme.article;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

@Priority(1)
@Alternative
@ApplicationScoped
public class TestArticleRepository extends ArticleRepository{

    @PostConstruct
    public void init() {
        var article = new Article();
        article.setName("pippo");
        article.setDescription("grande amico di topolino");
        persist(article);
    }
}
