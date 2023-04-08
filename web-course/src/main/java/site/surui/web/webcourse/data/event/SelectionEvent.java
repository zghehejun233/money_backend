package site.surui.web.webcourse.data.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import site.surui.web.webcourse.data.dto.SelectionEventDto;

@Setter
@Getter
@ToString
public class SelectionEvent extends ApplicationEvent {
    private SelectionEventDto selectionDto;

    public SelectionEvent(Object source, SelectionEventDto data) {
        super(source);
        this.selectionDto = data;
    }
}

