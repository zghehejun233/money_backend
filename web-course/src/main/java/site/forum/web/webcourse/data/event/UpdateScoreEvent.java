package site.forum.web.webcourse.data.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import site.forum.web.webcourse.data.dto.UpdateScoreDto;

@Setter
@Getter
@ToString
public class UpdateScoreEvent extends ApplicationEvent {

    private UpdateScoreDto updateScoreDto;

    public UpdateScoreEvent(Object source, UpdateScoreDto data) {
        super(source);
        this.updateScoreDto = data;
    }
}
