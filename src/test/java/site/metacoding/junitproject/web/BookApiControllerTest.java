package site.metacoding.junitproject.web;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import site.metacoding.junitproject.service.BookService;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;

//통합테스트(Controller, Service, Repository)
// 컨트롤러만 테스트하는 것이 아님
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void saveBook_test() throws Exception {
        // given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("스프링");
        bookSaveReqDto.setAuthor("헬로스프링부트");

        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("스프링");
        assertThat(author).isEqualTo("헬로스프링부트");
    }

    @Test
    public void di_test() {
        if (bookService == null) {
            System.out.println("null 이에요");
        } else {
            System.out.println("null 아니에요");
        }
    }

}
