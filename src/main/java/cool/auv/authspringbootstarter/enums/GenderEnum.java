package cool.auv.authspringbootstarter.enums;

import cool.auv.codegeneratorjpa.core.annotation.AutoEnum;
import cool.auv.codegeneratorjpa.core.enums.ISelectable;

@AutoEnum
public enum GenderEnum implements ISelectable {

    MALE("男"),
    FEMALE("女");

    private final String text;

    GenderEnum(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
