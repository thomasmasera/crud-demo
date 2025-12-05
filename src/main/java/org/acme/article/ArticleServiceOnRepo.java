package org.acme.article;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ArticleServiceOnRepo implements ArticleService {

    @Inject
    ArticleRepository articleRepository;
    @Inject
    ArticleMapper articleMapper;

    @Override
    @Transactional
    public ArticleVO create(ArticleVO articleVO) {
        if(articleVO == null || articleVO.getName() == null || articleVO.getDescription() == null){
            throw new WebApplicationException("Null VO or ull name and/or description", Response.Status.BAD_REQUEST);
        }
        if(articleVO.getId() != null || articleVO.getVersion() != 0) {
            throw new WebApplicationException("ID and version are assigned by internal logic", Response.Status.BAD_REQUEST);
        }
        Article article = articleMapper.entityFromVo(articleVO);
        articleRepository.persist(article);
        return articleMapper.voFromEntity(article);
    }

    @Override
    public Optional<ArticleVO> load(Long id) {
        if(id == null){
            throw new WebApplicationException("ID is null", Response.Status.BAD_REQUEST);
        }
        Article article = articleRepository.findById(id);
        return Optional.ofNullable(articleMapper.voFromEntity(article));
    }


    @Override
    @Transactional
    public ArticleVO update(ArticleVO articleVO) {
        if(articleVO == null){
            throw new WebApplicationException("Null VO", Response.Status.BAD_REQUEST);
        }
        if(articleVO.getId() != null) {
            Article article = articleRepository.findById(articleVO.getId());
            if (article != null) {
                try {
                    articleMapper.updateEntityWithVo(article, articleVO);
                    articleRepository.flush();
                    return articleMapper.voFromEntity(article);
                } catch (OptimisticLockException e) {
                    throw  new WebApplicationException("Version mismatch", Response.Status.PRECONDITION_FAILED);
                }
            } else {
                throw new WebApplicationException("Non existent article", Response.Status.NOT_FOUND);
            }
        }  else {
            throw new WebApplicationException("ID is null", Response.Status.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public void delete(ArticleVO articleVO) {
        if(articleVO == null){
            throw new WebApplicationException("Null VO", Response.Status.BAD_REQUEST);
        }
        if(articleVO.getId() != null) {
            try {
                articleRepository.delete(articleMapper.entityFromVo(articleVO));
            } catch (EntityNotFoundException e){
                throw  new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
            }
        } else {
            throw new WebApplicationException("ID is null", Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public List<ArticleVO> listAll() {
        List<Article> articles = articleRepository.listAll();
        List<ArticleVO> articleVOs = new ArrayList<>();
        for (Article article : articles){
            articleVOs.add(articleMapper.voFromEntity(article));
        }
        return articleVOs;
    }

}
