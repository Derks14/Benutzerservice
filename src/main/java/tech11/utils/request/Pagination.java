package tech11.utils.request;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class Pagination {
    long count;
    int page;
    int size;
    int totalPages;
    boolean hasNext;
    boolean hasPrevious;

    public static Pagination build(PanacheQuery<?> panacheQuery) {
        Pagination pagination = new Pagination(
                panacheQuery.count(),
                panacheQuery.page().index,
                panacheQuery.page().size,
                panacheQuery.pageCount(),
                panacheQuery.hasNextPage(),
                panacheQuery.hasPreviousPage()

        );
        log.info("Successfully built generated pagination data for request [pagination = {}]", pagination.toString());
        return pagination;

    }
}
