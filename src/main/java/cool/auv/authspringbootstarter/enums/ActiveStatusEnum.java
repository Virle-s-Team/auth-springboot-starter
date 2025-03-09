package cool.auv.authspringbootstarter.enums;

import cool.auv.codegeneratorjpa.core.annotation.AutoEnum;
import cool.auv.codegeneratorjpa.core.enums.ISelectable;

@AutoEnum
public enum ActiveStatusEnum implements ISelectable {
    ACTIVE("激活"),
    INACTIVE("未激活");

    private final String text;

    ActiveStatusEnum(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
