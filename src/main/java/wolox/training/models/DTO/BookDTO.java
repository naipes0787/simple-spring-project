package wolox.training.models.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {

    private String isbn;

    @JsonProperty
    private String title;

    @JsonProperty
    private String subtitle;

    @JsonProperty("cover")
    private ImageDTO image;

    @JsonProperty
    private List<AuthorDTO> publishers;

    @JsonProperty("publish_date")
    private String year;

    @JsonProperty("number_of_pages")
    private Integer pages;

    @JsonProperty
    private List<AuthorDTO> authors;

    public BookDTO() {

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbnFromResponse(String responseIsbn) {
        this.isbn = responseIsbn.replaceAll("ISBN:", "");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public List<AuthorDTO> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<AuthorDTO> publishers) {
        this.publishers = publishers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDTO> authors) {
        this.authors = authors;
    }

}
