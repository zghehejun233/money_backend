package site.forum.web.webcourse.data.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.Date;


@Getter
@Setter
@ToString
public class SelectionProcedureEvent extends ApplicationEvent {
    private Date time;
    private Long courseId;

    public SelectionProcedureEvent(Object source, Date time, Long courseId) {
        super(source);
        this.time = time;
        this.courseId = courseId;
    }

    public SelectionProcedureEvent(Object source, Clock clock, Date time, Long courseId) {
        super(source, clock);
        this.time = time;
        this.courseId = courseId;
    }
}
