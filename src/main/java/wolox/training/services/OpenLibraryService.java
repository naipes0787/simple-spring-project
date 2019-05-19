package wolox.training.services;

import java.util.Map;
import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.models.DTO.BookDTO;

@Service
class OpenLibraryService {

    private static final String API_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";
    private static final String FORMAT_PARAMETER = "&format=json&jscmd=data";

    Optional<BookDTO> bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        Optional<Map<String, BookDTO>> optionalResponse = Optional.ofNullable(restTemplate
            .exchange(
                API_URL + isbn + FORMAT_PARAMETER,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, BookDTO>>() {
                }).getBody());
        return optionalResponse.map(responseMap -> {
            String isbnFromResponse = responseMap.keySet().iterator().next();
            BookDTO bookDTO = responseMap.values().iterator().next();
            bookDTO.setIsbnFromResponse(isbnFromResponse);
            return bookDTO;
        });
    }

}
