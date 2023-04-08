package site.forum.web.common.data.dto;

public enum ReceiverTypeEnum {
    STUDENT(0),
    TEACHER(1),
    ADMIN(2);

    private final int type;

    ReceiverTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
