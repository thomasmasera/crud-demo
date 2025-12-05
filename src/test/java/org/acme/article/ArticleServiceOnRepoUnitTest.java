package org.acme.article;

import jakarta.inject.Inject;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//funzionano tutti ma 5 test falliscono se vengono eseguiti insieme agli altri, eseguendoli isolati no

@QuarkusTest
@TestTransaction
class ArticleServiceOnRepoUnitTest {

    @Inject
    ArticleServiceOnRepo service;

    @Inject
    ArticleMapper mapper;

    Article dbArticleCopy;

    ArticleServiceOnRepoUnitTest () {
        dbArticleCopy = new Article();
        dbArticleCopy.setName("pippo");
        dbArticleCopy.setDescription("grande amico di topolino");
        dbArticleCopy.setId(1L);
    }

    //INIZIO TEST CREATE
    @Test
    void createdSuccessfullyTest (){
        var inputVo = new ArticleVO(null, 0, "pippo", "pippo");
        var outputVo = new ArticleVO(2L, 0, "pippo", "pippo");
        assertEquals(service.create(inputVo), outputVo);
    }

    @Test
    void nullNameOnCreateTest () {
        var inputVo = new ArticleVO(null, 0, null, "pippo");
        assertThrows(WebApplicationException.class, () -> service.create(inputVo));
    }

    @Test
    void nullDescriptionOnCreateTest () {
        var inputVo = new ArticleVO(null, 0, "pippo", null);
        assertThrows(WebApplicationException.class, () -> service.create(inputVo));
    }

    @Test
    void nullVOOnCreateTest () {
        ArticleVO inputVo = null;
        assertThrows(WebApplicationException.class, () -> service.create(inputVo));
    }

    @Test
    void notNullIDOnCreateTest () {
        var inputVo = new ArticleVO(1L, 0, "pippo", "pippo");
        assertThrows(WebApplicationException.class, () -> service.create(inputVo));
    }

    @Test
    void notEqualsToZeroVersionOnCreateTest () {
        var inputVo = new ArticleVO(null, 1, null, "pippo");
        assertThrows(WebApplicationException.class, () -> service.create(inputVo));
    }
    //FINE TEST CREATE

    //INIZIO TEST LOAD
    @Test
    void loadedSuccessfullyTest() {
        assertEquals(service.load(1L), Optional.ofNullable(mapper.voFromEntity(dbArticleCopy)));
    }

    @Test
    void nullIdOnloadTest() {
        assertThrows(WebApplicationException.class, () -> service.load(null));
    }

    @Test
    void nonExistentIdOnLoadTest() {
        assertEquals(service.load(9L), Optional.empty());
    }
    //FINE TEST LOAD

    //INIZIO TEST UPDATE
    @Test
    void updatedSuccessfullyTest() {
        var inputVO = new ArticleVO(1L, 0, "pluto", "il cane di pippo");
        var outputVO = new ArticleVO(1L, 1, "pluto", "il cane di pippo");
        assertEquals(service.update(inputVO), outputVO);
    }

    @Test
    void updatedSuccessfullyMultipleTimeTest() {
        long version = 0;
        String string = "";
        boolean checker = false;
        while(version < 10){
            string += "a";
            var inputVO = new ArticleVO(1L, version++, string, string);
            var outputVO = new ArticleVO(1L, version, string, string);
            checker = service.update(inputVO).equals(outputVO);
        }
        assertTrue(checker);
    }

    @Test
    void nullVOOnUpdateTest () {
        ArticleVO inputVo = null;
        assertThrows(WebApplicationException.class, () -> service.update(inputVo));
    }

    @Test
    void versionMismatchOnUpdateTest() {
        var inputVO = new ArticleVO(1L, 2, "pluto", "il cane di pippo");
        assertThrows(WebApplicationException.class, () -> service.update(inputVO));
    }

    @Test
    void nonExistentIDOnUpdateTest() {
        var inputVO = new ArticleVO(2L, 0, "pluto", "il cane di pippo");
        assertThrows(WebApplicationException.class, () -> service.update(inputVO));
    }

    @Test
    void nullIDOnUpdateTest() {
        var inputVO = new ArticleVO(null, 0, "pluto", "il cane di pippo");
        assertThrows(WebApplicationException.class, () -> service.update(inputVO));
    }
    //FINE TEST UPDATE

    //INIZIO TEST DELETE
    @Test
    void deletedSuccessfullyTest () {
        service.delete(new ArticleVO(1L, 0, null, null));
        assertEquals(service.load(1L), Optional.empty());
    }

    @Test
    void nullVOOnDeleteTest () {
        ArticleVO inputVo = null;
        assertThrows(WebApplicationException.class, () -> service.delete(inputVo));
    }

    @Test
    void nullIDOnDeleteTest() {
        var inputVO = new ArticleVO(null, 0, "pippo", "pippo");
        assertThrows(WebApplicationException.class, () -> service.delete(inputVO));
    }

    @Test
    void nonExistentIDOnDeleteTest() {
        var inputVO = new ArticleVO(2L, 0, "pippo", "pippo");
        assertThrows(WebApplicationException.class, () -> service.delete(inputVO));
    }
    //FINE TEST DELETE

    //INIZIO TEST LISTALL
    @Test
    void listedAllSuccessfullyTest(){
        assertFalse(service.listAll().isEmpty());
    }
    //FINE TEST LISTALL

}