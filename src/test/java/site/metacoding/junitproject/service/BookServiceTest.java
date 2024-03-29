package site.metacoding.junitproject.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;
import site.metacoding.junitproject.web.dto.response.BookListRespDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    // 문제점 -> 서비스만 테스트하고 싶은데, 레포지토리 레이어가 함께 테스트 된다는 점 !!
    @Test
    public void 책등록하기_테스트() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit5");
        dto.setAuthor("메타코딩");

        // stub (가설)
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        // when
        BookRespDto bookRespDto = bookService.책등록하기(dto);

        // then
        // assertEquals(dto.getTitle(), bookRespDto.getTitle());
        // assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());

    }

    // 체크 포인트
    @Test
    public void 책목록보기_테스트() {
        // given(파라매터로 들어올 데이터)

        // stub(가설)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit5", "스프링부트"));
        books.add(new Book(2L, "springboot", "스프링"));
        when(bookRepository.findAll()).thenReturn(books);

        // when(실행)
        BookListRespDto bookListRespDto = bookService.책목록보기();

        // then(검증)
        assertThat(bookListRespDto.getItems().get(0).getTitle()).isEqualTo("junit5");
        assertThat(bookListRespDto.getItems().get(1).getTitle()).isEqualTo("springboot");
        assertThat(bookListRespDto.getItems().get(0).getAuthor()).isEqualTo("스프링부트");
        assertThat(bookListRespDto.getItems().get(1).getAuthor()).isEqualTo("스프링");

    }

    @Test
    public void 책한건보기_테스트() {
        // given
        Long id = 1L;

        // stub
        Book book = new Book(1L, "junit5", "스프링부트");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.책한건보기(id);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());

    }

    @Test
    public void 책수정하기_테스트() {

        // given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("springboot");
        dto.setAuthor("코딩코딩");

        // stub
        Book book = new Book(1L, "junit5", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.책수정하기(id, dto);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());

    }

}
