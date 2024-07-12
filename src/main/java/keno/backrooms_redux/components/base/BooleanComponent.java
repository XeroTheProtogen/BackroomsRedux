package keno.backrooms_redux.components.base;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface BooleanComponent extends Component {
    void setBool(boolean value);
    boolean getBool();
}
